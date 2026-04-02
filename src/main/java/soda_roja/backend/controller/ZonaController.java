package soda_roja.backend.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.ZonaDTORequest;
import soda_roja.backend.dtoResponse.ZonaDTOResponse;
import soda_roja.backend.model.Zona;
import soda_roja.backend.service.ZonaService;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/zona")
public class ZonaController {

    @Autowired
    private ZonaService service;

    // GET - traer todos
    @GetMapping
    public ResponseEntity<List<ZonaDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET - traer por ID
    @GetMapping("/{id}")
    public ResponseEntity<ZonaDTOResponse> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // POST - crear nuevo
    @PostMapping
    public ResponseEntity<ZonaDTOResponse> create(@Valid @RequestBody ZonaDTORequest entidad) {
        return ResponseEntity.ok(service.save(entidad));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<ZonaDTOResponse> update(@PathVariable long id, @Valid @RequestBody ZonaDTORequest entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
