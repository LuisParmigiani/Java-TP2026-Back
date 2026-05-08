package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<List<ProductoDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll( populate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTOResponse> getById(
            @PathVariable Long id,
            @RequestParam(required = false) String[] populate)
    {
        return ResponseEntity.ok(service.getById(id, populate));
    }
    @GetMapping("/Zona/{zona}")
    public ResponseEntity<List<ProductoDTOResponse>> getByZona(
            @PathVariable Long zona,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getByZona(zona, populate));
    }

    @GetMapping("/customer/active")
    public ResponseEntity<Page<ProductoDTOResponse>> getActive(
            @AuthenticationPrincipal String userId
            , @RequestParam(required = false) String[] populate,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {

        return ResponseEntity.ok(service.getActive(userId, sort, search, minPrice, maxPrice,direction, populate, page, size));
    }
    @GetMapping("/active")
    public ResponseEntity<Page<ProductoDTOResponse>> getActive(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String[] populate,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {

        return ResponseEntity.ok(service.getActive(null, sort, search, minPrice, maxPrice,direction, populate,page,size));
    }
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProductoDTOResponse> create(
            @Valid @RequestPart("entidad") ProductoDTORequest entidad,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad, file, populate));
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ProductoDTOResponse> update(
            @PathVariable Long id,
            @Valid @RequestPart("entidad") ProductoDTORequestPut entidad,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.update(id, entidad, file, populate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
