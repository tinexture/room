package com.pradip.roommanagementsystem.security.controller;

import com.pradip.roommanagementsystem.dto.ApiResponse;
import com.pradip.roommanagementsystem.security.dto.LoginRequest;
import com.pradip.roommanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.authenticateUser(loginRequest);
    }

    @GetMapping("/verify-token")
    public ResponseEntity<ApiResponse> verifyToken(@RequestHeader("token") String token) {
        return ResponseEntity.ok(userService.verifyJwtToken(token));
    }
}
