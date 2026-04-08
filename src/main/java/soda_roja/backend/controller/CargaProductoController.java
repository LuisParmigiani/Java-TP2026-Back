package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.CargaProductoDTORequest;
import soda_roja.backend.dtoResponse.CargaProductoDTOResponse;
import soda_roja.backend.service.CargaProductoService;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/carga-producto")
public class CargaProductoController {

    @Autowired
    private CargaProductoService service;

    @GetMapping
    public ResponseEntity<List<CargaProductoDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CargaProductoDTOResponse> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<CargaProductoDTOResponse> create(@Valid @RequestBody CargaProductoDTORequest entidad) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CargaProductoDTOResponse> update(@PathVariable long id, @Valid @RequestBody CargaProductoDTORequest entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
