package hu.torma.deliveryapplication.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.logging.Logger;

@SessionScope
@Controller("loginController")
@Data
public class LoginController implements Serializable {


    Logger logger = Logger.getLogger("FOCH");

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        log.info(username + "set as username");
    }

    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private final AuthenticationManager authenticationManager;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    private UserDetailsService userDetailsService;

    Logger log = Logger.getLogger("LOGIN LOGGER");

    @PostConstruct
    public void init() {

    }

    public String login() {
        log.info("Login attempted " + username + " " + password);
        try {
            // Create an authentication token with the provided username and password
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);

            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Load user details to perform additional operations if needed
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Add custom logic if needed (e.g., redirect to a specific page)

            return "/storage.xhtml"; // Redirect to the home page after successful login
        } catch (AuthenticationException e) {
            // Handle authentication failure (e.g., display error message)
            return "/login.xhtml?error=true"; // Redirect back to the login page with an error parameter
        }
    }

}

