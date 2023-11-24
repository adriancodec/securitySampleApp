package com.cc.security.security;

import com.cc.security.data.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private final User user;

    public UserPrincipal(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("SECURITY PRINCIPAL: getAuthorities executed");
        System.out.println(String.join(", ", user.getAuthorities()) + "  (the ROLE_ prefix is added in next step)");
        return user.getAuthorities().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        System.out.println("SECURITY PRINCIPAL: getPassword executed");
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        System.out.println("SECURITY PRINCIPAL: getUsername executed");
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        System.out.println("SECURITY PRINCIPAL: isAccountNonExpired executed");
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        System.out.println("SECURITY PRINCIPAL: isAccountNonLocked executed");
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        System.out.println("SECURITY PRINCIPAL: isCredentialsNonExpired executed");
        return true;
    }

    @Override
    public boolean isEnabled() {
        System.out.println("SECURITY PRINCIPAL: getUsername executed");
        return true;
    }
}
