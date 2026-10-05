package com.burkina.messenger.mapper;

import com.burkina.common.mapper.AbstractErrorMapper;
import com.burkina.messenger.exception.AuthorizationException;
import com.burkina.messenger.exception.IllegalChatTypeException;
import com.burkina.messenger.exception.MessageNotFoundException;
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
