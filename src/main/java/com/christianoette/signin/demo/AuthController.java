package com.christianoette.signin.demo;

import com.google.api.client.googleapis.auth.oauth2.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {

    private final GoogleIdTokenVerifier tokenVerifier;
    private final GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow;

    public AuthController(GoogleIdTokenVerifier tokenVerifier,
                          GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow) {
        this.tokenVerifier = tokenVerifier;
        this.googleAuthorizationCodeFlow = googleAuthorizationCodeFlow;
    }

    @GetMapping("/token/{token}")
    @ResponseBody
    public boolean authenticateByJavascriptClientToken(@PathVariable String token) {
        // TODO add token exchange logic here!
        try {
            return tokenVerifier.verify(token).getPayload().getEmailVerified();
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/redirect")
    public RedirectView redirectToGoogle() {
        GoogleAuthorizationCodeRequestUrl authUrl =
                googleAuthorizationCodeFlow.newAuthorizationUrl();
        authUrl.setRedirectUri("http://localhost:8080/auth/callback");
        String url = authUrl.build();
        return new RedirectView(url);
    }

    @GetMapping("/callback")
    public RedirectView authCallback(@RequestParam String code, HttpServletRequest servletRequest) throws IOException {
        GoogleAuthorizationCodeTokenRequest tokenRequest =
                googleAuthorizationCodeFlow.newTokenRequest(code);
        tokenRequest.setRedirectUri("http://localhost:8080/auth/callback");
        GoogleTokenResponse tokenResponse = tokenRequest.execute();

        // TODO replace session cookie with your your token exchange logic here
        setSessionCookie(servletRequest, tokenResponse);

        return new RedirectView("/");
    }

    private void setSessionCookie(HttpServletRequest request, GoogleTokenResponse tokenResponse) {

        // TODO Lookup credentials based on google token
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                "email", "credentials", new ArrayList<>());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    }
}
