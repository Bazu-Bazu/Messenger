package com.burkina.messenger.mapper;

import com.burkina.common.mapper.AbstractErrorMapper;
import com.burkina.messenger.exception.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
public class ErrorMapper extends AbstractErrorMapper {

    @Override
    protected int getErrorCode(Throwable e) {
        if (
                e instanceof PersonalChatNotFoundException ||
                e instanceof UserNotFoundException ||
                e instanceof SavedChatNotFoundException
        ) {
            return 404;
        } else if (
                e instanceof UserIsNotActiveException ||
                e instanceof IllegalRequestExcepion) {
            return 400;
        } else if (e instanceof AuthorizationException) {
            return 403;
        } else if (e instanceof MethodArgumentNotValidException) {
            return 400;
        }

        return 500;
    }
}
