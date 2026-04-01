package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.model.Usuario;
import soda_roja.backend.repository.UsuarioRepository;

import java.util.List;
@Service

public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public List<Usuario> getAll() {
        return repository.findAll();
    }

    public Usuario getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Usuario save(Usuario entidad) {
        return repository.save(entidad);
    }

    public Usuario update(Long id, Usuario entidad) {
        Usuario existing = repository.findById(id).orElseThrow();
        existing.setNombreUsuario(entidad.getNombreUsuario());
        existing.setContrasena(entidad.getContrasena());
        existing.setNivelAcceso(entidad.getNivelAcceso());


        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
