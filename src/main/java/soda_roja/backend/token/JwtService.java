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

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) //extrae el id
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractUsername(String token) { // Extrae el usuario (subject) del token
        return extractClaim(token, Claims::getSubject); // Usa extractClaim con Claims::getSubject
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { // Extrae un claim específico usando una función
        final Claims claims = Jwts.parserBuilder() // Crea un parser de JWT
                .setSigningKey(getSigningKey()) // Establece la clave de firma
                .build() // Construye el parser
                .parseClaimsJws(token) // Parsea el token JWT y valida firma
                .getBody(); // Obtiene el cuerpo (claims)
        return claimsResolver.apply(claims); // Aplica la función para extraer el claim deseado
    }

    private Key getSigningKey() { // Obtiene la clave de firma a partir del secret
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Decodifica el secret en base64
        return Keys.hmacShaKeyFor(keyBytes); // Genera la clave HMAC-SHA a partir de los bytes
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token); // Valida firma y vencimiento automáticamente
            return true; 
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Retorna false si el token expiró, fue adulterado o está vacío
        }
    }
}