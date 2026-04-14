package soda_roja.backend.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Auth token class
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder; // Holds security context
import org.springframework.stereotype.Component; // Marks as Spring component
import org.springframework.web.filter.OncePerRequestFilter; // Ensures filter runs once per request

import io.jsonwebtoken.JwtException; // Exception for JWT errors
import java.io.IOException; // For IO exceptions
import java.util.Collections;

import jakarta.servlet.FilterChain; // For filter chain
import jakarta.servlet.ServletException; // For servlet exceptions
import jakarta.servlet.http.HttpServletRequest; // HTTP request
import jakarta.servlet.http.HttpServletResponse; // HTTP response
import lombok.RequiredArgsConstructor; // Generates constructor for final fields

@Component // Registers as a Spring bean
@RequiredArgsConstructor // Lombok: generates constructor for final fields
public class JwtAuthFilter extends OncePerRequestFilter { // Custom filter extending Spring's filter

    private final JwtService jwtService; // Service to handle JWT logic


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String id = jwtService.extractUsername(token); //al hacer el parsing dentro de este método con el secret, valida token y que no esté vencido
                //si llegase a estar vencido, el parsing tira una JwtException que se captura acá y no se setea nada en el contexto.
                String rol = jwtService.extractClaim(token, claims -> claims.get("rol", String.class));

                //Volvemos a lo mismo de stateless, no tenemos un usuario autenticado en memoria, pero para que Spring Security sepa que el token es válido
                //y qué rol tiene, creamos un objeto de autenticación con el id del usuario como principal y el rol como autoridad.
                //dejando la clave en null (este contenedor es estandar en Spring)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(id, null,
                                //crea una lista super eficiente y liviana INMUTABLE (pueden ser muchos roles en Spring pero nosotros solo 1 pusimos)
                                //que dentro tiene un objeto que usa la interfaz de Spring para roles (que simplemente es un String pero que cumple con la interfaz)
                                //el prefijo ROLE_ es un estandar de Spring para reconocer que es un rol, no es necesario pero es buena práctica
                                //si se lo sacamos, en vez de usar hasRole(Administrador) ponemos hasAuthority(Administrador) porque el prefijo ROLE_ ya no estaría.
                                Collections.singletonList(new SimpleGrantedAuthority(rol)));

                SecurityContextHolder.getContext().setAuthentication(authentication); //se crea y se muere con cada request.
                // el contexto consultado en cada request por Spring Security Config para validar el acceso a los endpoints
            } catch (JwtException e) {
                // Token inválido
            }
        }

        //si no hay token o es inválido, simplemente no se setea nada en el contexto de seguridad y se sigue con la cadena de filtros

        filterChain.doFilter(request, response);
    }
}
