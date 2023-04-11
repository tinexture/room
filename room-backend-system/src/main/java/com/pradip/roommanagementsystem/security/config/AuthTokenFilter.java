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
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtils.getUsernameFromToken(jwtToken);
            } catch (ExpiredJwtException e) {
                String errorMsg = "JWT Token has expired";
                response=addErrorToResponse(response,errorMsg,HttpStatus.UNAUTHORIZED.value());
                return;
            }
            catch (Exception e){
                String errorMsg = "Invalid JWT token";
                response=addErrorToResponse(response,errorMsg,HttpStatus.UNAUTHORIZED.value());
                return;
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        try {
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtUtils.validateToken(jwtToken, userDetails)) {

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            chain.doFilter(request, response);
        }catch (AccessDeniedException  | UsernameNotFoundException  ex){
            addErrorToResponse(response,"Unauthorized: "+ex.getMessage(),401);
//            response.setStatus(HttpStatus.FORBIDDEN.value());
//            Map<String, Object> objectBody = new LinkedHashMap<>();
//            objectBody.put("timestamp", new Date());
//            objectBody.put("error", ex.getMessage());
//            objectBody.put("status", HttpStatus.FORBIDDEN.value());
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//            String jsonResponse = objectMapper.writeValueAsString(objectBody);
//
//            response.getWriter().write(jsonResponse);
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

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        try {
//            String jwt = parseJwt(request);
//            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
//                String username = jwtUtils.getUserNameFromJwtToken(jwt);
//
//                CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
//                System.out.println(userDetails);
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        } catch (Exception e) {
//            logger.error("Cannot set user authentication: {}", e);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private String parseJwt(HttpServletRequest request) {
//        String headerAuth = request.getHeader("Authorization");
//
//        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
//            return headerAuth.substring(7, headerAuth.length());
//        }
//
//        return null;
//    }
}