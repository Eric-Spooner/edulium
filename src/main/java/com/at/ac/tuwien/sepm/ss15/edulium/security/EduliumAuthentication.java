package com.at.ac.tuwien.sepm.ss15.edulium.security;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;

/**
 * Authentication Decorator
 */
public class EduliumAuthentication implements Authentication {
    private final Authentication authentication;
    private final User loggedInUser;

    public EduliumAuthentication(Authentication authentication, User user) {
        this.authentication = authentication;
        loggedInUser = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authentication.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return authentication.getCredentials();
    }

    @Override
    public Object getDetails() {
        return authentication.getDetails();
    }

    @Override
    public Object getPrincipal() {
        return loggedInUser;
    }

    @Override
    public boolean isAuthenticated() {
        return authentication.isAuthenticated();
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        authentication.setAuthenticated(b);
    }

    @Override
    public String getName() {
        return authentication.getName();
    }

    @Override
    public boolean implies(Subject subject) {
        return authentication.implies(subject);
    }
}
