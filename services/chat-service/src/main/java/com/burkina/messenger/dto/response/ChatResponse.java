package com.burkina.messenger.dto.response;

import com.burkina.common.enums.messenger.ChatType;
import lombok.Builder;

@Builder
public record ChatResponse(
        Long chatId,
        ChatType chatType,
        String name,
        Long avatarId
) {}
