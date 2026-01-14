package messenger.message.service.service.event;

import messenger.message.service.entity.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageEventProducer {

    public void publishMessageSent(Message message) {

    }

    public void publishMessageNotification(Message message, List<Long> memberIds) {

    }

    public void publishMessageEdited(Message message) {

    }

    public void publishMessageRead(Message message) {

    }

}
