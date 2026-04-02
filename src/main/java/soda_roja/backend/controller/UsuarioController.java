package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.UsuarioDTORequest;
import soda_roja.backend.dtoResponse.UsuarioDTOResponse;
import soda_roja.backend.service.UsuarioService;
import soda_roja.backend.model.Usuario;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuario")

public class UsuarioController {

    @Autowired
    private UsuarioService service;

    // GET - traer todos
    @GetMapping
    public ResponseEntity<List<UsuarioDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET - traer por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTOResponse> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // POST - crear nuevo
    @PostMapping
    public ResponseEntity<UsuarioDTOResponse> create(@Valid @RequestBody UsuarioDTORequest entidad) {
        return ResponseEntity.ok(service.save(entidad));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTOResponse> update(@PathVariable long id, @Valid @RequestBody UsuarioDTORequest entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
