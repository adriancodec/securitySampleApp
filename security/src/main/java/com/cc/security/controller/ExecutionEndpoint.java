package com.cc.security.controller;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("execution")
public class ExecutionEndpoint {

    //For this Annotation to take effect you should annotate SecurityConfiguration class with @EnableGlobalMethodSecurity
    //@PreAuthorize("hasRole('ROLE_ADMIN')")    //WORKING
    //@Secured("ROLE_ADMIN")                    //WORKING
    //@RolesAllowed("ROLE_ADMIN")               //NOT WORKING
    @GetMapping("admin")
    String adminExecution(Authentication authentication) {
        System.out.println("ADMIN: Actual Anthentication:" + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("ADMIN: Admin request arrived, starting execution");
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        System.out.println("ADMIN: Authorities of arrived user: " + authentication.getName() + " are: " + authorities);
        System.out.println("ADMIN: SECURITY: Admin endpoint executed");
        return "ADMIN: Admin executed a function";
    }

    //For this Annotation to take effect you should annotate SecurityConfiguration class with @EnableGlobalMethodSecurity
    //@PreAuthorize("hasRole('ROLE_USER')")     //WORKING
    //@Secured("ROLE_USER")                     //WORKING
    //@RolesAllowed("ROLE_USER")                //NOT WORKING
    @GetMapping("user")
    String userExecution(Authentication authentication) {
        System.out.println("USER: Actual Anthentication:" + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("USER: User request arrived, starting execution");
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        System.out.println("USER: Authorities of my authenticated user: " + authorities);
        System.out.println("USER: SECURITY: User endpoint executed");
        return "USER: User executed a function";
    }
}
