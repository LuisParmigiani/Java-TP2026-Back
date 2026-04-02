package soda_roja.backend.controller;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.CargaDTORequest;
import soda_roja.backend.dtoResponse.CargaDTOResponse;
import soda_roja.backend.service.CargaService;
import soda_roja.backend.model.Carga;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/carga")
public class CargaController {

    @Autowired
    private CargaService service;

    // GET - traer todos
    @GetMapping
    public ResponseEntity<List<CargaDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET - traer por ID
    @GetMapping("/{id}")
    public ResponseEntity<CargaDTOResponse> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // POST - crear nuevo
    @PostMapping
    public ResponseEntity<CargaDTOResponse> create(@Valid @RequestBody CargaDTORequest entidad) {
        return ResponseEntity.ok(service.save(entidad));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<CargaDTOResponse> update(@PathVariable long id, @Valid @RequestBody CargaDTORequest entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}
