package de.oette.google.signin.demo.google.signin.demo;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public WebSecurityConfiguration(GoogleIdTokenVerifier googleIdTokenVerifier) {
        this.googleIdTokenVerifier = googleIdTokenVerifier;
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(
                new AuthenticationProvider() {
                    @Override
                    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                        try {
                            GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(authentication.getCredentials().toString());
                            GoogleIdToken.Payload payload = googleIdToken.getPayload();
                            authentication.getCredentials();
                            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(payload.getEmail(), payload.getAccessTokenHash(), new ArrayList<>());
                            SecurityContext context = SecurityContextHolder.getContext();
                            context.setAuthentication(token);
                            return token;
                        } catch (Exception ex) {
                            throw new BadCredentialsException("failed", ex);
                        }
                    }

                    @Override
                    public boolean supports(Class<?> aClass) {
                        return aClass.equals(GoogleAuthenticationToken.class);
                    }
                }
        );
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/");

    }
}
