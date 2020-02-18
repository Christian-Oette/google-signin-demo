package de.oette.google.signin.demo.google.signin.demo;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.ArrayList;

public class GoogleAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;

    public GoogleAuthenticationToken(String token) {
        super(new ArrayList<>());
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
