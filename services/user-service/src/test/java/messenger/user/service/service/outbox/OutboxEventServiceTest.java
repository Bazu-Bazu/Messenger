package messenger.user.service.service.outbox;

import enums.UserEventType;
import messenger.user.service.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OutboxEventServiceTest {

    @Mock
    private OutboxEventRepository outboxEventRepository;

    @InjectMocks
    private OutboxEventService outboxEventService;

    @Test
    void saveEvent_shouldCreateAndSaveOutboxEvent() {
        User user = User.builder()
                .id(1L)
                .username("john")
                .build();

        outboxEventService.saveEvent("user_event", UserEventType.USER_REGISTERED, user);

        ArgumentCaptor<OutboxEvent> captor = ArgumentCaptor.forClass(OutboxEvent.class);
        verify(outboxEventRepository).save(captor.capture());

        OutboxEvent savedEvent = captor.getValue();
        assertEquals("user_event", savedEvent.getTopic());
        assertEquals(UserEventType.USER_REGISTERED, savedEvent.getEventType());
        assertEquals(user, savedEvent.getUser());
        assertNotNull(savedEvent.getCreatedAt());
        assertTrue(savedEvent.getCreatedAt().isBefore(Instant.now().plusSeconds(1)));
    }
}
