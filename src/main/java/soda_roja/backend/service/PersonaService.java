package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.PersonaDTORequest;
import soda_roja.backend.dtoRequestPut.PersonaDTORequestPut;
import soda_roja.backend.dtoResponse.PersonaDTOResponse;
import soda_roja.backend.model.Domicilio;
import soda_roja.backend.model.Persona;
import soda_roja.backend.model.Usuario;
import soda_roja.backend.model.Zona;
import soda_roja.backend.repository.DomicilioRepository;
import soda_roja.backend.repository.PersonaRepository;
import soda_roja.backend.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import soda_roja.backend.model.Camion;
import soda_roja.backend.model.Dia;
import soda_roja.backend.specification.PersonaSpecification;

import java.util.List;


@Service
public class PersonaService {

    @Autowired
    private PersonaRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private DomicilioRepository domicilioRepository;

    @Autowired
    private MapToDTO mapToDTOMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Page<PersonaDTOResponse> getAll(int page, int size, String[] populate) {
        return repository.findAll(PageRequest.of(page, size))
            .map(p -> mapToDTO(p, populate));
    }
    
    public Page<PersonaDTOResponse> getAllClientes(int page, int size, String[] populate) {
        return repository.findAllClientes(PageRequest.of(page, size))
            .map(p -> mapToDTO(p, populate));
    }
    public Page<PersonaDTOResponse> getByNameAndFiltered(String query, Zona zona, Camion camion, Dia dia, String ordenSaldo, String nivelAcceso,int page, int size, String[] populate) {
        Specification<Persona> spec = Specification.where(PersonaSpecification.porZona(zona))
            .and(PersonaSpecification.porCamion(camion))
            .and(PersonaSpecification.porDia(dia))
            .and((root, query1, cb) -> {
                if (query == null || query.isEmpty()) return null;
                return cb.or(
                    cb.like(cb.lower(root.get("nombre")), "%" + query.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("apellido")), "%" + query.toLowerCase() + "%")
                );
            })
            // Nueva especificación para filtrar por nivel de acceso
            .and((root, query1, cb) -> {
                if (nivelAcceso == null || nivelAcceso.isEmpty()) return null;
                return cb.equal(root.join("usuario").get("nivelAcceso"), nivelAcceso);
            });;
        
        Sort sort = "Descendente".equals(ordenSaldo) ? 
            Sort.by(Sort.Direction.DESC, "saldo") : 
            Sort.by(Sort.Direction.ASC, "saldo");
        
        return repository.findAll(spec, PageRequest.of(page, size, sort))
            .map(p -> mapToDTO(p, populate));
    }

    public List<PersonaDTOResponse> getByNameOnly(String query, String[] populate) {
    	String estado="Usuario";
        Specification<Persona> spec = Specification.where(PersonaSpecification.porNivelAcceso(estado))
        		.and((root, queryObj, cb) -> {
            if (query == null || query.isEmpty()) {
                return cb.conjunction();
            }
            return cb.or(
                cb.like(cb.lower(root.get("nombre")), "%" + query.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("apellido")), "%" + query.toLowerCase() + "%")
            );
        });
        
        return repository.findAll(spec).stream()
            .map(p -> mapToDTO(p, populate))
            .toList();
    }
    public Page<Persona> getAllFiltrado(Zona zona, Camion camion, Dia dia, String ordenSaldo, int page, int size) {
        Specification<Persona> spec = Specification.where(PersonaSpecification.porZona(zona))
            .and(PersonaSpecification.porCamion(camion))
            .and(PersonaSpecification.porDia(dia));
        
        Sort sort = "Descendente".equals(ordenSaldo) ? 
            Sort.by(Sort.Direction.DESC, "saldo") : 
            Sort.by(Sort.Direction.ASC, "saldo");
        
        return repository.findAll(spec, PageRequest.of(page, size, sort));
    }
 

    public PersonaDTOResponse getById(Long id,String[] populate) {
        return repository.findById(id)
                .map(p -> mapToDTO(p, populate))
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));
    }

    public List<PersonaDTOResponse> getActiveClient(String[] populate) {
        List<Persona> activos = repository.findByEstado("Habilitado");
        if (activos.isEmpty()) {
            throw new EntityNotFoundException("No se encontró ningún cliente activo");
        }
        return activos.stream()
                .map(p -> mapToDTO(p, populate))
                .toList();
    }


    public PersonaDTOResponse save(PersonaDTORequest entidad,String[] populate) {


        Persona persona = Persona.builder()
                .tipoDoc(entidad.getTipoDoc())
                .nroDocumento(entidad.getNroDocumento())
                .nombre(entidad.getNombre())
                .apellido(entidad.getApellido())
                .email(entidad.getEmail())
                .telefono(entidad.getTelefono())
                .saldo(entidad.getSaldo())
                .estado(entidad.getEstado()!= null ? entidad.getEstado() : "Pendiente") // Si no se proporciona estado, se asigna "Pendiente"
                .build();

        Persona personaGuardada = repository.save(persona);

        String nombreUsuario = (personaGuardada.getNombre() + personaGuardada.getApellido() + personaGuardada.getNroDocumento())
                .toLowerCase()
                .replaceAll("\\s+", "");

        Usuario usuario = Usuario.builder()
                .nombreUsuario(nombreUsuario)
                .contrasena(passwordEncoder.encode(personaGuardada.getNroDocumento()))
                .nivelAcceso("Usuario")
                .email(personaGuardada.getEmail())
                .persona(personaGuardada)
                .build();

        usuarioRepository.save(usuario);

        if (personaGuardada.getEmail() != null && !personaGuardada.getEmail().isBlank()) {
            mailService.enviarMail(
                personaGuardada.getEmail(),
                "Tu cuenta fue creada - Soda Roja",
                "<h1>¡Bienvenido/a a Soda Roja!</h1>" +
                "<p>Hola <b>" + personaGuardada.getNombre() + " " + personaGuardada.getApellido() + "</b>, te informamos que tu cuenta y usuario fueron creados exitosamente.</p>" +
                "<br>" +
                "<p>Podés ingresar con los siguientes datos:</p>" +
                "<p>Usuario: <b>" + nombreUsuario + "</b></p>" +
                "<p>Nivel de acceso: <b>Usuario</b></p>" +
                "<br>" +
                "<p>Si no reconocés este registro, contactate con nosotros a través de los medios presentes aquí debajo.</p>"
            );
        }

        return mapToDTO(personaGuardada, populate);
    }

    public PersonaDTOResponse update(Long id, PersonaDTORequestPut entidad,String[] populate) {
        Persona existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));

        if(entidad.getTipoDoc() != null) {
            existing.setTipoDoc(entidad.getTipoDoc());
        }
        if(entidad.getNroDocumento() != null) {
            existing.setNroDocumento(entidad.getNroDocumento());
        }
        if(entidad.getNombre() != null) {
            existing.setNombre(entidad.getNombre());
        }
        if(entidad.getApellido() != null) {
            existing.setApellido(entidad.getApellido());
        }
        if(entidad.getEmail() != null) {
            existing.setEmail(entidad.getEmail());
            // Sincronizar email con el usuario asociado
            if(existing.getUsuario() != null) {
                existing.getUsuario().setEmail(entidad.getEmail());
            }
        }
        if(entidad.getTelefono() != null) {
            existing.setTelefono(entidad.getTelefono());
        }
        if(entidad.getSaldo() != null) {
            float saldoAnterior = existing.getSaldo();
            existing.setSaldo(entidad.getSaldo());
            if (entidad.getSaldo() > saldoAnterior) {
                float montoCargado = entidad.getSaldo() - saldoAnterior;
                mailService.enviarMail(
                    existing.getEmail(),
                    "Carga de saldo - Soda Roja",
                    "<h1>¡Se acreditó saldo en tu cuenta!</h1>" +
                    "<p>Hola <b>" + existing.getNombre() + " " + existing.getApellido() + "</b>, te informamos que se realizó una carga de saldo en tu cuenta.</p>" +
                    "<br>" +
                    "<p>Monto cargado: <b>$" + String.format("%.2f", montoCargado) + "</b></p>" +
                    "<p>Saldo anterior: <b>$" + String.format("%.2f", saldoAnterior) + "</b></p>" +
                    "<p>Saldo actual: <b>$" + String.format("%.2f", entidad.getSaldo()) + "</b></p>"
                );
            }
        }
        if(entidad.getEstado() != null) {
			existing.setEstado(entidad.getEstado());
		}

        return mapToDTO(repository.save(existing), populate);
    }

    public void delete(Long id) {
        Persona persona = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));
        repository.delete(persona);
    }
    public void logicDelete(Long id) {
		Persona persona = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));
		persona.setEstado("Deshabilitado");
		repository.save(persona);
	}

    private Domicilio findDomicilioOrThrow(Long id) {
        return domicilioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + id));
    }

    private PersonaDTOResponse mapToDTO(Persona persona, String[] populate) {
        return mapToDTOMapper.mapToDTO(persona, populate);
    }


}
