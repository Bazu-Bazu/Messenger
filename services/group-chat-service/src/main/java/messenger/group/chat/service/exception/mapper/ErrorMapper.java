package messenger.group.chat.service.exception.mapper;

import mapper.AbstractErrorMapper;
import messenger.group.chat.service.exception.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
public class ErrorMapper extends AbstractErrorMapper {

    @Override
    protected int getErrorCode(Throwable e) {
        if (
                e instanceof GroupNotFoundException ||
                e instanceof GroupMemberNotFoundException ||
                e instanceof UserNotFoundException
        ) {
            return 404;
        } else if (
                e instanceof UserIsNotActive ||
                e instanceof BadRequestException) {
            return 400;
        } else if (e instanceof AuthorizationException) {
            return 403;
        } else if (e instanceof MethodArgumentNotValidException) {
            return 400;
        }

        return 500;
    }
}
