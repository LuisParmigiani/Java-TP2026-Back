package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.CreatVentaDriverDTORequest;
import soda_roja.backend.dtoRequest.ProductoDomicilioDTORequest;
import soda_roja.backend.dtoRequest.ProductoZonaDTORequest;
import soda_roja.backend.dtoRequest.VentaDTORequest;
import soda_roja.backend.dtoRequestPut.VentaDTORequestPut;
import soda_roja.backend.dtoResponse.VentaDTOResponse;
import soda_roja.backend.service.VentaService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/venta")
public class VentaController {

    @Autowired
    private VentaService service;

    @GetMapping
    public ResponseEntity<List<VentaDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll(populate));
    }

    @GetMapping("/{id}/{populate}")
    public ResponseEntity<VentaDTOResponse> getById(
            @PathVariable long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getById(id, populate));
    }
    @GetMapping("/pending")
    public ResponseEntity<Page<VentaDTOResponse>> getPendientes(
            @RequestParam(required = false) String[] populate,
            @RequestParam(required = false) String zona,
            @RequestParam(required = false, defaultValue = "Mas Recientes") String ordenBy,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(service.getPendientes(populate, zona, ordenBy, page, size));
    }


    @GetMapping("/token/ByUserId")
    public ResponseEntity<Page<VentaDTOResponse>> getByUserId(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) String[] populate,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String state,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        System.out.println("User ID from token: " + userId);

        Long id = Long.parseLong(userId);
        return ResponseEntity.ok(service.getByUserId(id, populate, orderBy, state, page, size));
    }


    @GetMapping("/ByUserId/{id}")
    public ResponseEntity<Page<VentaDTOResponse>> getByUserId(
            @PathVariable long id,
            @RequestParam(required = false) String[] populate,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {

        return ResponseEntity.ok(service.getByUserId(id, populate, null, null, page, size));
    }

    @PostMapping
    public ResponseEntity<VentaDTOResponse> create(
            @Valid @RequestBody VentaDTORequest entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad, populate));


    }

    @PostMapping("/driver")
    public ResponseEntity<VentaDTOResponse> createByDriver(
            @AuthenticationPrincipal String driverId,
            @Valid @RequestBody CreatVentaDriverDTORequest dto,
            @RequestParam(required = false) String monto,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveByDriver(dto.getVenta(), dto.getProductoDomicilio(), monto, populate,driverId));
    }


    @PutMapping("/{id}")
    public ResponseEntity<VentaDTOResponse> update(
            @PathVariable long id,
            @Valid @RequestBody VentaDTORequestPut entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.update(id, entidad, populate));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable long id)
         {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ventaHoy/{domicilioId}")
    public ResponseEntity<VentaDTOResponse> getVentasHoyByDomicilioId(
            @PathVariable long domicilioId,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getVentasHoyByDomicilioId(domicilioId, populate));
    }

}
