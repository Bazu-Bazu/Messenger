package messenger.message.service.exception.mapper;

import mapper.AbstractErrorMapper;
import messenger.message.service.exception.AuthorizationException;
import messenger.message.service.exception.IllegalChatTypeException;
import messenger.message.service.exception.MessageNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ErrorMapper extends AbstractErrorMapper {

    @Override
    protected int getErrorCode(Throwable e) {
        if (e instanceof MessageNotFoundException) {
            return 404;
        } else if (e instanceof IllegalChatTypeException) {
            return 400;
        } else if (e instanceof AuthorizationException) {
            return 403;
        }

        return 500;
    }
}
