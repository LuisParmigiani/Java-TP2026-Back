package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;

import soda_roja.backend.dtoRequest.LineaPedidoDTORequest;
import soda_roja.backend.dtoResponse.LineaPedidoDTOResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.service.LineaPedidoService;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/lineaPedido")


public class LineaPedidoController {

    @Autowired
    private LineaPedidoService service;

    // GET - traer todos
    @GetMapping
    public ResponseEntity<List<LineaPedidoDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET - traer por ID
    @GetMapping("/{id}")
    public ResponseEntity<LineaPedidoDTOResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // POST - crear nuevo
    @PostMapping
    public ResponseEntity<LineaPedidoDTOResponse> create(@Valid @RequestBody LineaPedidoDTORequest entidad) {
        return ResponseEntity.ok(service.save(entidad));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<LineaPedidoDTOResponse> update(@PathVariable Long id, @Valid @RequestBody LineaPedidoDTORequest entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
