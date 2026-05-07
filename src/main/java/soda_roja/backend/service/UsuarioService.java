package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import soda_roja.backend.dtoRequest.UsuarioDTORequest;
import soda_roja.backend.dtoRequestPut.UsuarioDTORequestPut;
import soda_roja.backend.dtoResponse.UsuarioDTOResponse;
import soda_roja.backend.model.Persona;
import soda_roja.backend.model.Usuario;
import soda_roja.backend.repository.PersonaRepository;
import soda_roja.backend.repository.UsuarioRepository;
import soda_roja.backend.repository.CargaRepository;
import soda_roja.backend.specification.CargaSpecification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    private CloudinaryService cloudinaryService;
    
    @Autowired
    private CargaRepository cargaRepository;


    @Autowired
    private MapToDTO mapToDTOMapper;
    
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
    public UsuarioDTOResponse update(Long id, UsuarioDTORequestPut entidad, MultipartFile file, String[] populate) {
        Usuario existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));

        if(entidad.getPersonaId() != null) {
            Persona persona = findPersonaOrThrow(entidad.getPersonaId());
            existing.setPersona(persona);
        }
        if(entidad.getNombreUsuario() != null) {
            existing.setNombreUsuario(entidad.getNombreUsuario());
        }
        if(entidad.getContrasena() != null) {
            existing.setContrasena(passwordEncoder.encode(entidad.getContrasena()));
        }
        if(entidad.getNivelAcceso() != null) {
            existing.setNivelAcceso(entidad.getNivelAcceso());
        }
        if(entidad.getEmail() != null) {
            existing.setEmail(entidad.getEmail());
            if(existing.getPersona() != null) {
                existing.getPersona().setEmail(entidad.getEmail());
            }
        }

        if (file != null && !file.isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadImage(file, "usuario",existing.getId());
                existing.setImagenUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Error uploading image: " + e.getMessage());
            }
        }

        return mapToDTO(repository.save(existing), populate);
    }

    public void delete(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));

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

        repository.delete(usuario);
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
