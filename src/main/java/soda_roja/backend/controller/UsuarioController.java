package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.UsuarioDTORequest;
import soda_roja.backend.dtoRequestPut.UsuarioDTORequestPut;
import soda_roja.backend.dtoResponse.UsuarioDTOResponse;
import soda_roja.backend.model.Usuario;
import soda_roja.backend.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public ResponseEntity<List<UsuarioDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }


    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal String userId) {
        UsuarioDTOResponse usuario = service.getById(Long.parseLong(userId));
        return ResponseEntity.ok((usuario));
    }


    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTOResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTOResponse> create(@Valid @RequestBody UsuarioDTORequest entidad) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTOResponse> update(@PathVariable Long id, @Valid @RequestBody UsuarioDTORequestPut entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
