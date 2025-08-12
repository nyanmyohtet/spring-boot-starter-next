package com.nyanmyohtet.springbootstarter.service;

import com.nyanmyohtet.springbootstarter.client.ApiClient;
import com.nyanmyohtet.springbootstarter.client.model.ApiMethod;
import com.nyanmyohtet.springbootstarter.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApiService {
    private final Logger LOGGER = LoggerFactory.getLogger(ApiService.class);

    private final ApiClient apiClient;
    private final JwtUtil jwtUtil;

    public String getAccessToken() {
        String accessToken = "";

        String jwt = jwtUtil.generateJwtAssertion();

        String response = apiClient.callApi(
                "/oauth/token",
                ApiMethod.POST,
                null,
                null,
                String.class,
                Map.of(),
                Map.of()
        ).block();


        return accessToken;
    }
}
