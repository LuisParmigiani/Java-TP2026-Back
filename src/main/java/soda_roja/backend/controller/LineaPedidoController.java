package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.LineaPedidoDTORequest;
import soda_roja.backend.dtoResponse.LineaPedidoDTOResponse;
import soda_roja.backend.service.LineaPedidoService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/lineaPedido")
public class LineaPedidoController {

    @Autowired
    private LineaPedidoService service;

    @GetMapping
    public ResponseEntity<List<LineaPedidoDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineaPedidoDTOResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<LineaPedidoDTOResponse> create(@Valid @RequestBody LineaPedidoDTORequest entidad) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineaPedidoDTOResponse> update(@PathVariable Long id, @Valid @RequestBody LineaPedidoDTORequest entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
