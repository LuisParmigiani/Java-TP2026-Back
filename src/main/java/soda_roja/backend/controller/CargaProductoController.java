package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.service.CargaProductoService;
import soda_roja.backend.service.ProductoService;
import soda_roja.backend.dtoRequest.CargaProductoDTORequest;
import soda_roja.backend.dtoResponse.CargaProductoDTOResponse;
import soda_roja.backend.dtoResponse.ZonaDTOResponse;
import soda_roja.backend.model.CargaProducto;
import soda_roja.backend.model.Zona;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cargaProducto")


public class CargaProductoController {
    @Autowired
    private CargaProductoService service;

    // GET - traer todos
    @GetMapping
    public ResponseEntity<List<CargaProductoDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET - traer por ID
    @GetMapping("/{id}")
    public ResponseEntity<CargaProductoDTOResponse> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // POST - crear nuevo
    @PostMapping
    public ResponseEntity<CargaProductoDTOResponse> create(@Valid @RequestBody CargaProductoDTORequest entidad) {
        return ResponseEntity.ok(service.save(entidad));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<CargaProductoDTOResponse> update(@PathVariable long id, @Valid @RequestBody CargaProductoDTORequest entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}
