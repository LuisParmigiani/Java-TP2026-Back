package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.service.DomicilioService;
import soda_roja.backend.model.Domicilio;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/domicilio")

public class DomicilioController {

    @Autowired
    private DomicilioService service;

    // GET - traer todos
    @GetMapping
    public ResponseEntity<List<Domicilio>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET - traer por ID
    @GetMapping("/{id}")
    public ResponseEntity<Domicilio> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // POST - crear nuevo
    @PostMapping
    public ResponseEntity<Domicilio> create(@Valid @RequestBody Domicilio entidad) {
        return ResponseEntity.ok(service.save(entidad));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Domicilio> update(@PathVariable long id, @Valid @RequestBody Domicilio entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
