package com.tecn.tijuana.congresos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

  private final String secretKeyStr;
  private final SecretKey signingKey;


  public JwtService () {
    this.secretKeyStr = generateSecretKeyStr();
    this.signingKey = getSigningKey();
  }


  public String generateToken (String username) {
    Map<String, Object> claims = new HashMap<>();
    var                 now    = System.currentTimeMillis();
    return Jwts.builder()
      .claims(claims)
      .subject(username)
      .issuedAt(new Date(now))
      .expiration(new Date(now + 7 * 24 * 60 * 60 * 1000)) // 7 dias
      .signWith(signingKey)
      .compact();
  }


  private String generateSecretKeyStr () {
    KeyGenerator keyGenerator;
    try {
      keyGenerator = KeyGenerator.getInstance("HmacSHA256");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    SecretKey secretKey = keyGenerator.generateKey();
    return Base64.getEncoder().encodeToString(secretKey.getEncoded());
  }


  private SecretKey getSigningKey () {
    byte[] keyBytes = Decoders.BASE64.decode(secretKeyStr);
    return Keys.hmacShaKeyFor(keyBytes);
  }


  public String extractUsername (String token) {
    return extractClaim(token, Claims::getSubject);
  }


  private <T> T extractClaim (String token, Function<Claims, T> claimResolver) {
    final Claims claims = extractAllClaims(token);
    return claimResolver.apply(claims);
  }

  private Claims extractAllClaims (String token) {
    return Jwts.parser()
      .verifyWith(signingKey)
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }


  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }
}
