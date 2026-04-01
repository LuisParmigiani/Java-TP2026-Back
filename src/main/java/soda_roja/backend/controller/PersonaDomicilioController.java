package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.service.PersonaDomicilioService;
import soda_roja.backend.model.PersonaDomicilio;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/personaDomicilio")

public class PersonaDomicilioController {
    @Autowired
    private PersonaDomicilioService service;

    // GET - traer todos
    @GetMapping
    public ResponseEntity<List<PersonaDomicilio>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET - traer por ID
    @GetMapping("/{id}")
    public ResponseEntity<PersonaDomicilio> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // POST - crear nuevo
    @PostMapping
    public ResponseEntity<PersonaDomicilio> create(@Valid @RequestBody PersonaDomicilio entidad) {
        return ResponseEntity.ok(service.save(entidad));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<PersonaDomicilio> update(@PathVariable long id, @Valid @RequestBody PersonaDomicilio entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
