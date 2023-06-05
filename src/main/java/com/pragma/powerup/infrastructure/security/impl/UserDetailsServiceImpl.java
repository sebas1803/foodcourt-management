package com.pragma.powerup.infrastructure.security.impl;

import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.infrastructure.security.jwt.UserManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RestTemplate restTemplate;
    @Value("${users.service.url}")
    private String baseUrl;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        UserResponseDto userResponse = restTemplate.getForObject(baseUrl + "users/findByEmail/" + userEmail, UserResponseDto.class);
        if (userResponse == null) {
            throw new UsernameNotFoundException("User not found with email: " + userEmail);
        }
        List<GrantedAuthority> authorities = userResponse.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UserManager(userResponse.getEmail(), authorities);
    }
}
