package com.burkina.messenger.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

public record RemoveMembersRequest(
        @NotEmpty(message = "The users must be specified")
        @Size(max = 50, message = "You can delete a maximum of 50 members at a time")
        List<Long> userIds
) {

    public RemoveMembersRequest {
        userIds = userIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }
}
