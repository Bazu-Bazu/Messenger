package com.burkina.messenger.dto.response;

import com.burkina.common.enums.messenger.ChatType;
import com.burkina.common.enums.messenger.MessageType;
import lombok.Builder;

import java.time.Instant;

@Builder
public record MessageResponse(
        Long messageId,
        String text,
        MessageType messageType,
        Instant createdAt,
        Boolean read,
        Long mediaId,
        Long chatId,
        ChatType chatType,
        Long userId
) {}
