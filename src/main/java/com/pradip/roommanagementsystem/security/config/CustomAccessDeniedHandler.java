package com.pradip.roommanagementsystem.security.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pradip.roommanagementsystem.security.config.AuthTokenFilter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException, ServletException {

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String roleType = authentication.getAuthorities().iterator().next().getAuthority();
//        System.out.println(roleType);
//        response.setStatus(403);
//        response.setContentType("application/json");
//        response.getWriter().write(new ObjectMapper().writeValueAsString(
//                new ErrorResponse(403,)));
        String errorMsg="Access Denied: You do not have permission to access '" + request.getMethod() + " " + request.getRequestURI() + "' resource.";
        response= AuthTokenFilter.addErrorToResponse(response, errorMsg, 403);
    }
}
