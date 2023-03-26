package com.pradip.roommanagementsystem.service;

import com.pradip.roommanagementsystem.dto.ApiResponse;
import com.pradip.roommanagementsystem.dto.ERoles;
import com.pradip.roommanagementsystem.dto.UserDTO;
import com.pradip.roommanagementsystem.entity.Role;
import com.pradip.roommanagementsystem.entity.User;
import com.pradip.roommanagementsystem.exception.ResourceNotFoundException;
import com.pradip.roommanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    PasswordEncoder encoder;

    private static final String projectionPackage = "com.pradip.roommanagementsystem.dto.projection.";

    public ApiResponse<List<?>> getAllUsers(String projectionName) throws ClassNotFoundException {
        List<?> allBy = userRepository.findAllBy(getClassName(projectionName));
        if(allBy == null || allBy.isEmpty()){
            throw new ResourceNotFoundException("Users are not available in system.");
        }
        return new ApiResponse<List<?>>(HttpStatus.OK.value(), "Users fetched successfully.", allBy);
    }

    public ApiResponse<Object> getUserById(Long id, String projectionName) throws ClassNotFoundException {
        Optional<?> userById = userRepository.findById(id,getClassName(projectionName));
        if(userById.isPresent()){
            return new ApiResponse<Object>(HttpStatus.OK.value(), "User fetched successfully.",userById.get());
        }
        else {
            return new ApiResponse<Object>(HttpStatus.NOT_FOUND.value(), "User not found.");
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
            return new ApiResponse<Object>(HttpStatus.NOT_FOUND.value(), "User not found.");
        }
    }

    public ApiResponse<UserDTO> createUser(User user) {
        Role role=new Role();
        role.setName(ERoles.USER);
        role.setUser(user);
        user.setRoles(Collections.singletonList(role));
        user.setPassword(encoder.encode(user.getPassword()));
        return new ApiResponse<UserDTO>(HttpStatus.OK.value(), "User saved successfully.",mapper.map(userRepository.save(user), UserDTO.class));
    }

    public ApiResponse<UserDTO> updateUser(User user) {
        return new ApiResponse<UserDTO>(HttpStatus.OK.value(), "User updated successfully.",mapper.map(userRepository.save(user), UserDTO.class));
    }
}
