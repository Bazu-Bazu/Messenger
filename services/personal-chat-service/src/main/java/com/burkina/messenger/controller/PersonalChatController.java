package com.burkina.messenger.controller;

import com.burkina.messenger.domain.entity.PersonalChat;
import com.burkina.messenger.mapper.PersonalChatMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.burkina.messenger.dto.request.CreatePersonalChatRequest;
import com.burkina.messenger.dto.response.PersonalChatResponse;
import com.burkina.messenger.service.PersonalChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chats/personal")
@RequiredArgsConstructor
public class PersonalChatController {

    private final PersonalChatMapper chatMapper;
    private final PersonalChatService personalChatService;

    @PostMapping
    public ResponseEntity<PersonalChatResponse> getOrCreatePersonalChat(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreatePersonalChatRequest request
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        PersonalChat chat = personalChatService.getOrCreate(userId, request);

        return ResponseEntity.status(HttpStatus.OK).body(chatMapper.toResponse(chat));
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deletePersonalChat(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("chatId") @Valid Long chatId
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        personalChatService.delete(userId, chatId);

        return ResponseEntity.noContent().build();
    }
}
