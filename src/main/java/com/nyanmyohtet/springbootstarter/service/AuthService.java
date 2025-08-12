package com.nyanmyohtet.springbootstarter.service;

import com.nyanmyohtet.springbootstarter.client.AuthClient;
import com.nyanmyohtet.springbootstarter.client.dto.TokenResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final AuthClient authClient;

    public AuthService(AuthClient authClient) {
        this.authClient = authClient;
    }

    public Mono<String> fetchAccessToken(String jwtAssertion) {
        return authClient.requestAccessToken(jwtAssertion);
                //.map(TokenResponse::accessToken);
    }
}
