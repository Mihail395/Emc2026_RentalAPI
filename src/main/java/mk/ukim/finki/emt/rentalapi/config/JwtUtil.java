package mk.ukim.finki.emt.rentalapi.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Generate a signing key from the secret string
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generate a JWT token for a given user
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                // subject = who the token is about (username)
                .issuedAt(new Date())
                // when the token was created
                .expiration(new Date(System.currentTimeMillis() + expiration))
                // when the token expires (24 hours from now)
                .signWith(getSigningKey())
                // sign with our secret key
                .compact();
        // build the final token string
    }

    // Extract the username from a token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Check if the token is valid for a given user
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Check if the token has expired
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Parse the token and extract all claims
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                // verify the token was signed with our key
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}