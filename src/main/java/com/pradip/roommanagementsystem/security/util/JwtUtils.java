package com.pradip.roommanagementsystem.security.util;

import java.util.*;
import java.util.function.Function;

import com.pradip.roommanagementsystem.security.dto.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.app.jwtSecret}")
    private String jwtSecret;

    @Value("${jwt.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    public String generateToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",userDetails.getId());
        claims.put("fullname",userDetails.getFullName());
        claims.put("roles",userDetails.getAuthorities());
        claims.put("enabled",userDetails.isEnabled());
        claims.put("locked",userDetails.isLocked());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        if (!authToken.startsWith("Bearer "))
            throw new JwtException("JWT Token does not begin with Bearer String");
        authToken=authToken.substring(7);
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            throw new JwtException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT token is expired");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT claims string is empty");
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        return true;
//        CustomUserDetails jwtUserDetails = (CustomUserDetails) userDetails;
//        final String username = getUsernameFromToken(token);
//        final String userLevel = getClaimFromToken(token, claims -> claims.get(JWT_USER_LEVEL)).toString();
//        final String country = getClaimFromToken(token, claims -> claims.get(JWT_COUNTRY)).toString();
//        final String role = getClaimFromToken(token, claims -> claims.get(JWT_ROLE)).toString();
//        final boolean active = Boolean.parseBoolean(getClaimFromToken(token, claims -> claims.get(JWT_ACTIVE)).toString());
//        if(!userLevel.equals(jwtUserDetails.getUserLevel()) || !country.equals(jwtUserDetails.getCountry())
//                || !role.equals(jwtUserDetails.getRole()) || active!=jwtUserDetails.isActive()){
//            throw new AccessDeniedException("Your permissions have been changed. Please log in again.");
//        }
//        return (username.equals(jwtUserDetails.getUsername()) && !isTokenExpired(token));
    }
}
