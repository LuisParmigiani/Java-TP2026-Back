package soda_roja.backend.token; // Package declaration

import org.springframework.context.annotation.Bean; // Marks a method as a Spring bean
import org.springframework.context.annotation.Configuration; // Marks class as a configuration class
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Enables method-level security
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Configures HTTP security
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Enables web security
import org.springframework.security.config.http.SessionCreationPolicy; // Session management policy // Password encoder interface
import org.springframework.security.web.SecurityFilterChain; // Security filter chain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Standard auth filter

import lombok.RequiredArgsConstructor; // Lombok: generates constructor for final fields

@Configuration // Marks this class as a Spring configuration
@EnableWebSecurity // Enables Spring Security web support
@EnableMethodSecurity // Enables method-level security annotations like @PreAuthorize
@RequiredArgsConstructor // Lombok: generates constructor for final fields
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter; // Custom JWT filter
    
    

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable()) // Disables CSRF (not needed for JWT)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No HTTP sessions
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**","/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/usuario").hasRole("Administrador")
                .requestMatchers(HttpMethod.GET, "/api/usuario/**").permitAll()
               
              

                
                .anyRequest().permitAll()
            )
            // Add JWT filter before the standard username/password filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
