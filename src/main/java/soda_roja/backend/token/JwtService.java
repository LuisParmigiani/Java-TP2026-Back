package soda_roja.backend.token;

import io.jsonwebtoken.*; // Importa la librería JJWT principal
import io.jsonwebtoken.io.Decoders; // Para decodificar claves en base64
import io.jsonwebtoken.security.Keys; // Para generar claves seguras
import org.springframework.beans.factory.annotation.Value; // Para inyectar valores desde application.properties
import org.springframework.security.core.userdetails.UserDetails; // Interfaz de usuario de Spring Security
import org.springframework.stereotype.Service; // Marca la clase como un servicio de Spring

import java.security.Key; // Representa la clave de firma
import java.util.*; // Utilidades de Java (Map, Date, etc)
import java.util.function.Function; // Para funciones lambda


@Service // Indica que es un servicio de Spring
public class JwtService {

    @Value("${jwt.secret}") // Inyecta el valor de jwt.secret desde application.properties
    private String secretKey;

    @Value("${jwt.expiration}") // Inyecta el valor de jwt.expiration desde application.properties
    private long expirationMs;

    public String generateToken(UserDetails userDetails, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", ""));
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername()) // email
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean isTokenValid(String token, UserDetails userDetails) { // Verifica si el token es válido
        final String username = extractUsername(token); // Extrae el usuario del token
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token); // Compara usuario y expiración
    }

    public String extractUsername(String token) { // Extrae el usuario (subject) del token
        return extractClaim(token, Claims::getSubject); // Usa extractClaim con Claims::getSubject
    }

    private boolean isTokenExpired(String token) { // Verifica si el token está expirado
        return extractClaim(token, Claims::getExpiration).before(new Date()); // Compara la fecha de expiración
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { // Extrae un claim específico usando una función
        final Claims claims = Jwts.parserBuilder() // Crea un parser de JWT
                .setSigningKey(getSigningKey()) // Establece la clave de firma
                .build() // Construye el parser
                .parseClaimsJws(token) // Parsea el token JWT
                .getBody(); // Obtiene el cuerpo (claims)
        return claimsResolver.apply(claims); // Aplica la función para extraer el claim deseado
    }

    private Key getSigningKey() { // Obtiene la clave de firma a partir del secret
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Decodifica el secret en base64
        return Keys.hmacShaKeyFor(keyBytes); // Genera la clave HMAC-SHA a partir de los bytes
    }
}