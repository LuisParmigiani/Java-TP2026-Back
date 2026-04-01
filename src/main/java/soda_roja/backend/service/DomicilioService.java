package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.model.Domicilio;
import soda_roja.backend.repository.DomicilioRepository;

import java.util.List;
@Service

public class DomicilioService {

    @Autowired
    private DomicilioRepository repository;

    public List<Domicilio> getAll() {
        return repository.findAll();
    }

    public Domicilio getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Domicilio save(Domicilio entidad) {
        return repository.save(entidad);
    }

    public Domicilio update(Long id, Domicilio entidad) {
        Domicilio existing = repository.findById(id).orElseThrow();
        existing.setCalle(entidad.getCalle());
        existing.setNumero(entidad.getNumero());
        existing.setCasa(entidad.getCasa());

        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
