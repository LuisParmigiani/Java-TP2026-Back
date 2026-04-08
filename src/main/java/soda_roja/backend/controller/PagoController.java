package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.service.PagoService;
import soda_roja.backend.dtoRequest.PagoDTORequest;
import soda_roja.backend.dtoResponse.PagoDTOResponse;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pago")
public class PagoController {

    @Autowired
    private PagoService service;

    @GetMapping
    public ResponseEntity<List<PagoDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDTOResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<PagoDTOResponse> create(@Valid @RequestBody PagoDTORequest entidad) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoDTOResponse> update(@PathVariable Long id, @Valid @RequestBody PagoDTORequest entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
