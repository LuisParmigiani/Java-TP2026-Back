package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.UsuarioDTORequest;
import soda_roja.backend.dtoResponse.UsuarioDTOResponse;
import soda_roja.backend.model.Persona;
import soda_roja.backend.model.Usuario;
import soda_roja.backend.repository.PersonaRepository;
import soda_roja.backend.repository.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PersonaRepository personaRepository;

    public List<UsuarioDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public UsuarioDTOResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
    }

    public UsuarioDTOResponse save(UsuarioDTORequest entidad) {
        Persona persona = findPersonaOrThrow(entidad.getPersonaId());

        Usuario usuario = Usuario.builder()
                .nombreUsuario(entidad.getNombreUsuario())
                .contrasena(entidad.getContrasena())
                .nivelAcceso(entidad.getNivelAcceso())
                .persona(persona)
                .build();
        return mapToDTO(repository.save(usuario));
    }

    public UsuarioDTOResponse update(Long id, UsuarioDTORequest entidad) {
        Persona persona = findPersonaOrThrow(entidad.getPersonaId());

        Usuario existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));

        existing.setNombreUsuario(entidad.getNombreUsuario());
        existing.setContrasena(entidad.getContrasena());
        existing.setNivelAcceso(entidad.getNivelAcceso());
        existing.setPersona(persona);

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
                .nivelAcceso(usuario.getNivelAcceso())
                .persona(new PersonaService().mapToDTO(usuario.getPersona()))
                .build();
    }
}
