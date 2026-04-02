package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.model.Camion;
import soda_roja.backend.repository.CamionRepository;

import java.util.List;
@Service

public class CamionService {

    @Autowired
    private CamionRepository repository;

    public List<Camion> getAll() {
        return repository.findAll();
    }

    public Camion getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Camion save(Camion entidad) {
        return repository.save(entidad);
    }

    public Camion update(Long id, Camion entidad) {
        Camion existing = repository.findById(id).orElseThrow();
        existing.setPatente(entidad.getPatente());
        existing.setModelo(entidad.getModelo());
        existing.setMarca(entidad.getMarca());
        existing.setKilometraje(entidad.getKilometraje());
        
       


        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
