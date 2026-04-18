package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.OrdenZonaDTORequest;
import soda_roja.backend.dtoRequestPut.OrdenZonaDTORequestPut;
import soda_roja.backend.dtoResponse.OrdenZonaDTOResponse;
import soda_roja.backend.service.OrdenZonaService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orden-zona")
public class OrdenZonaController {

    @Autowired
    private OrdenZonaService service;

    @GetMapping
    public ResponseEntity<List<OrdenZonaDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll(populate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenZonaDTOResponse> getById(
            @PathVariable Long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getById(id, populate));
    }

    @PostMapping
    public ResponseEntity<OrdenZonaDTOResponse> create(
            @Valid
            @RequestBody OrdenZonaDTORequest entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad, populate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdenZonaDTOResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody OrdenZonaDTORequestPut entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.update(id, entidad, populate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
