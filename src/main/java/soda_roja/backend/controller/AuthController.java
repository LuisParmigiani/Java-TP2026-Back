package soda_roja.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import soda_roja.backend.dtoRequest.LoginDTORequest;
import soda_roja.backend.dtoRequest.RegisterDTORequest;
import soda_roja.backend.model.Usuario;
import soda_roja.backend.service.AuthService;
import soda_roja.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDTORequest request) {
        try {
            String token = authService.login(request);
            Usuario usuario = usuarioService.getByEmail(request.getEmail());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("token", token);
            response.put("userId", usuario.getId());
            response.put("role", usuario.getNivelAcceso());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTORequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
