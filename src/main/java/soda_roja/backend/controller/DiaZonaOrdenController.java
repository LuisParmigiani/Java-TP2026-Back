package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.DiaZonaOrdenDTORequest;
import soda_roja.backend.dtoRequestPut.DiaZonaOrdenDTORequestPut;
import soda_roja.backend.dtoResponse.DiaZonaOrdenDTOResponse;
import soda_roja.backend.service.DiaZonaOrdenService;
import jakarta.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/dia-zona-orden")
public class DiaZonaOrdenController {

    @Autowired
    private DiaZonaOrdenService service;

    @GetMapping
    public ResponseEntity<List<DiaZonaOrdenDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll(populate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiaZonaOrdenDTOResponse> getById(
            @PathVariable Long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getById(id, populate));
    }

    @PostMapping
    public ResponseEntity<DiaZonaOrdenDTOResponse> create(
            @Valid
            @RequestBody DiaZonaOrdenDTORequest entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad, populate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaZonaOrdenDTOResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody DiaZonaOrdenDTORequestPut entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.update(id, entidad, populate));
    }

    @PutMapping("/multiple")
    public ResponseEntity<List<DiaZonaOrdenDTOResponse>> updateMultiple(
            @Valid
            @RequestBody List<DiaZonaOrdenDTORequestPut> entidades,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.updateMultiple(entidades, populate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
