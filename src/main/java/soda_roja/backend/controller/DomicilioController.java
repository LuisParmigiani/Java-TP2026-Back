package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoResponse.DomicilioDTOResponse;
import soda_roja.backend.service.DomicilioService;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/domicilio")

public class DomicilioController {

    @Autowired
    private DomicilioService service;

    // GET - traer todos
    @GetMapping
    public ResponseEntity<List<DomicilioDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET - traer por ID
    @GetMapping("/{id}")
    public ResponseEntity<DomicilioDTOResponse> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // POST - crear nuevo
    @PostMapping
    public ResponseEntity<DomicilioDTOResponse> create(@Valid @RequestBody DomicilioDTORequest entidad) {
        return ResponseEntity.ok(service.save(entidad));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<DomicilioDTOResponse> update(@PathVariable long id, @Valid @RequestBody DomicilioDTORequest entidad) {
        {
            return ResponseEntity.ok(service.update(id, entidad));
        }
    }
    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
