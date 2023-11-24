package com.cc.security.security;

import com.cc.security.logic.JwtGenerator;
import com.cc.security.repository.UserRepository;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(RsaKeyProperties.class)
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) //TODO important for method security to be enabled
public class SecurityConfiguration {

    // configuration
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtGenerator jwtGenerator) throws Exception {
        System.out.println("SECURITY: securityFilterChain executed");
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/messages/public", "/users", "/login/**").permitAll()
                        .requestMatchers("/execution/admin").hasAuthority("ROLE_ADMIN")//ROLE_ADMIN is correct with Basic Auth //TODO one way to do it
                        .requestMatchers("/execution/user").hasAuthority("ROLE_USER")  //ROLE_USER is correct with Basic Auth //TODO one way to do it
                        .anyRequest().authenticated())
                //.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)//TODO this is the Filter which filters the requests
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter(jwtGenerator))))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    // configuration allowance
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        System.out.println("SECURITY: corsConfigurationSource executed");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // convert roles data to GrantedAuthorities for spring
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(JwtGenerator jwtGenerator) {
        System.out.println("SECURITY: jwtAuthenticationConverter executed");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> jwtGenerator.extractAuthoritiesFromToken(jwt));
        return converter;
    }

    // used by spring - when logging in. We need to map it to a UserDetail (in our case UserPrincipal)
    @Bean
    UserDetailsService userDetailsService(UserRepository userRepository){
        System.out.println("SECURITY: userDetailService executed");
        return username -> userRepository.findByUsername(username)
                .map(user -> new UserPrincipal(user))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    // used in populate user when creating a user in the DB with a password
    @Bean
    PasswordEncoder passwordEncoder(){
        System.out.println("SECURITY: passwordEncoder executed");
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // used by Spring - decoding the token and checking if it is valid
    @Bean
    JwtDecoder jwtDecoder(RsaKeyProperties properties) {
        System.out.println("SECURITY: jwtDecoder executed");
        return NimbusJwtDecoder.withPublicKey(properties.publicKey()).build();
    }

    // used by me - generating the token when logging in
    @Bean
    JwtEncoder jwtEncoder(RsaKeyProperties properties) {
        System.out.println("SECURITY: jwtEncoder executed");
        JWK jwk = new RSAKey.Builder(properties.publicKey()).privateKey(properties.privateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }
}
