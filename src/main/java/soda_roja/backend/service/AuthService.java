package soda_roja.backend.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import soda_roja.backend.dtoRequest.LoginDTORequest;
import soda_roja.backend.dtoRequest.PersonaDTORequest;
import soda_roja.backend.dtoRequest.RegisterDTORequest;
import soda_roja.backend.dtoRequest.UsuarioDTORequest;
import soda_roja.backend.dtoResponse.PersonaDTOResponse;
import soda_roja.backend.model.Usuario;
import soda_roja.backend.token.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final PersonaService personaService;

    public String login(LoginDTORequest request) {
        Usuario usuario = usuarioService.getByEmail(request.getEmail());
        
        if (usuario == null || !usuarioService.verifyPassword(request.getContrasena(), usuario.getContrasena())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        

        UserDetails userDetails = User.builder()
                .username(usuario.getId().toString())
                .authorities(new SimpleGrantedAuthority(usuario.getNivelAcceso()))
                .build();

        return jwtService.generateToken(userDetails);
    }
    
    @Transactional
    public String register(RegisterDTORequest request) {
		if (usuarioService.getByEmail(request.getEmail()) != null) {
			throw new RuntimeException("El email ya está registrado");
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
		PersonaDTOResponse nuevaPersonaDto = personaService.save(nuevaPersona);
		
		UsuarioDTORequest nuevoUsuario = UsuarioDTORequest.builder()
				 .nombreUsuario(request.getUsuario_nombre())
				 .email(request.getEmail())
				 .contrasena(passwordEncoder.encode(request.getUsuario_contrasena()))
				 .nivelAcceso("Usuario")
				 .personaId(nuevaPersonaDto.getId())
				 .build();
		usuarioService.save(nuevoUsuario);
		
		return "Registro exitoso";
	}
}
