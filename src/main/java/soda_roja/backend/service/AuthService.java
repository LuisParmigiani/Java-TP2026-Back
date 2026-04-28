package soda_roja.backend.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import soda_roja.backend.dtoRequest.LoginDTORequest;
import soda_roja.backend.dtoRequest.PersonaDTORequest;
import soda_roja.backend.dtoRequest.RegisterDTORequest;
import soda_roja.backend.dtoRequest.UsuarioDTORequest;
import soda_roja.backend.dtoResponse.LoginDTOResponse;
import soda_roja.backend.dtoResponse.PersonaDTOResponse;
import soda_roja.backend.dtoResponse.RegisterDTOResponse;
import soda_roja.backend.model.Usuario;
import soda_roja.backend.repository.UsuarioRepository;
import soda_roja.backend.token.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final PersonaService personaService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    // token -> {email, expiracion}
    private final Map<String, TokenResetData> tokensReset = new ConcurrentHashMap<>();
    @Autowired
    private MailService mailService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    
    // Clase interna para los datos del token
    @Data
    @AllArgsConstructor
    @Builder
    private static class TokenResetData {
        private final String email;
        private final LocalDateTime expiracion;

        public boolean estaExpirado() {
            return LocalDateTime.now().isAfter(expiracion);
        }
    }
    
    public LoginDTOResponse login(LoginDTORequest request) {
        Usuario usuario = usuarioService.getByEmail(request.getEmail(),null);

        if (usuario == null || !usuarioService.verifyPassword(request.getContrasena(), usuario.getContrasena())) {
            throw new RuntimeException( "Credenciales inválidas");
        }

        UserDetails userDetails = User.builder()
                .username(usuario.getId().toString())
                .authorities(new SimpleGrantedAuthority(usuario.getNivelAcceso()))
                .build();

        // Add your custom claims here
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", usuario.getId());
        claims.put("email", usuario.getEmail());
        claims.put("role", usuario.getNivelAcceso());
        // If 'name' is in Persona instead of Usuario, you might need to fetch Persona here
        claims.put("username", usuario.getNombreUsuario()); 

        // Pass the extra claims to the JwtService
        String token = jwtService.generateToken(claims, userDetails);

        return new LoginDTOResponse(true, token, null);
    }
    
    @Transactional
    public RegisterDTOResponse register(RegisterDTORequest request) {
		try {
    	if (usuarioService.getByEmail(request.getEmail(),null) != null) {
			return new RegisterDTOResponse(false, "El email ya está registrado");
        }
		PersonaDTORequest nuevaPersona = PersonaDTORequest.builder()
				.tipoDoc(request.getPersona_tipoDoc())
				.nroDocumento(request.getPersona_nroDoc())
				.nombre(request.getPersona_nombre())
				.apellido(request.getPersona_apellido())
				.email(request.getEmail())
				.telefono(request.getPersona_telefono())
				.saldo(0)
				.build();
		PersonaDTOResponse nuevaPersonaDto = personaService.save(nuevaPersona, null);
		
		UsuarioDTORequest nuevoUsuario = UsuarioDTORequest.builder()
				 .nombreUsuario(request.getUsuario_nombre())
				 .email(request.getEmail())
				 .contrasena(request.getUsuario_contrasena())
				 .nivelAcceso("Usuario")
				 .personaId(nuevaPersonaDto.getId())
				 .build();
		usuarioService.save(nuevoUsuario, null);
		
		return new RegisterDTOResponse(true, null);
    } catch (Exception e) {
        return new RegisterDTOResponse(false, e.getMessage());
    }
	}
    
    public boolean verifyToken(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			String jwt = token.substring(7);
			return jwtService.validateToken(jwt);
		}
		return false;
	}
    
    // Parte de Password Reset
    
    public void solicitarResetPassword(String email) {
        usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email no encontrado"));

        String token = UUID.randomUUID().toString();
        tokensReset.put(token, new TokenResetData(email, LocalDateTime.now().plusMinutes(30)));

        String link = "Tu token es: <b>" + token + "</b>";
        mailService.enviarMail(email, "Restablecer contraseña", 
                "<p>Ingresá este token en nuestra app</p><p>" + link + "</p>"
                + "<p><b>Expira en 30 minutos.</b></p>");
    }
    
    public void verificarResetToken(String token) {
        TokenResetData data = tokensReset.get(token);
        if (data == null || data.estaExpirado()) {
            tokensReset.remove(token);
            throw new RuntimeException("Token inválido o expirado");
        }
    }

    public void resetearPassword(String token, String nuevaPassword) {
        TokenResetData data = tokensReset.get(token);
        if (data == null || data.estaExpirado()) {
            tokensReset.remove(token);
            throw new RuntimeException("Token inválido o expirado");
        }

        Usuario usuario = usuarioRepository.findByEmail(data.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setContrasena(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);

        tokensReset.remove(token); // token de un solo uso
    }

    
}
