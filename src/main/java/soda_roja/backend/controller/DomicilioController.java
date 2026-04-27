package soda_roja.backend.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequestPut.DomicilioDTORequestPut;
import soda_roja.backend.service.DomicilioService;
import soda_roja.backend.dtoRequest.DomicilioDTORequest;
import soda_roja.backend.dtoResponse.DomicilioDTOResponse;
import jakarta.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("/api/domicilio")
public class DomicilioController {

    @Autowired
    private DomicilioService service;

    @GetMapping
    public ResponseEntity<List<DomicilioDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll(populate));
    }


    @GetMapping("/{id}")
    public ResponseEntity<DomicilioDTOResponse> getById(
            @PathVariable Long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getById(id,populate));
    }


    @GetMapping("/token/usuario")
    public ResponseEntity<Page<DomicilioDTOResponse>> getByUserId(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer dias,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String nameSearch,

            @RequestParam(required = false) String enabledStatus,
            @RequestParam(required = false) String[] populate,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size

    ){
        return ResponseEntity.ok(service.getByUserId(Long.parseLong(userId),orderBy,nameSearch,enabledStatus, estado, dias,populate, page, size));
    }


    @PostMapping
    public ResponseEntity<DomicilioDTOResponse> create(
            @AuthenticationPrincipal String userId,
            @Valid
            @RequestBody DomicilioDTORequest entidad,
            @RequestParam(required = false) String[] populate) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad,userId,populate));
    }


    @PutMapping("/{id}")
    public ResponseEntity<DomicilioDTOResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody DomicilioDTORequestPut entidad,
            @RequestParam(required = false) String[] populate) {

        return ResponseEntity.ok(service.update(id, entidad,populate));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
