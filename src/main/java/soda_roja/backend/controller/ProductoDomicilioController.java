package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import soda_roja.backend.dtoRequest.ProductoDomicilioDTORequest;
import soda_roja.backend.dtoRequestPut.ProductoDomicilioDTORequestPut;
import soda_roja.backend.dtoResponse.ProductoDomicilioDTOResponse;
import soda_roja.backend.service.ProductoDomicilioService;

import java.util.List;

@RestController
@RequestMapping("/api/producto-persona-domicilio")
@Tag(name = "ProductoPersonaDomicilio", description = "Gestión de asociaciones entre Productos y PersonaDomicilio")
public class ProductoDomicilioController {

    @Autowired
    private ProductoDomicilioService service;

    @GetMapping
    @Operation(summary = "Obtener todas las asociaciones ProductoPersonaDomicilio")
    public ResponseEntity<List<ProductoDomicilioDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una asociación ProductoPersonaDomicilio por ID")
    public ResponseEntity<ProductoDomicilioDTOResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva asociación ProductoPersonaDomicilio")
    public ResponseEntity<ProductoDomicilioDTOResponse> save(@Valid @RequestBody ProductoDomicilioDTORequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una asociación ProductoPersonaDomicilio")
    public ResponseEntity<ProductoDomicilioDTOResponse> update(@PathVariable Long id, @Valid @RequestBody ProductoDomicilioDTORequestPut request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una asociación ProductoPersonaDomicilio")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
