package soda_roja.backend.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequestPut.CamionDTORequestPut;
import soda_roja.backend.service.CamionService;
import soda_roja.backend.dtoRequest.CamionDTORequest;
import soda_roja.backend.dtoResponse.CamionDTOResponse;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/camion")
public class CamionController {


    @Autowired
    private CamionService service;

    // GET - traer todos

    @GetMapping
    public ResponseEntity<List<CamionDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
    	
        return ResponseEntity.ok(service.getAll(populate));
    }

    // GET - traer por ID
    @GetMapping("/{id}")
    public ResponseEntity<CamionDTOResponse> getById(
            @PathVariable long id,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getById(id,populate));
    }

    // POST - crear nuevo
    @PostMapping
    public ResponseEntity<CamionDTOResponse> create(
            @Valid @RequestBody CamionDTORequest entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad,populate));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<CamionDTOResponse> update(
            @PathVariable long id,
            @Valid  @RequestBody CamionDTORequestPut entidad,
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.update(id, entidad,populate));
    }
    // Patch - actualizar parcialmente

    @PatchMapping("/{id}/disable")
    public ResponseEntity<CamionDTOResponse> disable(
            @PathVariable long id,
            @RequestParam(required = false) String[] populate) {
    			return ResponseEntity.ok(service.disable(id,populate));
    			
    }

    
    // DELETE - eliminar

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable long id)
    {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
