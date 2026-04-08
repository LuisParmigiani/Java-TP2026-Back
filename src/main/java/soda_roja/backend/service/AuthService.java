package soda_roja.backend.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import soda_roja.backend.dtoRequest.LoginDTORequest;
import soda_roja.backend.model.Usuario;
import soda_roja.backend.token.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public String login(LoginDTORequest request) {
        Usuario usuario = usuarioService.getByEmail(request.getEmail());
        
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        
        if (!usuarioService.verifyPassword(request.getPassword(), usuario.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        UserDetails userDetails = User.builder()
                .username(usuario.getEmail())
                .password(usuario.getContrasena())
                .authorities(new SimpleGrantedAuthority("ROLE_" + usuario.getNivelAcceso()))
                .build();

        return jwtService.generateToken(userDetails, usuario.getId());
    }
}
