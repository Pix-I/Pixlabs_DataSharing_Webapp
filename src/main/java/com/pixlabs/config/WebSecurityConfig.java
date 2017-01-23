package com.pixlabs.config;

import com.pixlabs.data.dao.UserRepository;
import com.pixlabs.security.AuthenticationProviderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.inject.Inject;

/**
 * Created by pix-i on 01/01/2017.
 * ${Copyright}
 */

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private String[] publicStuff = {"/index","/","/css/**","/js/**","/img/**","/auth/**","/login*"};

    private UserDetailsService userDetailsService;
    private UserRepository userRepository;


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .authorizeRequests()
                    .antMatchers(publicStuff)
                    .permitAll()
                    .antMatchers("/user/**").access("hasRole('USER')")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/auth/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                   .permitAll()
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .permitAll()
                .and()
                    .exceptionHandling().accessDeniedPage("/AccessDenied");

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("Configuring I guess");
        auth.authenticationProvider(authProvider());

    }


    @Bean
    public DaoAuthenticationProvider authProvider(){
        final AuthenticationProviderImpl authProvider = new AuthenticationProviderImpl();
        authProvider.setUserDetailsService(this.userDetailsService);
        authProvider.setUserRepository(userRepository);

        return authProvider;

    }



    @Inject
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Inject
    public WebSecurityConfig setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
        return this;
    }
}
