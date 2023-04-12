package com.pradip.roommanagementsystem.security.config;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pradip.roommanagementsystem.exception.ErrorResponse;
import com.pradip.roommanagementsystem.security.service.CustomUserDetailsService;
import com.pradip.roommanagementsystem.security.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;
    private static  ObjectMapper objectMapper=new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;

        try {
            if (requestTokenHeader != null && jwtUtils.validateJwtToken(requestTokenHeader))
            username = jwtUtils.getUsernameFromToken(requestTokenHeader);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            chain.doFilter(request, response);
        }
        catch (JwtException ex) {
            logger.error("Jwt Token Error: " + ex.getMessage());
            addErrorToResponse(response, "Jwt Token Error: " + ex.getMessage(), 401);
        }
        catch (Exception  ex){
            logger.error("Unauthorized: "+ex.getMessage());
            addErrorToResponse(response,"Unauthorized: "+ex.getMessage(),401);
        }
    }

    public static HttpServletResponse addErrorToResponse(HttpServletResponse response, String errorMsg, int httpStatus) throws IOException {
        logger.error(errorMsg);
        response.setStatus(httpStatus);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(
                new ErrorResponse(httpStatus,errorMsg)));
        return response;
    }
}