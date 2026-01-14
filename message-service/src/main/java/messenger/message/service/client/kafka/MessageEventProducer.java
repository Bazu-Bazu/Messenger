package messenger.message.service.client.kafka;

import messenger.message.service.dto.response.MessageResponse;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MessageEventProducer {

    public void publishMessageNotification(MessageResponse messageResponse, Set<Long> memberIds) {

    }

}
