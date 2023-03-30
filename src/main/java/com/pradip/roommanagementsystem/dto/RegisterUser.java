package com.pradip.roommanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pradip.roommanagementsystem.entity.Address;
import com.pradip.roommanagementsystem.entity.Role;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@Data
public class RegisterUser {
    @Email
    private String email;

    @Size(max = 15)
    private String mobile;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String gender;
    private String profilePhoto;

    private List<Address> addresses;

    private List<Role> roles;

    private String password;
}
