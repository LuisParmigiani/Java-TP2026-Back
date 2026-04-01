package soda_roja.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.service.PagoService;
import soda_roja.backend.model.Pago;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pago")

public class PagoController {

    @Autowired
    private PagoService service;

    // GET - traer todos
    @GetMapping
    public ResponseEntity<List<Pago>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET - traer por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pago> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // POST - crear nuevo
    @PostMapping
    public ResponseEntity<Pago> create(@Valid @RequestBody Pago entidad) {
        return ResponseEntity.ok(service.save(entidad));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Pago> update(@PathVariable long id, @Valid @RequestBody Pago entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
