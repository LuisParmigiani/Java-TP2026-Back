package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequestPut.PagoDTORequestPut;
import soda_roja.backend.service.PagoService;
import soda_roja.backend.dtoRequest.PagoDTORequest;
import soda_roja.backend.dtoResponse.PagoDTOResponse;
import jakarta.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/pago")
public class PagoController {

    @Autowired
    private PagoService service;

    @GetMapping
    public ResponseEntity<List<PagoDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll(populate));
    }

    @GetMapping("/ingressos")
    public ResponseEntity<Float> getIngresos(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getIngresos(populate));
    }


    @GetMapping("/{id}")
    public ResponseEntity<PagoDTOResponse> getById(
            @PathVariable Long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getById(id,populate));
    }
    @GetMapping("/me")
    public ResponseEntity<List<PagoDTOResponse>> getByUserId(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getByUserId(userId, populate));
    }


    @PostMapping
    public ResponseEntity<PagoDTOResponse> create(
            @Valid
            @RequestBody PagoDTORequest entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad,populate));
    }


    @PutMapping("/{id}")
    public ResponseEntity<PagoDTOResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody PagoDTORequestPut entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.update(id, entidad,populate));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
