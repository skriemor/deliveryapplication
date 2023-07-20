package hu.torma.deliveryapplication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;
    public SecurityConfig(@Lazy  PasswordEncoder pw){
        this.passwordEncoder = pw;

    }
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/javax.faces.resource/**", "/resources/**", "/login.xhtml", "/login").permitAll()                .anyRequest().authenticated() // Require authentication for other requests
                .and()
                .formLogin()
                .loginPage("/login.xhtml") // Set the login page URL
                .defaultSuccessUrl("/storage.xhtml") // Set the default URL after successful login
                .permitAll() // Allow access to the login page
                .and()
                .logout()
                .logoutSuccessUrl("/login.xhtml") // Set the URL to redirect after logout
                .permitAll() // Allow access to the logout URL
                .and()
                .headers()
                .frameOptions().sameOrigin(); // Allow rendering of content within an iframe (e.g., PrimeFaces dialogs)

        // Enable AJAX calls with Spring Security
        http
                .headers()
                .contentSecurityPolicy("script-src 'self' 'unsafe-inline'"); // Allow inline scripts for PrimeFaces AJAX
    }

}
