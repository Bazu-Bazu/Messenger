package messenger.user.service.exception.mapper;

import mapper.AbstractErrorMapper;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.stereotype.Component;

@Component
public class ErrorMapper extends AbstractErrorMapper {

    @Override
    protected int getErrorCode(Throwable e) {
        if (e instanceof MethodArgumentNotValidException) {
            return 400;
        }

        return 500;
    }
}
