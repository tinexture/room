package com.pradip.roommanagementsystem.service;

import com.pradip.roommanagementsystem.dto.*;
import com.pradip.roommanagementsystem.entity.Role;
import com.pradip.roommanagementsystem.entity.User;
import com.pradip.roommanagementsystem.exception.ResourceNotFoundException;
import com.pradip.roommanagementsystem.exception.UnauthorizedException;
import com.pradip.roommanagementsystem.repository.UserRepository;
import com.pradip.roommanagementsystem.security.dto.CustomUserDetails;
import com.pradip.roommanagementsystem.security.dto.LoginRequest;
import com.pradip.roommanagementsystem.security.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;


    private static final String projectionPackage = "com.pradip.roommanagementsystem.dto.projection.";

    public ApiResponse<List<?>> getAllUsers(String projectionName) throws ClassNotFoundException {
        List<?> allBy = userRepository.findAllBy(getClassName(projectionName));
        if(allBy == null || allBy.isEmpty()){
            throw  new EntityNotFoundException("User not found.");
        }
        return new ApiResponse<List<?>>(HttpStatus.OK.value(), "Users fetched successfully.", allBy);
    }

    public ApiResponse<Object> getUserById(Long id, String projectionName) throws ClassNotFoundException {
        Optional<?> userById = userRepository.findById(id,getClassName(projectionName));
        if(userById.isPresent()){
            return new ApiResponse<Object>(HttpStatus.OK.value(), "User fetched successfully.",userById.get());
        }
        else {
            throw  new EntityNotFoundException("User not found.");
        }
    }

    private Class<?> getClassName(String projectionName) throws ClassNotFoundException {
        return Class.forName(projectionPackage+""+projectionName);
    }


    public ApiResponse<Object> deleteUserById(Long id) throws ClassNotFoundException {
        Optional<?> userById = userRepository.findById(id, getClassName("UserProjectionDTO"));
        if(userById.isPresent()){
            userRepository.deleteById(id);
            return new ApiResponse<Object>(HttpStatus.OK.value(), "User deleted successfully.",userById.get());
        }
        else {
            throw  new EntityNotFoundException("User not found.");
        }
    }

    public ApiResponse<UserDTO> createUser(User user) {
        Role role=new Role();
        role.setName(ERoles.ROLE_USER);
        role.setUser(user);
        user.setRoles(Collections.singletonList(role));
        user.setPassword(encoder.encode(user.getPassword()));
        return new ApiResponse<UserDTO>(HttpStatus.OK.value(), "User saved successfully.",mapper.map(userRepository.save(user), UserDTO.class));
    }

    public ApiResponse<UserDTO> updateUser(User user) {
        return new ApiResponse<UserDTO>(HttpStatus.OK.value(), "User updated successfully.",mapper.map(userRepository.save(user), UserDTO.class));
    }

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(principal);
            return ResponseEntity.ok(new JwtResponse(jwt));
        }catch (Exception e){
            throw new UnauthorizedException(e.getMessage());
        }
    }
}
