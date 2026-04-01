package soda_roja.backend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.model.Zona;
import soda_roja.backend.repository.ZonaRepository;

import java.util.List;
@Service
public class ZonaService {

    @Autowired
    private ZonaRepository repository;

    public List<Zona> getAll() {
        return repository.findAll();
    }

    public Zona getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Zona save(Zona entidad) {
        return repository.save(entidad);
    }

    public Zona update(Long id, Zona entidad) {
        Zona existing = repository.findById(id).orElseThrow();
        existing.setDetalle(entidad.getDetalle());
        existing.setNombre(entidad.getNombre());


        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
