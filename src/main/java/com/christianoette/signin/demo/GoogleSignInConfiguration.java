package com.christianoette.signin.demo;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class GoogleSignInConfiguration {

    private final String clientId;
    private final String clientSecret;

    public GoogleSignInConfiguration(@Value("${clientId}") String clientId,
                                     @Value("${clientSecret}") String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Bean
    public GoogleIdTokenVerifier tokenVerifier() {
        HttpTransport transport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        return new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();

    }

    @Bean
    public GoogleAuthorizationCodeFlow authorizationCodeFlow() {
        HttpTransport transport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        return new GoogleAuthorizationCodeFlow.Builder(
                transport, jsonFactory, clientId, clientSecret,
                Arrays.asList("profile", "email")
        ).build();
    }
}
