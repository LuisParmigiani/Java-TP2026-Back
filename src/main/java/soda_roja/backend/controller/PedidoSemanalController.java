package soda_roja.backend.controller;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequestPut.PedidoSemanalDTORequestPut;
import soda_roja.backend.service.PedidoSemanalService;
import soda_roja.backend.dtoRequest.PedidoSemanalDTORequest;
import soda_roja.backend.dtoResponse.PedidoSemanalDTOResponse;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pedidoSemanal")
public class PedidoSemanalController {

    @Autowired
    private PedidoSemanalService service;
    @Autowired
    private PathMatcher pathMatcher;

    @GetMapping
    public ResponseEntity<List<PedidoSemanalDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll(populate));
    }


    @GetMapping("/{id}")
    public ResponseEntity<PedidoSemanalDTOResponse> getById(
            @PathVariable Long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getById(id,populate));
    }


    @PostMapping
    public ResponseEntity<PedidoSemanalDTOResponse> create(
            @Valid
            @RequestBody PedidoSemanalDTORequest entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad,populate));
    }

    @PostMapping("/createMany")
    public ResponseEntity<List<PedidoSemanalDTOResponse>> createMany(
            @Valid
            @RequestBody List<PedidoSemanalDTORequest> weeklyOrder,
            @PathParam("addressId") String addressId,
            @RequestParam(required = false) String[] populate) {

    return ResponseEntity.status(HttpStatus.CREATED).body(service.saveList(weeklyOrder,addressId,populate));
    }
    @PutMapping("/{id}")
    public ResponseEntity<PedidoSemanalDTOResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody PedidoSemanalDTORequestPut entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.update(id, entidad,populate));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
