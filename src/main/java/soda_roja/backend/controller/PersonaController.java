package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import soda_roja.backend.model.Camion;
import soda_roja.backend.model.Dia;
import soda_roja.backend.model.Zona;
import soda_roja.backend.repository.CamionRepository;
import soda_roja.backend.repository.DiaRepository;
import soda_roja.backend.repository.ZonaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequestPut.PersonaDTORequestPut;
import soda_roja.backend.service.PersonaService;
import soda_roja.backend.dtoRequest.PersonaDTORequest;
import soda_roja.backend.dtoResponse.PersonaDTOResponse;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/persona")
public class PersonaController {

    @Autowired
    private PersonaService service;
    @Autowired
    private ZonaRepository zonaRepository;

    @Autowired
    private CamionRepository camionRepository;

    @Autowired
    private DiaRepository diaRepository;

    @GetMapping
    public ResponseEntity<Page<PersonaDTOResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll(page, size, populate));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PersonaDTOResponse>> getByNameAndFiltered(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long zonaId,
            @RequestParam(required = false) Long camionId,
            @RequestParam(required = false) Long diaId,
            @RequestParam(required = false, defaultValue = "ascendente") String ordenSaldo,
            @RequestParam(required = false) String nivelAcceso,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String[] populate) {
    		Zona zona = zonaId != null ? zonaRepository.findById(zonaId).orElse(null) : null;
    	    Camion camion = camionId != null ? camionRepository.findById(camionId).orElse(null) : null;
    	    Dia dia = diaId != null ? diaRepository.findById(diaId).orElse(null) : null;


        return ResponseEntity.ok(service.getByNameAndFiltered(query, zona, camion, dia, ordenSaldo, nivelAcceso ,page, size, populate));
    }
    @GetMapping("/{id}")
    public ResponseEntity<PersonaDTOResponse> getById(
            @PathVariable Long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getById(id,populate));
    }
    @GetMapping("/client/active")
    public ResponseEntity<List<PersonaDTOResponse>> getActiveClient(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getActiveClient(populate));
    }
    


    @PostMapping
    public ResponseEntity<PersonaDTOResponse> create(
            @Valid
            @RequestBody PersonaDTORequest entidad
            ,@RequestParam(required = false) String[] populate
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad,populate));
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<PersonaDTOResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody PersonaDTORequestPut entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.update(id, entidad,populate));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    //BAJA LOGICAAAAAAA
    @DeleteMapping("/{id}/disable")
    public ResponseEntity<Void> disablePersona(@PathVariable Long id) {
        service.logicDelete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/clientes")
    public ResponseEntity<Page<PersonaDTOResponse>> getAllClientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAllClientes(page, size, populate));
    }

}
