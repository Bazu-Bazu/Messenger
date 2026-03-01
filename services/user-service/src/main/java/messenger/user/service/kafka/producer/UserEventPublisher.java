package messenger.user.service.kafka.producer;

import dto.event.UserRegistrationEvent;
import enums.UserUpdateType;
import lombok.RequiredArgsConstructor;
import dto.event.UserUpdatingEvent;
import messenger.user.service.domain.entity.User;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserRegistrationToKafka(User user) {
        UserRegistrationEvent event = createUserRegistrationEvent(user);

        kafkaTemplate.send("user_registration", event.id().toString(), event);
    }

    private UserRegistrationEvent createUserRegistrationEvent(User user) {
        return UserRegistrationEvent.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phone(user.getPhone())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
    public void sendUserUpdatingToKafka(User user, UserUpdateType type) {
        UserUpdatingEvent event = createUserUpdatingEvent(user, type);

        kafkaTemplate.send("user_updating", event.id().toString(), event);
    }

    private UserUpdatingEvent createUserUpdatingEvent(User user, UserUpdateType type) {
        String updatedField = switch (type) {
            case EMAIL -> user.getEmail();
            case PHONE -> user.getPhone();
            case PASSWORD -> user.getPassword();
            case USERNAME -> user.getUsername();
        };

        return UserUpdatingEvent.builder()
                .id(user.getId())
                .updatedField(updatedField)
                .type(type)
                .build();
    }

}
