package com.pragma.powerup.infrastructure.security.impl;

import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.infrastructure.security.jwt.UserManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final static Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final RestTemplate restTemplate;
    @Value("${users.service.url}")
    private String baseUrl;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        try {
            ResponseEntity<UserResponseDto> responseEntity = restTemplate.getForEntity(baseUrl + "/users/findByEmail/" + userEmail, UserResponseDto.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                UserResponseDto userResponse = responseEntity.getBody();
                if (userResponse != null) {
                    List<GrantedAuthority> authorities = userResponse.getRoles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    LOGGER.debug("Loaded user authorities successfully");
                    return new UserManager(userResponse.getEmail(), authorities);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving user details: {}", e.getMessage());
        }
        throw new UsernameNotFoundException("User not found with email: " + userEmail);
    }
}
