package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.DiaDomicilioDTORequest;
import soda_roja.backend.dtoRequestPut.DiaDomicilioDTORequestPut;
import soda_roja.backend.dtoResponse.DiaDomicilioDTOResponse;
import soda_roja.backend.service.DiaDomicilioService;
import jakarta.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/dia-domicilio")
public class DiaDomicilioController {

    @Autowired
    private DiaDomicilioService service;

    @GetMapping
    public ResponseEntity<List<DiaDomicilioDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll(populate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiaDomicilioDTOResponse> getById(
            @PathVariable Long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getById(id, populate));
    }

    @PostMapping
    public ResponseEntity<DiaDomicilioDTOResponse> create(
            @Valid
            @RequestBody DiaDomicilioDTORequest entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad, populate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaDomicilioDTOResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody DiaDomicilioDTORequestPut entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.update(id, entidad, populate));
    }

    @PutMapping("/multiple")
    public ResponseEntity<List<DiaDomicilioDTOResponse>> updateMultiple(
            @Valid
            @RequestBody List<DiaDomicilioDTORequestPut> entidades,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.updateMultiple(entidades, populate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
