package com.burkina.messenger.dto.response;

import lombok.*;
import com.burkina.messenger.domain.enums.GroupMemberRole;

@Builder
public record GroupMemberResponse(
        Long id,
        GroupMemberRole role
) {}
