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
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOriginPatterns(java.util.List.of("*"));
        configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(java.util.List.of("*"));
        configuration.setAllowCredentials(true);

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean //En Spring, un Bean es un objeto que es instanciado, configurado y administrado por el contenedor central
    //de Spring (conocido como IoC container o contexto de aplicación). No hace falta usar new manualmente.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
        	.cors(cors -> {})
            .csrf(csrf -> csrf.disable()) // Disables CSRF (not needed for JWT)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No HTTP sessions
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**","/swagger-ui.html").permitAll()
                //.requestMatchers(HttpMethod.GET, "/api/usuario").hasAuthority("Administrador") //si no hay contexto, devuelve 401
//                .requestMatchers(HttpMethod.GET, "/api/usuario/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/usuario/**").hasAuthority("Usuario") //descomentar si lo necesitan para algo
                 //descomentar si lo necesitan para algo

               /* SINTAXIS
                * .requestMatchers(método, "endpoint").
                * permitAll()
                * hasAuthority("Administrador/Usuario/Empleado") -> requiere autenticación y que el rol del token sea el indicado (si no hay token o el rol no coincide, devuelve 403)
                  authenticated()*/
                
              

                
                .anyRequest().permitAll() //todas las demás, permitidas
            )
            //Antes de lo de arriba, el jwtAuthFilter intercepta:
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) 
            //esto le dice, pone el filtro antes de la autenticación de Spring de usuario y clave
            //pero como nosotros tenemos una API Stateless que es el estandar
            //no tenemos ese tipo de autenticacón pero lo dejamos para guiar a Spring para que ponga este filtro primero
            //sino, no sabe donde poner el filtro y tira error.
            .build();
    }
}
