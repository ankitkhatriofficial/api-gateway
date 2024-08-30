package org.khatri.sto.ambassador.service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.khatri.sto.ambassador.dto.security.AuthenticationBody;
import org.khatri.sto.ambassador.exceptions.ErrorCode;
import org.khatri.sto.ambassador.exceptions.WrongCredentialsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Ankit Khatri
 */

@Service
@Slf4j
public class JwtAuthenticationService implements AuthenticationService{

    @Value("${application.security.jwt.secret-key}") private String JWT_SECRET_KEY;

    @Value("${application.security.jwt.expiration}") private long JWT_EXPIRATION;

    @Value("${application.security.jwt.refresh-token.expiration}") private long JWT_REFRESH_TOKEN_EXPIRATION;

    @Override
    public String generateToken(final String subject) {
        Assert.notNull(subject, "subject can't be null for generating token..!");
        return this.createToken(new HashMap<>(), subject, this.JWT_EXPIRATION);
    }

    @Override
    public String generateRefreshToken(final String accessToken) {
        Assert.notNull(accessToken, "AccessToken can't be null for generating refresh token..!");
        return this.createToken(new HashMap<>(), accessToken, this.JWT_REFRESH_TOKEN_EXPIRATION);
    }

    @Override
    public AuthenticationBody validateToken(final String token, final String subject) {
        Assert.notNull(token, "Requested token can't be empty for validation..!");
        Assert.notNull(subject, "Subject can't be null for validating token..!");
        final String username = this.getSubject(token);
        final boolean expired = this.isExpired(token);
        final boolean isValidToken = !expired && StringUtils.hasText(username) && username.equals(subject);
        if(isValidToken){
            return AuthenticationBody.builder()
                    .isValid(Boolean.TRUE)
                    .username(username)
                    .expiration(getJwtExpirationDate(token))
                    .build();
        }
        return AuthenticationBody.builder().build();
    }

    @Override
    public String getSubject(final String token) {
        Assert.notNull(token, "Token can't ne empty for retrieving username..!");
        return this.getTokenClaim(token, Claims::getSubject);
    }

    @Override
    public String getSubjectEvenTokenExpired(final String token) {
        Assert.notNull(token, "Token can't ne empty for retrieving subject..!");
        return this.getSubjectEvenWhenTokenIsExpired(token);
    }

    private Date getJwtExpirationDate(String token) {
        return this.getTokenClaim(token, Claims::getExpiration);
    }

    private <T> T getTokenClaim(String token, Function<Claims, T> claimResolver) {
        return claimResolver.apply(this.getAllClaimsFromToken(token));
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, this.JWT_SECRET_KEY)
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        Jws<Claims> claims = null;
        try {
            claims = Jwts.parser().setSigningKey(this.JWT_SECRET_KEY).parseClaimsJws(token);
        } catch(Exception ex){
            log.info("[JwtAuthenticationService] Invalid token is passed:{}, ex:{}", token, ex.getStackTrace());
            throw new WrongCredentialsException(ErrorCode.INVALID_TOKEN);
        }
        return claims.getBody();
    }

    private String getSubjectEvenWhenTokenIsExpired(String token) {
        Jws<Claims> claims = null;
        try {
            claims = Jwts.parser().setSigningKey(this.JWT_SECRET_KEY).parseClaimsJws(token);
        } catch(ExpiredJwtException ex){
           return ex.getClaims().getSubject();
        } catch (Exception ex){
            log.info("[JwtAuthenticationService] Invalid token received:{}, ex:{}", token, ex.getStackTrace());
            throw new WrongCredentialsException(ErrorCode.INVALID_TOKEN);
        }
        return claims.getBody().getSubject();
    }

    private boolean isExpired(String token) {
        return this.getJwtExpirationDate(token).before(new Date());
    }
}
