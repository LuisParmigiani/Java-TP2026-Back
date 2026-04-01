package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.service.PersonaService;
import soda_roja.backend.model.Persona;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/persona")

public class PersonaController {


    @Autowired
    private PersonaService service;

    // GET - traer todos
    @GetMapping
    public ResponseEntity<List<Persona>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET - traer por ID
    @GetMapping("/{id}")
    public ResponseEntity<Persona> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // POST - crear nuevo
    @PostMapping
    public ResponseEntity<Persona> create(@Valid @RequestBody Persona entidad) {
        return ResponseEntity.ok(service.save(entidad));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Persona> update(@PathVariable long id, @Valid @RequestBody Persona entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
