package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.service.ProductoService;
import soda_roja.backend.dtoRequest.ProductoDTORequest;
import soda_roja.backend.dtoResponse.ProductoDTOResponse;
import soda_roja.backend.model.Producto;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/producto")
public class ProductoController {

    @Autowired
    private ProductoService service;

    // GET - traer todos
    @GetMapping
    public ResponseEntity<List<ProductoDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET - traer por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTOResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // POST - crear nuevo
    @PostMapping
    public ResponseEntity<ProductoDTOResponse> create(@Valid @RequestBody ProductoDTORequest entidad) {
        return ResponseEntity.ok(service.save(entidad));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTOResponse> update(@PathVariable Long id, @Valid @RequestBody ProductoDTORequest entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}