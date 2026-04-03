package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.PersonaDomicilioDTORequest;
import soda_roja.backend.dtoResponse.PersonaDomicilioDTOResponse;
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
    public ResponseEntity<List<PersonaDomicilioDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET - traer por ID
    @GetMapping("/{id}")
    public ResponseEntity<PersonaDomicilioDTOResponse> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // POST - crear nuevo
    @PostMapping
    public ResponseEntity<PersonaDomicilioDTOResponse> create(@Valid @RequestBody PersonaDomicilioDTORequest entidad) {
        return ResponseEntity.ok(service.save(entidad));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<PersonaDomicilioDTOResponse> update(@PathVariable long id, @Valid @RequestBody PersonaDomicilioDTORequest entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
