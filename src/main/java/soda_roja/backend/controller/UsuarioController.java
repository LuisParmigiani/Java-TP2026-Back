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
    public ResponseEntity<List<UsuarioDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll( populate));
    }



    @GetMapping("/me")
    public ResponseEntity<?> getMe(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) String[] populate) {
        UsuarioDTOResponse usuario = service.getById(Long.parseLong(userId),populate);
        return ResponseEntity.ok((usuario));
    }

    @PutMapping("/updatePersona")
    public ResponseEntity<?> updatePersona(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody UsuarioDTORequestPut entidad,
            @RequestParam(required = false) String[] populate) {
        System.out.println("Entidad: " + entidad);
        UsuarioDTOResponse updatedUsuario = service.update(Long.parseLong(userId), entidad, populate);
        return ResponseEntity.ok(updatedUsuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTOResponse> getById(
            @PathVariable Long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getById(id, populate));
    }


    @PostMapping
    public ResponseEntity<UsuarioDTOResponse> create(
            @Valid @RequestBody UsuarioDTORequest entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad, populate));
    }


    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTOResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTORequestPut entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.update(id, entidad, populate));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
          ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
