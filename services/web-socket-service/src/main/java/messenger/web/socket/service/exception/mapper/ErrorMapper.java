package messenger.web.socket.service.exception.mapper;

import mapper.AbstractErrorMapper;
import org.springframework.stereotype.Component;

@Component
public class ErrorMapper extends AbstractErrorMapper {

    @Override
    protected int getErrorCode(Throwable e) {
        return 500;
    }
}
