package soda_roja.backend.token; 

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Auth token class
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder; // Holds security context
import org.springframework.security.core.userdetails.UserDetails; // User details interface
import org.springframework.security.core.userdetails.UserDetailsService; // Loads user data
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Builds auth details
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
                String email = jwtService.extractUsername(token);
                String rol = jwtService.extractClaim(token, claims -> claims.get("rol", String.class));
                Long userId = jwtService.extractClaim(token, claims -> claims.get("userId", Long.class));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol)));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException e) {
                // Token inválido
            }
        }

        filterChain.doFilter(request, response);
    }
}
