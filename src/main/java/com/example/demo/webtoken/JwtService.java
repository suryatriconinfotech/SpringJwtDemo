package com.example.demo.webtoken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@Service
public class JwtService {
    public static final String SECRET="9C37FBE9CA28B50779A49A5B05183F5A2E3B8E1D7AA800B338D71B19D0DBD62E";
    public static final Long VALIDITY= TimeUnit.MINUTES.toMillis(30);
    public String generateToken(UserDetails userDetails){
        Map<String,String> claims=new HashMap<>();
        claims.put("name","username");
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey() {
       byte[] decoded= Base64.getDecoder().decode(SECRET);
       return Keys.hmacShaKeyFor(decoded);
    }

    public String extractUsername(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getSubject();
    }

    private Claims getClaims(String jwt) {
        return Jwts.parser()
                 .verifyWith(generateKey())
                 .build()
                 .parseSignedClaims(jwt)
                 .getPayload();

    }

    public boolean isTokenValid(String jwt) {
        Claims claims=getClaims(jwt);
        return claims.getExpiration()
                .after(Date.from(Instant.now()));
    }
}
