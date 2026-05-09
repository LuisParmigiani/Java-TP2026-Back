package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import soda_roja.backend.dtoRequest.UsuarioDTORequest;
import soda_roja.backend.dtoRequestPut.UsuarioDTORequestPut;
import soda_roja.backend.dtoResponse.UsuarioDTOResponse;
import soda_roja.backend.service.PersonaService;
import soda_roja.backend.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.List;


@RestController

@RequestMapping("/api/usuario")
public class UsuarioController {


    @Autowired
    private UsuarioService service;
    @Autowired
    private PersonaService servicePersona;


    @GetMapping
    public ResponseEntity<List<UsuarioDTOResponse>> getAll(
            @RequestParam(required = false) String[] populate) {
        return ResponseEntity.ok(service.getAll( populate));
    }
    
    @GetMapping("/empleados")
    public ResponseEntity<List<UsuarioDTOResponse>> getEmpleados(
    					@RequestParam(required = false) String[] populate,
    					@RequestParam(required = false) String nivelAcceso,
    					@RequestParam(required = false) String estado,
    					@RequestParam(required = false) String conCargas){
    	List<UsuarioDTOResponse> empleados = service.getByNivelAcceso(nivelAcceso, populate,estado,conCargas);
    	return ResponseEntity.ok(empleados);
    	
    }



    @GetMapping("/me")
    public ResponseEntity<?> getMe(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) String[] populate) {
        UsuarioDTOResponse usuario = service.getById(Long.parseLong(userId),populate);
        return ResponseEntity.ok((usuario));
    }
    @GetMapping("/driver")
    public ResponseEntity<?> getDriver(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) String[] populate) {
        UsuarioDTOResponse usuario = service.getEmpleado(Long.parseLong(userId),populate);
        return ResponseEntity.ok((usuario));
    }
    
    
    @PutMapping(value = "/updatePersona", consumes = {"multipart/form-data"})
    public ResponseEntity<UsuarioDTOResponse> update(
            @AuthenticationPrincipal String userId,
            @Valid @RequestPart("entidad") UsuarioDTORequestPut entidad,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam(required = false) String[] populate) {
    	UsuarioDTOResponse usuario= service.getById(Long.parseLong(userId), new String[0]);
    	servicePersona.update(usuario.getPersonaId(), entidad.getPersona() , new String[0]);
    	UsuarioDTOResponse usuarioActualizado= service.update(Long.parseLong(userId), entidad, file, populate);
    	
    	return ResponseEntity.ok(usuarioActualizado);
    }
    
    @PutMapping(value="/update/withPersona/{id}/{personaId}")
    public ResponseEntity<UsuarioDTOResponse> updateWithPersona(
			@PathVariable Long id,
			@PathVariable Long personaId,
			@Valid @RequestBody UsuarioDTORequestPut entidad,
			@RequestParam(required = false) String[] populate) {
		UsuarioDTOResponse usuarioActualizado= service.updateWithPersona(id,personaId, entidad,entidad.getPersona(), populate);
		
		return ResponseEntity.ok(usuarioActualizado);
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

//    @PutMapping("/{id}")
//    public ResponseEntity<UsuarioDTOResponse> updateById(
//            @PathVariable Long id,
//            @PathVariable Long personaId,
//            @Valid @RequestBody UsuarioDTORequestPut entidad,
//            @RequestParam(required = false) String[] populate) {
//        return ResponseEntity.ok(service.update(id,personaId, entidad,null, populate));
//    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
          ) {
    	System.out.println("El id recibido para eliminar es: " + id);
        service.logicDelete(id);
        return ResponseEntity.noContent().build();
    }
}
