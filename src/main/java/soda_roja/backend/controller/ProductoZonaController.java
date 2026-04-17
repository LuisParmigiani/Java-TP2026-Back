package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import soda_roja.backend.dtoRequest.ProductoZonaDTORequest;
import soda_roja.backend.dtoRequestPut.ProductoZonaDTORequestPut;
import soda_roja.backend.dtoResponse.ProductoZonaDTOResponse;
import soda_roja.backend.service.ProductoZonaService;

import java.util.List;

@RestController
@RequestMapping("/api/producto-zona")
@Tag(name = "ProductoZona", description = "Gestión de asociaciones entre Productos y Zonas")
public class ProductoZonaController {

    @Autowired
    private ProductoZonaService service;

    @GetMapping
    @Operation(summary = "Obtener todas las asociaciones ProductoZona")
    public ResponseEntity<List<ProductoZonaDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll(populate));
    }


    @GetMapping("/{id}")

    @Operation(summary = "Obtener una asociación ProductoZona por ID")
    public ResponseEntity<ProductoZonaDTOResponse> getById(
            @PathVariable Long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getById(id, populate));
    }


    @PostMapping

    @Operation(summary = "Crear una nueva asociación ProductoZona")
    public ResponseEntity<ProductoZonaDTOResponse> save(
            @Valid @RequestBody ProductoZonaDTORequest request,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request, populate));
    }


    @PutMapping("/{id}")

    @Operation(summary = "Actualizar una asociación ProductoZona")
    public ResponseEntity<ProductoZonaDTOResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductoZonaDTORequestPut request,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.update(id, request, populate));
    }


    @DeleteMapping("/{id}")

    @Operation(summary = "Eliminar una asociación ProductoZona")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
