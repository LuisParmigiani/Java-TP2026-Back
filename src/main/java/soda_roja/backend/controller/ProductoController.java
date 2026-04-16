package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequestPut.ProductoDTORequestPut;
import soda_roja.backend.service.ProductoService;
import soda_roja.backend.dtoRequest.ProductoDTORequest;
import soda_roja.backend.dtoResponse.ProductoDTOResponse;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/producto")
public class ProductoController {

    @Autowired
    private ProductoService service;

    @GetMapping
    public ResponseEntity<List<ProductoDTOResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTOResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/customer/active")
    public ResponseEntity<List<ProductoDTOResponse>> getActive(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String direction
    ) {

        return ResponseEntity.ok(service.getActive(userId, sort, search, minPrice, maxPrice,direction));
    }
    @GetMapping("/active")
    public ResponseEntity<List<ProductoDTOResponse>> getActive(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String direction
    ) {

        return ResponseEntity.ok(service.getActive(null, sort, search, minPrice, maxPrice,direction));
    }
    @PostMapping
    public ResponseEntity<ProductoDTOResponse> create(@Valid @RequestBody ProductoDTORequest entidad) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTOResponse> update(@PathVariable Long id, @Valid @RequestBody ProductoDTORequestPut entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
