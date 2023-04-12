package com.pradip.roommanagementsystem.dto.projection;

import com.pradip.roommanagementsystem.entity.Role;

import java.util.List;

public interface JwtUser {
    Long getId();
    String getEmail();
    String getFirstName();
    String getLastName();
    List<Role> getRoles();
    String getPassword();
}
