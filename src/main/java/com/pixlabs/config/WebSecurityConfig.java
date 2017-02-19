package com.pixlabs.config;

import com.pixlabs.data.dao.user.UserRepository;
import com.pixlabs.security.AuthenticationProviderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.inject.Inject;

/**
 * Created by pix-i on 01/01/2017.
 * ${Copyright}
 */

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private String[] publicStuff = {"/index","/data/get/**","/","/static/css/**","/css/**","/js/**","/img/**","/auth/**","/user/**","/login*","/auth/logout"};

    private UserDetailsService userDetailsService;
    private UserRepository userRepository;
    private LogoutSuccessHandler logoutSuccessHandler;

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .authorizeRequests()
                    .antMatchers(publicStuff)
                    .permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/auth/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successForwardUrl("/auth/loginSuccess")
                   .permitAll()
                .and()
                .logout()
                    .logoutUrl("/auth/logout")
                    .logoutSuccessUrl("/auth/logoutSuccess")
                    //.deleteCookies("")
//                    .logoutSuccessHandler(logoutSuccessHandler)
                    .invalidateHttpSession(false)
                    .permitAll()
                .and()
                    .exceptionHandling().accessDeniedPage("/auth/loginPage")
                .and()
                    .sessionManagement()
                    .maximumSessions(1).sessionRegistry(sessionRegistry());

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
        authProvider.setPasswordEncoder(encoder());
        return authProvider;

    }


    @Bean
    public PasswordEncoder encoder(){
        PasswordEncoder encoder = new BCryptPasswordEncoder(11);
        return encoder;
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
