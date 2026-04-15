package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.UsuarioDTORequest;
import soda_roja.backend.dtoRequestPut.UsuarioDTORequestPut;
import soda_roja.backend.dtoResponse.UsuarioDTOResponse;
import soda_roja.backend.model.Persona;
import soda_roja.backend.model.Usuario;
import soda_roja.backend.repository.PersonaRepository;
import soda_roja.backend.repository.UsuarioRepository;
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
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public List<UsuarioDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public UsuarioDTOResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
    }
    
    public Usuario getByEmail(String email) {
		return repository.findByEmail(email)
				.orElse(null);
	}

    public UsuarioDTOResponse save(UsuarioDTORequest entidad) {
        Persona persona = findPersonaOrThrow(entidad.getPersonaId());

        Usuario usuario = Usuario.builder()
                .nombreUsuario(entidad.getNombreUsuario())
                .contrasena(passwordEncoder.encode(entidad.getContrasena()))
                .nivelAcceso(entidad.getNivelAcceso())
                .email(entidad.getEmail())
                .persona(persona)
                .build();
        return mapToDTO(repository.save(usuario));
    }

    public UsuarioDTOResponse update(Long id, UsuarioDTORequestPut entidad) {
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
        }

        return mapToDTO(repository.save(existing));
    }

    public void delete(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
        repository.delete(usuario);
    }

    private Persona findPersonaOrThrow(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));
    }
    


    public UsuarioDTOResponse mapToDTO(Usuario usuario) {
        return UsuarioDTOResponse.builder()
                .id(usuario.getId())
                .nombreUsuario(usuario.getNombreUsuario())
                .email(usuario.getEmail())
                .nivelAcceso(usuario.getNivelAcceso())
                .persona(personaService.mapToDTO(usuario.getPersona()))
                .build();
    }
    
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
