package com.cc.security.logic;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtGenerator {

    private final JwtEncoder jwtEncoder;

    public static final String DELIMITER = ",";
    public static final String KEY_VALUE = "roles";

    public JwtGenerator(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateJwtToken(Authentication authentication) {
        Instant now = Instant.now();

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(DELIMITER)); // Comma-separated roles

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("sekf")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim(KEY_VALUE, roles)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Collection<GrantedAuthority> extractAuthoritiesFromToken(Jwt jwt) {
        // Extract authorities from the JWT claims or other sources
        // Customize this method based on your JWT structure
        Map<String, Object> claims = jwt.getClaims();
        System.out.println(claims.toString());

        String claimAuthority = (String) claims.get(KEY_VALUE);
        System.out.println(claimAuthority);

        // Convert String authorities to GrantedAuthority instances
        Collection<GrantedAuthority> authorities = Arrays.stream(claimAuthority.split(DELIMITER))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        System.out.println("Extracted Authorities from JWT Token: " + authorities);
        return authorities;
    }

}
