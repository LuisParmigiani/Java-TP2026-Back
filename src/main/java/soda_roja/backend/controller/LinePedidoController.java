package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.service.LineaPedidoService;
import soda_roja.backend.model.LineaPedido;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/linePedido")


public class LinePedidoController {

    @Autowired
    private LineaPedidoService service;

    // GET - traer todos
    @GetMapping
    public ResponseEntity<List<LineaPedido>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET - traer por ID
    @GetMapping("/{id}")
    public ResponseEntity<LineaPedido> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // POST - crear nuevo
    @PostMapping
    public ResponseEntity<LineaPedido> create(@Valid @RequestBody LineaPedido entidad) {
        return ResponseEntity.ok(service.save(entidad));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<LineaPedido> update(@PathVariable long id, @Valid @RequestBody LineaPedido entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
