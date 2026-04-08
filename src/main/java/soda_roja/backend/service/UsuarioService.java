package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.dtoRequest.UsuarioDTORequest;
import soda_roja.backend.dtoResponse.UsuarioDTOResponse;
import soda_roja.backend.model.*;
import soda_roja.backend.repository.PersonaRepository;
import soda_roja.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
@Service

public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private PersonaRepository PersonaRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public List<UsuarioDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public UsuarioDTOResponse getById(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        return mapToDTO(usuario);
    }
    
    public Usuario getByEmail(String email) {
		return repository.findByEmail(email)
				.orElse(null);
	}

    public UsuarioDTOResponse save(UsuarioDTORequest entidad) {
        Persona persona = PersonaRepository.findById(entidad.getPersonaId())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + entidad.getPersonaId()));

        Usuario usuario = Usuario.builder()
                .nombreUsuario(entidad.getNombreUsuario())
                .contrasena(passwordEncoder.encode(entidad.getContrasena()))
                .nivelAcceso(entidad.getNivelAcceso())
                .email(entidad.getEmail())
                .persona(persona)
                .build();
        return mapToDTO(repository.save(usuario));
    }

    public UsuarioDTOResponse update(Long id, UsuarioDTORequest entidad) {
        Usuario existing = repository.findById(id).orElseThrow(()-> new RuntimeException("Usuario no encontrado con id: " + id));

        Persona persona = PersonaRepository.findById(entidad.getPersonaId())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + entidad.getPersonaId()));
        existing.setNombreUsuario(entidad.getNombreUsuario());
        existing.setContrasena(passwordEncoder.encode(entidad.getContrasena()));
        existing.setNivelAcceso(entidad.getNivelAcceso());
        existing.setEmail(entidad.getEmail());
        existing.setPersona(persona);


        return mapToDTO(repository.save(existing));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
    


    public UsuarioDTOResponse mapToDTO(Usuario usuario) {
        return UsuarioDTOResponse.builder()
                .id(usuario.getId())
                .nombreUsuario(usuario.getNombreUsuario())
                .email(usuario.getEmail())
                .nivelAcceso(usuario.getNivelAcceso())
                .persona(new PersonaService().mapToDTO(usuario.getPersona()))
                .build();
    }
    
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
