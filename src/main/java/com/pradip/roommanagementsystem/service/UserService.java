package com.pradip.roommanagementsystem.service;

import com.pradip.roommanagementsystem.dto.*;
import com.pradip.roommanagementsystem.entity.Otp;
import com.pradip.roommanagementsystem.entity.Role;
import com.pradip.roommanagementsystem.entity.User;
import com.pradip.roommanagementsystem.exception.EmailException;
import com.pradip.roommanagementsystem.exception.UnauthorizedException;
import com.pradip.roommanagementsystem.repository.UserRepository;
import com.pradip.roommanagementsystem.security.dto.CustomUserDetails;
import com.pradip.roommanagementsystem.security.dto.LoginRequest;
import com.pradip.roommanagementsystem.security.util.JwtUtils;
import com.pradip.roommanagementsystem.util.GeneralUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private GeneralUtil util;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private static final String projectionPackage = "com.pradip.roommanagementsystem.dto.projection.";

    public ApiResponse<List<?>> getAllUsers(String projectionName) throws ClassNotFoundException {
        List<User> allBy = userRepository.findAll();
        //        List<?> allBy = userRepository.findAllBy(getClassName(projectionName));
        if(allBy == null || allBy.isEmpty()){
            throw  new EntityNotFoundException("User not found.");
        }

        return new ApiResponse<List<?>>(HttpStatus.OK.value(), "Users fetched successfully.", allBy);
    }

    public ApiResponse<Object> getUserById(Long id, String projectionName) throws ClassNotFoundException {
//        Optional<?> userById = userRepository.findById(id,getClassName(projectionName));
        Optional<?> userById=userRepository.findById(id);
        if(userById.isPresent()){
            return new ApiResponse<Object>(HttpStatus.OK.value(), "User fetched successfully.",userById.get());
        }
        else {
            throw  new EntityNotFoundException("User not found.");
        }
    }
    public ApiResponse<Object> getUserByEmail(String email) {
        Optional<User> userById=userRepository.findByEmail(email);
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

    public ApiResponse<RegisterUser> createUser(RegisterUser registerUser) {
        User user = util.convertObject(registerUser, User.class);
        Role role=new Role();
        role.setName(ERoles.ROLE_USER);
        role.setUser(user);
        user.setRoles(Collections.singletonList(role));
        user.setPassword(encoder.encode(user.getPassword()));
        return new ApiResponse<RegisterUser>(HttpStatus.OK.value(), "User saved successfully.",util.convertObject(userRepository.save(user), RegisterUser.class));
    }

    public ApiResponse<RegisterUser> updateUser(User user) {
        return new ApiResponse<RegisterUser>(HttpStatus.OK.value(), "User updated successfully.",util.convertObject(userRepository.save(user), RegisterUser.class));
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

    public ApiResponse<String> sendOtpToEmail(String email) throws MessagingException {
        User userPersonal = (User) getUserByEmail(email).getData();
        String otp = util.generateOtp();
        if(util.sendOtp(email,userPersonal.getFirstName()+" "+userPersonal.getLastName(),otp)){
            userPersonal.setOtp(new Otp(otp,false,userPersonal));
            userRepository.save(userPersonal);
        }
        return new ApiResponse<String>(HttpStatus.OK.value(), "Otp sent sucessfully");
    }

    public ApiResponse<String> verifyOtpToEmail(String email, String otp) {
        User user = (User) getUserByEmail(email).getData();
        Otp otpDTO = user.getOtp();
        if(otpDTO != null){
            if(!otp.equals(otpDTO.getCode())){
                throw new EmailException("Invalid OTP. Please check the code and try again.");
            }
            // check otp to ensure it has not expired
            long timeLapse = new Date().getTime() - otpDTO.getCreatedAt().getTime();
            // ensure the time lapse is not greater than 15 mins in milliseconds
            if(timeLapse >= 900000) {
                throw new EmailException("The One-Time Password (OTP) has expired. Please request a new one.");
            }
            if (otpDTO.isVerified() != true){
                otpDTO.setVerified(true);
                user.setOtp(otpDTO);
                userRepository.save(user);
            }
        }
        return new ApiResponse<String>(HttpStatus.OK.value(), "OTP verification successful");
    }

    public ApiResponse<String> changePassword(ChangePasswordDTO passwordDTO) {
        User userByEmail = (User)getUserByEmail(passwordDTO.getEmail()).getData();
        if(!userByEmail.getOtp().isVerified()){
            throw new EmailException("OTP is not verified. Please resend otp and try again.");
        }
        // ensure the time lapse is not greater than 15 mins in milliseconds
        if(new Date().getTime() - userByEmail.getOtp().getCreatedAt().getTime() >= 900000) {
            throw new EmailException("The One-Time Password (OTP) has expired. Please request a new one.");
        }
        userByEmail.setPassword(passwordEncoder.encode(passwordDTO.getPassword()));
        userByEmail.setOtp(null);
        userRepository.save(userByEmail);
        return new ApiResponse<String>(HttpStatus.OK.value(), "Password updated successfully");
    }
}
