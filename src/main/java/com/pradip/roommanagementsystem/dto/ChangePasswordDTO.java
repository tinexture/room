package com.pradip.roommanagementsystem.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String password;

}
