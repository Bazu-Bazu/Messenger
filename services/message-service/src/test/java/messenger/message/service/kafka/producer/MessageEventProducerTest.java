package messenger.message.service.kafka.producer;

import dto.event.MessageDetailEvent;
import dto.event.MessageShortEvent;
import enums.ChatType;
import messenger.message.service.domain.entity.Message;
import messenger.message.service.mapper.MessageMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageEventProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private Message message;

    @Mock
    private MessageDetailEvent detailEvent;

    @Mock
    private MessageShortEvent shortEvent;

    @InjectMocks
    private MessageEventProducer producer;

    @Test
    void publishMessageNotification_personalChat_shouldPublishBothEvents() {
        when(message.getChatType()).thenReturn(ChatType.PERSONAL);
        when(messageMapper.toMessageDetailEvent(message, Set.of(2L, 3L))).thenReturn(detailEvent);
        when(messageMapper.toMessageShortEvent(message)).thenReturn(shortEvent);
        when(detailEvent.id()).thenReturn(100L);
        when(shortEvent.id()).thenReturn(200L);

        producer.publishMessageNotification(message, Set.of(2L, 3L));

        try { Thread.sleep(100); } catch (InterruptedException ignored) {}

        verify(kafkaTemplate).send("message_sending", "100", detailEvent);
        verify(kafkaTemplate).send("activity_personal_chat", "200", shortEvent);
        verify(kafkaTemplate, never()).send(eq("activity_group_chat"), any(), any());
    }

    @Test
    void publishMessageNotification_groupChat_shouldPublishBothEvents() {
        when(message.getChatType()).thenReturn(ChatType.GROUP);
        when(messageMapper.toMessageDetailEvent(message, Set.of(2L, 3L))).thenReturn(detailEvent);
        when(messageMapper.toMessageShortEvent(message)).thenReturn(shortEvent);
        when(detailEvent.id()).thenReturn(101L);
        when(shortEvent.id()).thenReturn(201L);

        producer.publishMessageNotification(message, Set.of(2L, 3L));

        try { Thread.sleep(100); } catch (InterruptedException ignored) {}

        verify(kafkaTemplate).send("message_sending", "101", detailEvent);
        verify(kafkaTemplate).send("activity_group_chat", "201", shortEvent);
        verify(kafkaTemplate, never()).send(eq("activity_personal_chat"), any(), any());
    }
}