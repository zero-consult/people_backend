package org.zero_consult.people_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.zero_consult.people_backend.configuration.CustomProperties;
import org.zero_consult.people_backend.entities.AppUser;

import javax.crypto.SecretKey;
import javax.naming.AuthenticationException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private final CustomProperties customProperties;

    private SecretKey secretKey;

    private final long accessTokenValidity = 60 * 60 * 1000;

    private final JwtParser jwtParser;

    public final static String TOKEN_HEADER = "Authorization";
    public final static String TOKEN_PREFIX = "Bearer ";

    public JwtUtil(CustomProperties customProperties) {
        this.customProperties = customProperties;
        this.jwtParser = Jwts.parser().verifyWith(getSecretKey()).build();
    }

    private SecretKey getSecretKey() {
        if(secretKey == null) {
            secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(customProperties.getJwtSecretKey()));
        }
        return secretKey;
    }

    public String createToken(AppUser appUser) {
        Claims claims = Jwts.claims().subject(appUser.getEmployee().getEmail())
                .add("id", appUser.getId())
                .add("user", appUser.getEmployee().getEmail())
                .add("firstname", appUser.getEmployee().getFirstName())
                .add("lastname", appUser.getEmployee().getLastName())
                .add("password", appUser.getPassword())
                .add("admin", appUser.isAdmin())
                .build();
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));

        return Jwts.builder()
                .claims(claims)
                .expiration(tokenValidity)
                .signWith(secretKey)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }
}
