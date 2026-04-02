package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.dtoRequest.UsuarioDTORequest;
import soda_roja.backend.dtoResponse.UsuarioDTOResponse;
import soda_roja.backend.model.*;
import soda_roja.backend.repository.PersonaRepository;
import soda_roja.backend.repository.UsuarioRepository;

import java.util.List;
@Service

public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private PersonaRepository PersonaRepository;

    public List<UsuarioDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public UsuarioDTOResponse getById(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        return mapToDTO(usuario);
    }

    public UsuarioDTOResponse save(UsuarioDTORequest entidad) {
        Persona persona = PersonaRepository.findById(entidad.getPersonaId())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + entidad.getPersonaId()));

        Usuario usuario = Usuario.builder()
                .nombreUsuario(entidad.getNombreUsuario())
                .contrasena(entidad.getContrasena())
                .nivelAcceso(entidad.getNivelAcceso())
                .persona(persona)
                .build();
        return mapToDTO(repository.save(usuario));
    }

    public UsuarioDTOResponse update(Long id, UsuarioDTORequest entidad) {
        Usuario existing = repository.findById(id).orElseThrow(()-> new RuntimeException("Usuario no encontrado con id: " + id));

        Persona persona = PersonaRepository.findById(entidad.getPersonaId())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + entidad.getPersonaId()));
        existing.setNombreUsuario(entidad.getNombreUsuario());
        existing.setContrasena(entidad.getContrasena());
        existing.setNivelAcceso(entidad.getNivelAcceso());
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
                .nivelAcceso(usuario.getNivelAcceso())
                .persona(new PersonaService().mapToDTO(usuario.getPersona()))
                .build();
    }
}
