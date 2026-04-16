package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequestPut.PedidoSemanalDTORequestPut;
import soda_roja.backend.service.PedidoSemanalService;
import soda_roja.backend.dtoRequest.PedidoSemanalDTORequest;
import soda_roja.backend.dtoResponse.PedidoSemanalDTOResponse;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pedidoSemanal")
public class PedidoSemanalController {

    @Autowired
    private PedidoSemanalService service;

    @GetMapping
    public ResponseEntity<List<PedidoSemanalDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoSemanalDTOResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<PedidoSemanalDTOResponse> create(@Valid @RequestBody PedidoSemanalDTORequest entidad) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoSemanalDTOResponse> update(@PathVariable Long id, @Valid @RequestBody PedidoSemanalDTORequestPut entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
