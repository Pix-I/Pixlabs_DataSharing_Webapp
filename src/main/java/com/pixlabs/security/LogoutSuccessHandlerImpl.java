package com.pixlabs.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by pix-i on 24/01/2017.
 * ${Copyright}
 */

@Component("LogoutSuccessHandler")
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {


    /**
     * Overrides the super method, this handler
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        final HttpSession session = httpServletRequest.getSession();
        if(session!=null){
            session.removeAttribute("user");
        }

        //Maybe add a "see you again" page
        httpServletResponse.sendRedirect("/");


    }
}
