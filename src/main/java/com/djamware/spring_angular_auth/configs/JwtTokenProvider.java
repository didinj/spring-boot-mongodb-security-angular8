package com.djamware.spring_angular_auth.configs;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.djamware.spring_angular_auth.models.Role;
import com.djamware.spring_angular_auth.services.CustomUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secret;

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds;

    private SecretKey secretKey;

    private JwtParser jwtParser;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        // Encode and build a proper HMAC SecretKey
        byte[] keyBytes = Base64.getEncoder().encode(secret.getBytes());
        secretKey = Keys.hmacShaKeyFor(keyBytes);

        jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .build();
    }

    public String createToken(String username, Set<Role> roles) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMilliseconds);

        Claims claims = (Claims) Jwts.claims().subject(username);
        claims.put("roles", roles);

        // signWith(Key) is now preferred and auto-selects algorithm
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getEncoded());
        Claims claims = ((JwtParserBuilder) Jwts.builder()
                .signWith(key))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject(); // Returns username
    }

    public String resolveToken(HttpServletRequest req) {
        String auth = req.getHeader("Authorization");
        return (auth != null && auth.startsWith("Bearer ")) ? auth.substring(7) : null;
    }

    public boolean validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getEncoded());

            var claims = ((JwtParserBuilder) Jwts
                    .builder()
                    .signWith(key))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return !claims.getExpiration().before(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Expired or invalid JWT token", e);
        }
    }
}
