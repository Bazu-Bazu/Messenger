package com.burkina.messenger.controller;

import com.burkina.messenger.domain.entity.SavedChat;
import com.burkina.messenger.mapper.SavedChatMapper;
import lombok.RequiredArgsConstructor;
import com.burkina.messenger.dto.response.SavedChatResponse;
import com.burkina.messenger.service.SavedChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chats/saved")
@RequiredArgsConstructor
public class SavedChatController {

    private final SavedChatMapper chatMapper;
    private final SavedChatService savedChatService;

    @PostMapping
    public ResponseEntity<SavedChatResponse> getOrCreateSavedChat(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());

        SavedChat chat = savedChatService.getOrCreate(userId);

        return ResponseEntity.status(HttpStatus.OK).body(chatMapper.toResponse(chat));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSavedChat(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());

        savedChatService.delete(userId);

        return ResponseEntity.noContent().build();
    }
}
