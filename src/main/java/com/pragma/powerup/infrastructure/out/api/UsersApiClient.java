package com.pragma.powerup.infrastructure.out.api;

import com.pragma.powerup.application.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class UsersApiClient {

    private final RestTemplate restTemplate;

    @Value("${users.service.url}")
    private String baseUrl;

    public UserResponseDto getUserById(Long userId, String token) {
        String url = baseUrl + "users/findById/" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<UserResponseDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, UserResponseDto.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get user. Status code: " + response.getStatusCode());
        }
    }
}
