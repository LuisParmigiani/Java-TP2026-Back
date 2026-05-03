package soda_roja.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import soda_roja.backend.dtoRequest.LoginDTORequest;
import soda_roja.backend.dtoRequest.RegisterDTORequest;
import soda_roja.backend.dtoRequest.ResetPasswordDTORequest;
import soda_roja.backend.dtoRequest.SolicitarResetPasswordDTORequest;
import soda_roja.backend.dtoRequest.VerificarResetTokenDTORequest;
import soda_roja.backend.dtoResponse.LoginDTOResponse;
import soda_roja.backend.dtoResponse.RegisterDTOResponse;
import soda_roja.backend.dtoResponse.VerifyTokenDTOResponse;
import soda_roja.backend.service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<LoginDTOResponse> login(@RequestBody LoginDTORequest request) {
        try {
            LoginDTOResponse response = authService.login(request);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
           return ResponseEntity.status(401)
        		   .body(new LoginDTOResponse(false, null, e.getMessage()));
        }
    }

    
    @PostMapping("/register")
    public ResponseEntity<RegisterDTOResponse> register(@RequestBody RegisterDTORequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(new RegisterDTOResponse(false, e.getMessage()));
        }
    }
    
    @PostMapping("/verify-token")
    public ResponseEntity<VerifyTokenDTOResponse> verifyToken(@RequestHeader("Authorization") String token) {
        boolean isValid = authService.verifyToken(token);
        return ResponseEntity.ok(new VerifyTokenDTOResponse(isValid));
    }
    
    @PostMapping("/solicitar-reset-password")
    public ResponseEntity<String> solicitarResetPassword(@RequestBody SolicitarResetPasswordDTORequest body) {
        try {
            authService.solicitarResetPassword(body.getEmail());
            return ResponseEntity.ok("Mail enviado");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verificar-reset-token")
    public ResponseEntity<String> verificarToken(@RequestBody VerificarResetTokenDTORequest body) {
        try {
            authService.verificarResetToken(body.getToken());
            return ResponseEntity.ok("Token válido");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetearPassword(@RequestBody ResetPasswordDTORequest body) {
        try {
            authService.resetearPassword(body.getToken(), body.getNuevaPassword());
            return ResponseEntity.ok("Contraseña actualizada");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
