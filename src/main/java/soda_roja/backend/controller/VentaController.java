package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.VentaDTORequest;
import soda_roja.backend.dtoRequestPut.VentaDTORequestPut;
import soda_roja.backend.dtoResponse.VentaDTOResponse;
import soda_roja.backend.service.VentaService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/venta")
public class VentaController {

    @Autowired
    private VentaService service;

    @GetMapping
    public ResponseEntity<List<VentaDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll(populate));
    }

    @GetMapping("/{id}/{populate}")
    public ResponseEntity<VentaDTOResponse> getById(
            @PathVariable long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getById(id, populate));
    }

    @GetMapping("/token/ByUserId")
    public ResponseEntity<List<VentaDTOResponse>> getByUserId(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getByUserId(Long.parseLong(userId), populate));
    }


    @GetMapping("/ByUserId/{id}")
    public ResponseEntity<List<VentaDTOResponse>> getByUserId(
            @PathVariable long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getByUserId(id, populate));
    }

    @PostMapping
    public ResponseEntity<VentaDTOResponse> create(
            @Valid @RequestBody VentaDTORequest entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad, populate));
    }


    @PutMapping("/{id}")
    public ResponseEntity<VentaDTOResponse> update(
            @PathVariable long id,
            @Valid @RequestBody VentaDTORequestPut entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.update(id, entidad, populate));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable long id)
         {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
