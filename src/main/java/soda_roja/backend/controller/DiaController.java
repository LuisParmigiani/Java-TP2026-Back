package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.service.DiaService;
import soda_roja.backend.dtoRequest.DiaDTORequest;
import soda_roja.backend.dtoResponse.DiaDTOResponse;
import jakarta.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/dia")
public class DiaController {

    @Autowired
    private DiaService service;

    @GetMapping
    public ResponseEntity<List<DiaDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll(populate));
    }


    @GetMapping("/{id}")
    public ResponseEntity<DiaDTOResponse> getById(
            @PathVariable Long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getById(id,populate));
    }


    @PostMapping
    public ResponseEntity<DiaDTOResponse> create(
            @Valid
            @RequestBody DiaDTORequest entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad,populate));
    }


    @PutMapping("/{id}")
    public ResponseEntity<DiaDTOResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody DiaDTORequest entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.update(id, entidad,populate));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
