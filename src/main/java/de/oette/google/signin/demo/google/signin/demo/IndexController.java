package de.oette.google.signin.demo.google.signin.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    private final String clientId;
    private final AuthenticationManager authenticationManager;

    public IndexController(@Value("${clientId}") String clientId, AuthenticationManager authenticationManager) {
        this.clientId = clientId;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView model = new ModelAndView("index");
        model.addObject("clientId", clientId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null) {
            model.addObject("principal", authentication.getPrincipal());
        }
        return model;
    }

    @GetMapping("/auth/{token}")
    public RedirectView auth(@PathVariable(value = "token") String token, HttpServletRequest request)  {
        authenticationManager.authenticate(new GoogleAuthenticationToken(token));
        SecurityContext context = SecurityContextHolder.getContext();
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
        return new RedirectView("/");
    }

    @GetMapping("/secret-information")
    @ResponseBody
    public String getSecretInformation() {
        return "You are now logged in via google token. Next, you should sign up to this application.";
    }
}
