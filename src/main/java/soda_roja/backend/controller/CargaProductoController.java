package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.CargaProductoDTORequest;
import soda_roja.backend.dtoRequestPut.CargaProductoDTORequestPut;
import soda_roja.backend.dtoResponse.CargaProductoDTOResponse;
import soda_roja.backend.service.CargaProductoService;
import jakarta.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("/api/carga-producto")
public class CargaProductoController {

    @Autowired
    private CargaProductoService service;

    @GetMapping
    public ResponseEntity<List<CargaProductoDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll(populate));
    }


    @GetMapping("/{id}")
    public ResponseEntity<CargaProductoDTOResponse> getById(
            @PathVariable long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getById(id,populate));
    }


    @PostMapping
    public ResponseEntity<CargaProductoDTOResponse> create(
            @Valid
            @RequestBody CargaProductoDTORequest entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad,populate));
    }


    @PutMapping("/{id}")
    public ResponseEntity<CargaProductoDTOResponse> update(
            @PathVariable long id,
            @Valid
            @RequestBody CargaProductoDTORequestPut entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.update(id, entidad,populate));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
