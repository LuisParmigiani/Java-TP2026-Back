package soda_roja.backend.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.model.Venta;
import soda_roja.backend.service.VentaService;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/venta")
public class VentaController {

    @Autowired
    private VentaService service;

    // GET - traer todos
    @GetMapping
    public ResponseEntity<List<Venta>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET - traer por ID
    @GetMapping("/{id}")
    public ResponseEntity<Venta> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // POST - crear nuevo
    @PostMapping
    public ResponseEntity<Venta> create(@Valid @RequestBody Venta entidad) {
        return ResponseEntity.ok(service.save(entidad));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Venta> update(@PathVariable long id, @Valid @RequestBody Venta entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
