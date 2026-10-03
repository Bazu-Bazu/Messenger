package com.burkina.messenger.dto.response;

import lombok.*;

import java.time.Instant;

@Builder
public record GroupResponse(
        Long id,
        String name,
        Long avatarId,
        Long createdBy,
        Instant createdAt
) {}
