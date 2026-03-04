package messenger.personal.chat.service.exception.mapper;

import mapper.AbstractErrorMapper;
import messenger.personal.chat.service.exception.AuthorizationException;
import messenger.personal.chat.service.exception.PersonalChatNotFoundException;
import messenger.personal.chat.service.exception.UserIsNotActive;
import messenger.personal.chat.service.exception.UserNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
public class ErrorMapper extends AbstractErrorMapper {

    @Override
    protected int getErrorCode(Throwable e) {
        if (
                e instanceof PersonalChatNotFoundException ||
                e instanceof UserNotFoundException
        ) {
            return 404;
        } else if (e instanceof UserIsNotActive) {
            return 400;
        } else if (e instanceof AuthorizationException) {
            return 403;
        } else if (e instanceof MethodArgumentNotValidException) {
            return 400;
        }

        return 500;
    }
}
