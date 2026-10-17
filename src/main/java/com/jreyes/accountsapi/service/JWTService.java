package com.jreyes.accountsapi.service;

import com.jreyes.accountsapi.domain.User;
import io.jsonwebtoken.Claims;

import java.util.Map;
import java.util.function.Function;

public interface JWTService {
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generateToken(User user);
    String generateToken(Map<String, Object> extraClaims, User user);
    boolean isTokenValid(String token, User user);

}
