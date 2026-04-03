package soda_roja.backend.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.GastoDTORequest;
import soda_roja.backend.dtoResponse.GastoDTOResponse;
import soda_roja.backend.model.Gasto;
import soda_roja.backend.service.GastoService;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/gasto")
public class GastoController {

    @Autowired
    private GastoService service;

    // GET - traer todos
    @GetMapping
    public ResponseEntity<List<GastoDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET - traer por ID
    @GetMapping("/{id}")
    public ResponseEntity<GastoDTOResponse> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // POST - crear nuevo
    @PostMapping
    public ResponseEntity<GastoDTOResponse> create(@Valid @RequestBody GastoDTORequest entidad) {
        return ResponseEntity.ok(service.save(entidad));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<GastoDTOResponse> update(@PathVariable long id, @Valid @RequestBody GastoDTORequest entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
