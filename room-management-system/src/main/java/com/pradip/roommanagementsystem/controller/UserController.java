package com.pradip.roommanagementsystem.controller;

import com.pradip.roommanagementsystem.dto.*;
import com.pradip.roommanagementsystem.entity.Role;
import com.pradip.roommanagementsystem.entity.User;
import com.pradip.roommanagementsystem.security.CustomUserDetails;
import com.pradip.roommanagementsystem.security.JwtUtils;
import com.pradip.roommanagementsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(principal);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<?>>> getAlUsers(@RequestParam("projection") String projectionName) throws ClassNotFoundException {
        return ResponseEntity.ok(userService.getAllUsers(projectionName));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getUserById(
            @RequestParam("projection") String projectionName,
            @PathVariable Long id) throws ClassNotFoundException {
        return ResponseEntity.ok(userService.getUserById(id, projectionName));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody User user){
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@RequestBody User user){
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable Long id) throws ClassNotFoundException {
        return ResponseEntity.ok(userService.deleteUserById(id));
    }
}