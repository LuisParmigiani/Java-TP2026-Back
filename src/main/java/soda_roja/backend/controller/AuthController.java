package soda_roja.backend.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import soda_roja.backend.dtoRequest.LoginDTORequest;
import soda_roja.backend.dtoRequest.RegisterDTORequest;
import soda_roja.backend.dtoResponse.LoginDTOResponse;
import soda_roja.backend.dtoResponse.RegisterDTOResponse;
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
        		   .body(new LoginDTOResponse(false, null, null, null, e.getMessage()));
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
}
