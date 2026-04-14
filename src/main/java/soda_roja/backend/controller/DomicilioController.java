package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequestPut.DomicilioDTORequestPut;
import soda_roja.backend.service.DomicilioService;
import soda_roja.backend.dtoRequest.DomicilioDTORequest;
import soda_roja.backend.dtoResponse.DomicilioDTOResponse;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/domicilio")
public class DomicilioController {

    @Autowired
    private DomicilioService service;

    @GetMapping
    public ResponseEntity<List<DomicilioDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DomicilioDTOResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("token/usuario")
    public ResponseEntity<List<DomicilioDTOResponse>> getByUserId(
            @AuthenticationPrincipal String userId
            , @RequestParam(required = false) String estado
            , @RequestParam(required = false) Integer dias
    ){
        return ResponseEntity.ok(service.getByUserId(Long.parseLong(userId), estado, dias));
    }

    @PostMapping
    public ResponseEntity<DomicilioDTOResponse> create(@Valid @RequestBody DomicilioDTORequest entidad) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DomicilioDTOResponse> update(@PathVariable Long id, @Valid @RequestBody DomicilioDTORequestPut entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
