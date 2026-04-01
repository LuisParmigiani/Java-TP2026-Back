package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.model.PersonaDomicilio;
import soda_roja.backend.repository.PersonaDomicilioRepository;

import java.util.List;
@Service

public class PersonaDomicilioService {
    @Autowired
    private PersonaDomicilioRepository repository;

    public List<PersonaDomicilio> getAll() {
        return repository.findAll();
    }

    public PersonaDomicilio getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public PersonaDomicilio save(PersonaDomicilio entidad) {
        return repository.save(entidad);
    }

    public PersonaDomicilio update(Long id, PersonaDomicilio entidad) {
        PersonaDomicilio existing = repository.findById(id).orElseThrow();
        existing.setDia(entidad.getDia());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
