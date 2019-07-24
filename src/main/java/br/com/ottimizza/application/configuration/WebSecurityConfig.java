package br.com.ottimizza.application.configuration;

import br.com.ottimizza.application.service.impl.UserDetailsService;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity(debug = false)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        String[] resources = Arrays.asList(new String[] {
                "/assets/**", "/css/**", "/js/**", "/images/**"
        }).toArray(new String[] {});
        
        String[] allowed = Arrays.asList(new String[] {
                "/password_recovery*", 
                "/password_reset*", 
                "/register*", 
                "/user/password_recovery*", 
                "/user/password_reset*",
                "/maintenance",
                "/tokens"
        }).toArray(new String[] {});
        
        // http
        //     .authorizeRequests()
        //         .antMatchers("/api/**").authenticated();

        http
            .authorizeRequests()
                .antMatchers(resources).permitAll();

        http.authorizeRequests()
                .antMatchers(allowed).permitAll();
        
        http.authorizeRequests()
                .antMatchers("/oauth/revoke_token*").permitAll();

        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/user/**", "/api/**").permitAll() 
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()                                    
                .permitAll();
        //@formatter:on
    }

}