package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import soda_roja.backend.dtoRequest.ProductoPersonaDomicilioDTORequest;
import soda_roja.backend.dtoResponse.ProductoPersonaDomicilioDTOResponse;
import soda_roja.backend.service.ProductoPersonaDomicilioService;

import java.util.List;

@RestController
@RequestMapping("/api/producto-persona-domicilio")
@Tag(name = "ProductoPersonaDomicilio", description = "Gestión de asociaciones entre Productos y PersonaDomicilio")
public class ProductoPersonaDomicilioController {

    @Autowired
    private ProductoPersonaDomicilioService service;

    @GetMapping
    @Operation(summary = "Obtener todas las asociaciones ProductoPersonaDomicilio")
    public ResponseEntity<List<ProductoPersonaDomicilioDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una asociación ProductoPersonaDomicilio por ID")
    public ResponseEntity<ProductoPersonaDomicilioDTOResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva asociación ProductoPersonaDomicilio")
    public ResponseEntity<ProductoPersonaDomicilioDTOResponse> save(@Valid @RequestBody ProductoPersonaDomicilioDTORequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una asociación ProductoPersonaDomicilio")
    public ResponseEntity<ProductoPersonaDomicilioDTOResponse> update(@PathVariable Long id, @Valid @RequestBody ProductoPersonaDomicilioDTORequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una asociación ProductoPersonaDomicilio")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
