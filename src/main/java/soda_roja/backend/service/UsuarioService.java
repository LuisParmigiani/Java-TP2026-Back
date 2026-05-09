package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import soda_roja.backend.dtoRequest.UsuarioDTORequest;
import soda_roja.backend.dtoRequestPut.PersonaDTORequestPut;
import soda_roja.backend.dtoRequestPut.UsuarioDTORequestPut;
import soda_roja.backend.dtoResponse.UsuarioDTOResponse;
import soda_roja.backend.dtoResponse.CargaDTOResponse;
import soda_roja.backend.dtoResponse.PersonaDTOResponse;
import soda_roja.backend.model.Persona;
import soda_roja.backend.model.Usuario;
import soda_roja.backend.repository.PersonaRepository;
import soda_roja.backend.repository.UsuarioRepository;
import soda_roja.backend.repository.CargaRepository;
import soda_roja.backend.specification.CargaSpecification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PersonaRepository personaRepository;
    
    @Autowired
    private PersonaService personaService;
    
    @Autowired
    private VentaService ventaService;
    
    @Autowired
    private PedidoSemanalService pedidoSemanalService;
    @Autowired
    private CargaService cargaService;
    
    @Autowired
    private CloudinaryService cloudinaryService;
    
    @Autowired
    private CargaRepository cargaRepository;


    @Autowired
    private MapToDTO mapToDTOMapper;

    @Autowired
    private MailService mailService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public List<UsuarioDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(u -> mapToDTO(u, populate)).toList();
    }

    public UsuarioDTOResponse getById(Long id, String[] populate) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
        
        UsuarioDTOResponse responseDTO = mapToDTO(usuario, populate);
        
        // Calcular y asignar el precio del último pedido semanal
        double precioPedidosSemanales = pedidoSemanalService.getTotalMontoByPersona(usuario.getId());
        responseDTO.setPrecioPedidosSemanales(precioPedidosSemanales);
        
        return responseDTO;
    }
    
    public List<UsuarioDTOResponse> getByNivelAcceso(String nivelAcceso, String[] populate, String estado, String cargas) {
        List<UsuarioDTOResponse> usuarios = repository.findByNivelAcceso(nivelAcceso).stream()
                .map(u -> mapToDTO(u, populate))
                .toList();

        ZoneId zone = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zone);
        Instant startInstant = today.atStartOfDay(zone).toInstant();
        Instant endInstant = today.plusDays(1).atStartOfDay(zone).toInstant().minusMillis(1);

        Date startOfDay = Date.from(startInstant);
        Date endOfDay = Date.from(endInstant);

            	System.out.println("Estado filter applied: " + estado);
        return usuarios.stream().filter(usuario -> {
            if (estado != null && !estado.isEmpty() && estado.equalsIgnoreCase("Todos")) {
            	System.out.println("Estado filter applied: " + estado);
            	if (cargas == "si") {
                List<CargaDTOResponse> cargasHoy = cargaService.getByUsuarioIdAndFechaHoraBetween(
                        usuario.getId(), startOfDay, endOfDay, new String[] { "camion" });
                usuario.setCargas(cargasHoy);}
                return true;
            } else {
                if (usuario.getPersona().getEstado().equalsIgnoreCase("deshabilitado")) {
                    return false;
                } else {
                	if (cargas == "si") {
                    List<CargaDTOResponse> cargasHoy = cargaService.getByUsuarioIdAndFechaHoraBetween(
                            usuario.getId(), startOfDay, endOfDay, new String[] { "camion" });
                    usuario.setCargas(cargasHoy);
                	}
                    return true;
                }
            }
        }).toList();
    }


    public Usuario getByEmail(String email,String[] populate) {
		return repository.findByEmail(email)
				.orElse(null);
	}

    public UsuarioDTOResponse save(UsuarioDTORequest entidad,String[] populate) {
        Persona persona = findPersonaOrThrow(entidad.getPersonaId());

        Usuario usuario = Usuario.builder()
                .nombreUsuario(entidad.getNombreUsuario())
                .contrasena(passwordEncoder.encode(entidad.getContrasena()))
                .nivelAcceso(entidad.getNivelAcceso())
                .email(entidad.getEmail())
                .persona(persona)
                .build();

        Usuario usuarioGuardado = repository.save(usuario);

        mailService.enviarMail(
            usuarioGuardado.getEmail(),
            "Tu cuenta fue creada - Soda Roja",
            "<h1>Tu cuenta fue creada en Soda Roja</h1>" +
            "<p>Hola <b>" + usuarioGuardado.getNombreUsuario() + "</b>, tu cuenta ha sido registrada con los siguientes datos:</p>" +
            "<br>" +
            "<p>Usuario: <b>" + usuarioGuardado.getNombreUsuario() + "</b></p>" +
            "<p>Email: <b>" + usuarioGuardado.getEmail() + "</b></p>" +
            "<p>Nivel de acceso: <b>" + usuarioGuardado.getNivelAcceso() + "</b></p>" +
            "<br>" +
            "<p>Si no solicitaste la creación de esta cuenta, contactate con nosotros a través de los medios presentes aquí debajo.</p>"
        );

        return  mapToDTO(usuarioGuardado, populate);

    }

    public UsuarioDTOResponse getEmpleado(Long id, String[] populate) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));

        // Cargar las cargas de hoy del usuario
        cargarCargasDeHoy(usuario);

        return mapToDTO(usuario, populate);
    }

    @Transactional
    public UsuarioDTOResponse update(Long id,UsuarioDTORequestPut entidad, MultipartFile file, String[] populate) {
        Usuario existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));

        StringBuilder cambios = new StringBuilder();

        if(entidad.getPersonaId() != null) {
            existing.setPersona(findPersonaOrThrow(entidad.getPersonaId()));
        }
        if(entidad.getNombreUsuario() != null) {
            cambios.append("<p>Nombre de usuario: <b>").append(entidad.getNombreUsuario()).append("</b></p>");
            existing.setNombreUsuario(entidad.getNombreUsuario());
        }
        if(entidad.getContrasena() != null) {
            cambios.append("<p>Contraseña: <b>actualizada</b></p>");
            existing.setContrasena(passwordEncoder.encode(entidad.getContrasena()));
        }
        if(entidad.getNivelAcceso() != null) {
            cambios.append("<p>Nivel de acceso: <b>").append(entidad.getNivelAcceso()).append("</b></p>");
            existing.setNivelAcceso(entidad.getNivelAcceso());
        }
        if(entidad.getEmail() != null) {
            cambios.append("<p>Email: <b>").append(entidad.getEmail()).append("</b></p>");
            existing.setEmail(entidad.getEmail());
            if(existing.getPersona() != null) {
                existing.getPersona().setEmail(entidad.getEmail());
            }
        }
        if (file != null && !file.isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadImage(file, "usuario", existing.getId());
                existing.setImagenUrl(imageUrl);
                cambios.append("<p>Foto de perfil: <b>actualizada</b></p>");
            } catch (Exception e) {
                throw new RuntimeException("Error uploading image: " + e.getMessage());
            }
        }
        Usuario saved = repository.save(existing);

        if (cambios.length() > 0) {
            mailService.enviarMail(
                saved.getEmail(),
                "Tu cuenta fue actualizada - Soda Roja",
                "<h1>Tu cuenta fue actualizada</h1>" +
                "<p>Hola <b>" + saved.getNombreUsuario() + "</b>, te informamos que se realizaron cambios en tu cuenta.</p>" +
                "<br><h3>Datos actualizados:</h3>" +
                cambios +
                "<br><p>Si no solicitaste estos cambios, contactate con nosotros a través de los medios presentes aquí debajo.</p>"
            );
        }

        return mapToDTO(saved, populate);
    }
    @Transactional
    public UsuarioDTOResponse updateWithPersona(Long id, Long personaId, UsuarioDTORequestPut user, PersonaDTORequestPut persona, String[] populate) {
        Usuario existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
        
        if (personaId != existing.getPersona().getId()) {
            throw new IllegalArgumentException("El ID de persona no coincide con el usuario existente");
        }
        
        Persona existingPersona = personaRepository.findById(personaId)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + personaId));
        
        // Actualizar persona
        if(persona.getNombre() != null) existingPersona.setNombre(persona.getNombre());
        if(persona.getApellido() != null) existingPersona.setApellido(persona.getApellido());
        if(persona.getTipoDoc() != null) existingPersona.setTipoDoc(persona.getTipoDoc());
        if(persona.getNroDocumento() != null) existingPersona.setNroDocumento(persona.getNroDocumento());
        if(persona.getTelefono() != null) existingPersona.setTelefono(persona.getTelefono());
        
        // Actualizar usuario
        if(user.getNombreUsuario() != null) existing.setNombreUsuario(user.getNombreUsuario());
        if(user.getContrasena() != null) existing.setContrasena(passwordEncoder.encode(user.getContrasena()));
        if(user.getNivelAcceso() != null) existing.setNivelAcceso(user.getNivelAcceso());
        if(user.getEmail() != null) {
            existing.setEmail(user.getEmail());
            existingPersona.setEmail(user.getEmail());
        }
        
        // Guardar ambas entidades
        personaRepository.save(existingPersona);
        repository.save(existing);
        
        return mapToDTO(existing, populate);
    }

            
    @Transactional
    public void logicDelete(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));

        // Eliminar imagen si existe
        String imagenUrl = usuario.getImagenUrl();
        if (imagenUrl != null && !imagenUrl.isEmpty()) {
            try {
                String publicId = imagenUrl.substring(
                    imagenUrl.indexOf("/", imagenUrl.indexOf("/upload/") + 8) + 1,
                    imagenUrl.lastIndexOf(".")
                );
                cloudinaryService.deleteImage(publicId);
            } catch (Exception e) {
                throw new RuntimeException("Error deleting image: " + e.getMessage());
            }
        }
        
        // Obtener la persona antes de eliminar el usuario
        Persona persona = usuario.getPersona(); 
        persona.setEstado("Deshabilitado");
        personaRepository.save(persona);
        
        
        
    }

    private Persona findPersonaOrThrow(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));
    }
    
    private void cargarCargasDeHoy(Usuario usuario) {
        var cargas = cargaRepository.findAll(CargaSpecification.cargasDeHoyPorUsuario(usuario.getId()));
        usuario.setCargas(cargas);
    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    private UsuarioDTOResponse mapToDTO(Usuario usuario, String[] populate) {
        return mapToDTOMapper.mapToDTO(usuario, populate);
    }

}
