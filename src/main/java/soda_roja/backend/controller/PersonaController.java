package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequestPut.PersonaDTORequestPut;
import soda_roja.backend.service.PersonaService;
import soda_roja.backend.dtoRequest.PersonaDTORequest;
import soda_roja.backend.dtoResponse.PersonaDTOResponse;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/persona")
public class PersonaController {

    @Autowired
    private PersonaService service;

    @GetMapping
    public ResponseEntity<List<PersonaDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll(populate));
    }


    @GetMapping("/{id}")
    public ResponseEntity<PersonaDTOResponse> getById(
            @PathVariable Long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getById(id,populate));
    }


    @PostMapping
    public ResponseEntity<PersonaDTOResponse> create(
            @Valid
            @RequestBody PersonaDTORequest entidad
            ,@RequestParam(required = false) String[] populate
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad,populate));
    }


    @PutMapping("/{id}")
    public ResponseEntity<PersonaDTOResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody PersonaDTORequestPut entidad,
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
