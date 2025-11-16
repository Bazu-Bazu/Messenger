package messenger.user.service.service.event;

import lombok.RequiredArgsConstructor;
import messenger.user.service.dto.event.UserEvent;
import messenger.user.service.entity.User;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserRegistrationToKafka(User user) {
        UserEvent event = createUserEvent(user);

        kafkaTemplate.send("user_registration", event.id().toString(), event);
    }

    private UserEvent createUserEvent(User user) {
        return UserEvent.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .phone(user.getPhone())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

}
