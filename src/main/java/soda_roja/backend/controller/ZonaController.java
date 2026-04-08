package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.ZonaDTORequest;
import soda_roja.backend.dtoResponse.ZonaDTOResponse;
import soda_roja.backend.service.ZonaService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/zona")
public class ZonaController {

    @Autowired
    private ZonaService service;

    @GetMapping
    public ResponseEntity<List<ZonaDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZonaDTOResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ZonaDTOResponse> create(@Valid @RequestBody ZonaDTORequest entidad) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ZonaDTOResponse> update(@PathVariable Long id, @Valid @RequestBody ZonaDTORequest entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
