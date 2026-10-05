package com.burkina.messenger.controller;

import com.burkina.common.enums.messenger.ChatType;
import com.burkina.messenger.domain.entity.Message;
import com.burkina.messenger.dto.response.MessageResponse;
import com.burkina.messenger.mapper.MessageMapper;
import com.burkina.messenger.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class MessageController {

    private final MessageMapper messageMapper;
    private final MessageService messageService;

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageResponse>> getChatMessages(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long chatId,
            @RequestParam ChatType chatType,
            @RequestParam(defaultValue = "0") int before,
            @RequestParam(defaultValue = "20") int after
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        List<Message> messages = messageService.getChatMessages(
                userId,
                chatId,
                chatType,
                before,
                after
        );

        return ResponseEntity.ok().body(messageMapper.toMessageResponseList(messages));
    }
}
