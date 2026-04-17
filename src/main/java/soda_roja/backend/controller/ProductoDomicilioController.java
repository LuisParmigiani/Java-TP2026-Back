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
    public ResponseEntity<List<ProductoDomicilioDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll(populate));
    }


    @GetMapping("/{id}")

    @Operation(summary = "Obtener una asociación ProductoPersonaDomicilio por ID")
    public ResponseEntity<ProductoDomicilioDTOResponse> getById(
            @PathVariable Long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getById(id,populate));
    }


    @PostMapping

    @Operation(summary = "Crear una nueva asociación ProductoPersonaDomicilio")
    public ResponseEntity<ProductoDomicilioDTOResponse> save(
            @Valid @RequestBody ProductoDomicilioDTORequest request,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request,populate));
    }


    @PutMapping("/{id}")

    @Operation(summary = "Actualizar una asociación ProductoPersonaDomicilio")
    public ResponseEntity<ProductoDomicilioDTOResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductoDomicilioDTORequestPut request,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.update(id, request,populate));
    }


    @DeleteMapping("/{id}")

    @Operation(summary = "Eliminar una asociación ProductoPersonaDomicilio")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
