package com.burkina.messenger.kafka.consumer;

import com.burkina.common.dto.event.user.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import com.burkina.messenger.service.UserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConsumer {

    private final ObjectMapper mapper;
    private final UserService userService;

    @KafkaListener(topics = "user-registered")
    public void handleUserRegisteredEvent(String message, Acknowledgment ack) throws JsonProcessingException {
        UserRegisteredEvent event = mapper.readValue(message, UserRegisteredEvent.class);

        userService.createUser(event);

        ack.acknowledge();
    }

//    @KafkaListener(topics = "user-updated")
//    public void handleUserUpdatedEvent(String message, Acknowledgment ack) throws JsonProcessingException {
//        UserUpdatedEvent event = mapper.readValue(message, UserUpdatedEvent.class);
//
//        userService.updateAvatar(event);
//
//        ack.acknowledge();
//    }
}
