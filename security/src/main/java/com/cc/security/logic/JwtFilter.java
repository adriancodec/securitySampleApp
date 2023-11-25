package com.cc.security.logic;

import com.cc.security.security.RsaKeyProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.cc.security.security.SecurityConfiguration.PUBLIC_URLS;

public class JwtFilter extends OncePerRequestFilter {

    //TODO to use this class, you need to add into securityFilterChain method in the SecurityConfiguration class.
    //.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)

    private final RsaKeyProperties rsaKeyProperties;

    public JwtFilter(RsaKeyProperties rsaKeyProperties) {
        this.rsaKeyProperties = rsaKeyProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        // Check if the request is for the login page
        if(isPublicUrl(request.getRequestURI())){
            // Allow access to the pages without token verification
            filterChain.doFilter(request, response);
            return;
        }

        if(token==null){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Missing Token");
            return;
        }

        if(!token.startsWith("Bearer ")){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Incorrect 'Bearer' prefix before the Token");
            return;
        }

        System.out.println(token);

        try {
            Claims tokenBody = Jwts.parser()
                    .setSigningKey(rsaKeyProperties.publicKey())
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();



            String username = tokenBody.getSubject();




            if (username != null) {
                System.out.println("USER STRING FROM TOKEN: " + username);
                //you have the username, so you can get the user from DB and set authorities for UsernamePasswordAuthenticationToken.
                //Spring already parsed your token for authorities and decided what you can see and what you cannot see
                //This part is for you to continue handling anything else you want to customize.
                //TODO you can customize your Authentication class here
                //if you want changes to take effect, you need to comment out this line in SecurityConfiguration class(this also sets the Authentication)
                //.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter(jwtGenerator))))

                //TODO code to implement custom Authentication class
                /*String[] rolesArray = ((String) tokenBody.get("roles")).split(",");
                // Convert the array to a List of GrantedAuthority
                List<GrantedAuthority> authorities = Arrays.stream(rolesArray)
                        .map(role -> new SimpleGrantedAuthority(role))
                        .collect(Collectors.toList());
                //System.out.println("USER AUTHORITIES FROM TOKEN: " + authorities);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);*/
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Invalid Token");
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);

        filterChain.doFilter(request, response);
    }

    private boolean isPublicUrl(String requestURI) {
        return PUBLIC_URLS.stream().anyMatch(publicUrl -> requestURI.endsWith(publicUrl));
    }
}

