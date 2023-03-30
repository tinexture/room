package com.pradip.roommanagementsystem.security.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
