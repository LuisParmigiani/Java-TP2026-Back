package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soda_roja.backend.dtoRequest.GastoDTORequest;
import soda_roja.backend.dtoResponse.GastoDTOResponse;
import soda_roja.backend.service.GastoService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/gasto")
public class GastoController {

	@Autowired
	private GastoService service;

	@GetMapping
	public ResponseEntity<List<GastoDTOResponse>> getAll() {
		return ResponseEntity.ok(service.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<GastoDTOResponse> getById(@PathVariable Long id) {
		return ResponseEntity.ok(service.getById(id));
	}

	@PostMapping
	public ResponseEntity<GastoDTOResponse> create(@Valid @RequestBody GastoDTORequest entidad) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entidad));
	}

	@PutMapping("/{id}")
	public ResponseEntity<GastoDTOResponse> update(@PathVariable Long id, @Valid @RequestBody GastoDTORequest entidad) {
		return ResponseEntity.ok(service.update(id, entidad));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
