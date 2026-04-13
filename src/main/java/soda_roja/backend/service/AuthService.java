package soda_roja.backend.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import soda_roja.backend.dtoRequest.LoginDTORequest;
import soda_roja.backend.dtoRequest.PersonaDTORequest;
import soda_roja.backend.dtoRequest.RegisterDTORequest;
import soda_roja.backend.dtoRequest.UsuarioDTORequest;
import soda_roja.backend.dtoResponse.LoginDTOResponse;
import soda_roja.backend.dtoResponse.PersonaDTOResponse;
import soda_roja.backend.dtoResponse.RegisterDTOResponse;
import soda_roja.backend.model.Usuario;
import soda_roja.backend.token.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final PersonaService personaService;

    public LoginDTOResponse login(LoginDTORequest request) {
        Usuario usuario = usuarioService.getByEmail(request.getEmail());

        if (usuario == null || !usuarioService.verifyPassword(request.getContrasena(), usuario.getContrasena())) {
            return new LoginDTOResponse(false, null, null, null, "Credenciales inválidas");
        }

        UserDetails userDetails = User.builder()
                .username(usuario.getId().toString())
                .authorities(new SimpleGrantedAuthority(usuario.getNivelAcceso()))
                .build();

        String token = jwtService.generateToken(userDetails);

        return new LoginDTOResponse(true, token, usuario.getId(), usuario.getNivelAcceso(), null);
    }
    
    @Transactional
    public RegisterDTOResponse register(RegisterDTORequest request) {
		try {
    	if (usuarioService.getByEmail(request.getEmail()) != null) {
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
		PersonaDTOResponse nuevaPersonaDto = personaService.save(nuevaPersona);
		
		UsuarioDTORequest nuevoUsuario = UsuarioDTORequest.builder()
				 .nombreUsuario(request.getUsuario_nombre())
				 .email(request.getEmail())
				 .contrasena(request.getUsuario_contrasena())
				 .nivelAcceso("Usuario")
				 .personaId(nuevaPersonaDto.getId())
				 .build();
		usuarioService.save(nuevoUsuario);
		
		return new RegisterDTOResponse(true, null);
    } catch (Exception e) {
        return new RegisterDTOResponse(false, e.getMessage());
    }
	}
}
