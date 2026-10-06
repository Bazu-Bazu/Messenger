package com.burkina.messenger.service;

import com.burkina.common.dto.event.messenger.message.*;
import com.burkina.common.enums.messenger.ChatType;
import com.burkina.messenger.domain.entity.ChatMember;
import com.burkina.messenger.service.event.MessagePublisher;
import lombok.RequiredArgsConstructor;
import com.burkina.messenger.domain.entity.Message;
import com.burkina.messenger.domain.repository.MessageRepository;
import com.burkina.messenger.exception.MessageNotFoundException;
import com.burkina.messenger.validation.MemberRightsValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChatMemberService memberService;
    private final MessagePublisher messagePublisher;
    private final MessageRepository messageRepository;
    private final MemberRightsValidator validationMemberService;

    @Transactional
    public void sendMessage(SendMessageEvent event) {
        validationMemberService.validateSending(event.userId(), event.chatId(), event.chatType());

        Message newMessage = Message.builder()
                .chatId(event.chatId())
                .text(event.text())
                .senderId(event.userId())
                .messageType(event.messageType())
                .chatType(event.chatType())
                .build();

        Message savedMessage = messageRepository.save(newMessage);

        Set<Long> memberIds = memberService.getMembersIdsByChat(event.chatId(), event.chatType());

        messagePublisher.publishMessageSentEvent(savedMessage, memberIds);
    }

    @Transactional
    public void removeMessage(RemoveMessageEvent event) {
        Message message = getMessageById(event.messageId());

        validationMemberService.validateRemoving(event.userId(), message);

        messageRepository.delete(message);

        Set<Long> memberIds = memberService.getMembersIdsByChat(message.getChatId(), message.getChatType());

        messagePublisher.publishMessageRemovedEvent(message, memberIds);
    }

    @Transactional
    public void markMessageAsRead(MarkMessageAsReadEvent event) {
        Message message = getMessageById(event.messageId());

        validationMemberService.validateReading(event.userId(), message.getChatId(), message.getChatType());

        int savedMessage = messageRepository.markAsRead(event.messageId());

        if (savedMessage == 1) {
            Set<Long> memberIds = memberService.getMembersIdsByChat(message.getChatId(), message.getChatType());

            messagePublisher.publishMessageReadEvent(message, memberIds);
        }

        memberService.updateLastReadMessage(event.userId(), message.getChatId(), message.getChatType(), message.getId());
    }

    @Transactional(readOnly = true)
    public Message getMessageById(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(
                        String.format("Message %d not found", messageId)
                ));
    }

    @Transactional(readOnly = true)
    public List<Message> getChatMessages(Long userId, Long chatId, ChatType chatType, int before, int after) {
        ChatMember member = validationMemberService.validateReading(userId, chatId, chatType);

        if (member.getLastReadMessageId() == null) {
            List<Message> messages = messageRepository.findByChatIdAndChatTypeOrderByIdDesc(
                    chatId,
                    chatType,
                    PageRequest.of(0, before + after)
            );

            Collections.reverse(messages);

            return messages;
        }

        List<Message> previousMessages =
                messageRepository.findPreviousMessages(
                        chatId,
                        chatType,
                        member.getLastReadMessageId(),
                        PageRequest.of(0, before)
                );

        List<Message> newMessages =
                messageRepository.findNextMessages(
                        chatId,
                        chatType,
                        member.getLastReadMessageId(),
                        PageRequest.of(0, after)
                );

        Collections.reverse(previousMessages);

        return Stream.concat(
                previousMessages.stream(),
                newMessages.stream()
        ).toList();
    }
}
