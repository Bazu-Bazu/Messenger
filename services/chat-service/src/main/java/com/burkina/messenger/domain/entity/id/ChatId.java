package com.burkina.messenger.domain.entity.id;

import com.burkina.common.enums.messenger.ChatType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class ChatId {

    private Long chatId;
    private ChatType chatType;
}
