package com.pradip.roommanagementsystem.security.service;

import com.pradip.roommanagementsystem.entity.User;
import com.pradip.roommanagementsystem.repository.UserRepository;
import com.pradip.roommanagementsystem.security.dto.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        CustomUserDetails customUserDetails =new CustomUserDetails();
        customUserDetails.setId(user.getId());
        customUserDetails.setUsername(user.getEmail());
        customUserDetails.setFullName(user.getFullName());
        customUserDetails.setAuthorities(authorities);
        customUserDetails.setLocked(user.isLocked());
        customUserDetails.setEnabled(user.isEnabled());
        customUserDetails.setPassword(user.getPassword());

        return customUserDetails;
    }
}

