package com.burkina.messenger.dto.request;

import jakarta.validation.constraints.Size;

public record ChangeGroupInfoRequest(
        Long avatarId,

        @Size(min = 1, max = 40, message = "Group name must be 1-40 characters")
        String name
) {}
