package messenger.sso.service.exception.mapper;

import mapper.AbstractErrorMapper;
import messenger.sso.service.exception.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
public class ErrorMapper extends AbstractErrorMapper {

    @Override
    protected int getErrorCode(Throwable e) {
        if (
                e instanceof SsoUserNotFoundException ||
                e instanceof RefreshTokenNotFoundException
        ) {
            return 404;
        } else if (
                e instanceof IllegalRefreshTokenException ||
                e instanceof RefreshTokenExpiredException ||
                e instanceof RefreshTokenReuseException ||
                e instanceof UserIsNotEnabledException
        ) {
            return 401;
        } else if (e instanceof AuthorizationException) {
            return 403;
        } else if (e instanceof MethodArgumentNotValidException) {
            return 400;
        }

        return 500;
    }
}