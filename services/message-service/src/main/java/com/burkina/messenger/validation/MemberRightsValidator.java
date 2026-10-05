package com.burkina.messenger.validation;

import com.burkina.common.enums.messenger.ChatType;
import com.burkina.messenger.domain.entity.ChatMember;
import com.burkina.messenger.domain.entity.Message;
import com.burkina.messenger.exception.AuthorizationException;
import com.burkina.messenger.service.ChatMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberRightsValidator {

    private final ChatMemberService memberService;

    public void validateSending(Long userId, Long chatId, ChatType chatType) {
        ChatMember member = memberService.getMemberByUserAndChat(userId, chatId, chatType);

        if (!member.getCanSendMessage()) {
            throw new AuthorizationException(
                        String.format("User with id %d has no rights to send messages in %s chat with id %d",
                                userId, chatType, chatId)
            );
        }
    }

    public ChatMember validateReading(Long userId, Long chatId, ChatType chatType) {
        return memberService.getMemberByUserAndChat(userId, chatId, chatType);
    }

    public void validateRemoving(Long userId, Message message) {
        if (!message.getSenderId().equals(userId)) {
            throw new AuthorizationException(
                    String.format("User %d is not allowed to remove message %d", userId, message.getId())
            );
        }
    }
}
