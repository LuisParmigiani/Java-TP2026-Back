package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.DiaZonaDTORequest;
import soda_roja.backend.dtoRequest.DiaZonaDTORequestWithOrdenes;
import soda_roja.backend.dtoRequestPut.DiaZonaDTORequestPut;
import soda_roja.backend.dtoResponse.DiaZonaDTOResponse;
import soda_roja.backend.service.DiaZonaService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/dia-zona")
public class DiaZonaController {

	@Autowired
    private DiaZonaService service;

    @GetMapping
    public ResponseEntity<List<DiaZonaDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll(populate));
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<DiaZonaDTOResponse> getById(
            @PathVariable Long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getById(id, populate));
    }

    @GetMapping("/zona/{zonaId}/dia/{diaId}")
    public ResponseEntity<List<DiaZonaDTOResponse>> getByZonaIdAndDiaId(
            @PathVariable Long zonaId,
            @PathVariable Long diaId,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getByZonaIdAndDiaId(zonaId, diaId, populate));
    }

    @GetMapping("/camion/{camionId}/dia/{diaId}")
    public ResponseEntity<List<DiaZonaDTOResponse>> getByCamionIdAndDiaId(
            @PathVariable Long camionId,
            @PathVariable Long diaId,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getByCamionIdAndDiaId(camionId, diaId, populate));
    }

    @GetMapping("/camion/me/dia/{dia}")
    public ResponseEntity<List<DiaZonaDTOResponse>> getByMeAndDiaId(
            @AuthenticationPrincipal String userId,
            @PathVariable String dia,
            @RequestParam(required = false) String zona,
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String venta,
            @RequestParam(required = false) String[] populate) {
        System.out.println("User ID: " + userId);
        System.out.println("Dia: " + dia);
        System.out.println("Zona: " + zona);
        System.out.println("Direccion: " + direccion);
        System.out.println("Venta: " + venta);
        System.out.println("Zona: " + zona);
        return ResponseEntity.ok(service.getByMeAndDiaId(Long.parseLong(userId), dia, populate, zona, direccion, venta));
    }
    @PostMapping
    public ResponseEntity<DiaZonaDTOResponse> create(
            @Valid
            @RequestBody DiaZonaDTORequest entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad, populate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaZonaDTOResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody DiaZonaDTORequestPut entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.update(id, entidad, populate));
    }
    
    @PutMapping("/{id}/ordenes")
    public ResponseEntity<DiaZonaDTOResponse> updateWithOrdenes(
            @PathVariable Long id,
            @RequestBody DiaZonaDTORequestWithOrdenes entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.updateWithOrdenes(id, entidad, populate));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

