package com.burkina.messenger.client;

import com.burkina.common.dto.request.UserStatusRequest;
import com.burkina.common.dto.response.UserStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IdentityUserClient {

    private final RestClient identityRestClient;

    public List<UserStatusResponse> getUsersStatus(List<Long> userIds) {
        return identityRestClient.post()
                .uri("/internal/users/status")
                .body(new UserStatusRequest(userIds))
                .retrieve()
                .body(new ParameterizedTypeReference<List<UserStatusResponse>>() {});
    }
}