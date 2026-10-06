package com.burkina.messenger.dto.response;

import com.burkina.common.enums.messenger.ChatType;
import lombok.Builder;

@Builder
public record MessageRemovedResponse(
        Long messageId,
        Long chatId,
        ChatType chatType
) {}
