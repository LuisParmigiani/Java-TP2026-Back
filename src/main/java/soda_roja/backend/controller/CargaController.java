package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.CargaDTORequest;
import soda_roja.backend.dtoResponse.CargaDTOResponse;
import soda_roja.backend.service.CargaService;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/carga")
public class CargaController {

    @Autowired
    private CargaService service;

    @GetMapping
    public ResponseEntity<List<CargaDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CargaDTOResponse> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<CargaDTOResponse> create(@Valid @RequestBody CargaDTORequest entidad) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CargaDTOResponse> update(@PathVariable long id, @Valid @RequestBody CargaDTORequest entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
