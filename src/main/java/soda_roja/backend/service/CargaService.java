package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.model.Carga;
import soda_roja.backend.repository.CargaRepository;

import java.util.List;

@Service
public class CargaService {

    @Autowired
    private CargaRepository repository;

    public List<Carga> getAll() {
        return repository.findAll();
    }

    public Carga getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Carga save(Carga entidad) {
        return repository.save(entidad);
    }

    public Carga update(Long id, Carga entidad) {
        Carga existing = repository.findById(id).orElseThrow();
        existing.setTipo(entidad.getTipo());
        existing.setFechaHora(entidad.getFechaHora());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
