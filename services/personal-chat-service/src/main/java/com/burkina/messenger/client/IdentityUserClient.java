package com.burkina.messenger.client;

import com.burkina.common.dto.response.UserStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class IdentityUserClient {

    private final RestClient identityRestClient;

    public UserStatusResponse getUserStatus(Long userId) {
        return identityRestClient.get()
                .uri("/internal/users/{userId}/status", userId)
                .retrieve()
                .body(UserStatusResponse.class);
    }
}