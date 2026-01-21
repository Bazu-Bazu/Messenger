package messenger.web.socket.service.client.grpc;

import dto.request.EditMessageRequest;
import dto.request.MarkMessageAsReadRequest;
import dto.request.SendMessageRequest;
import dto.response.ErrorResponse;
import dto.response.MessageResponse;
import dto.result.MessageResult;
import lombok.RequiredArgsConstructor;
import message.Message;
import message.MessageServiceGrpc;
import converter.MessageConverter;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class MessageGrpcClient {

    @GrpcClient("message-service")
    private MessageServiceGrpc.MessageServiceBlockingStub blockingStub;
    private final MessageConverter messageConverter;

    public MessageResult sendMessage(SendMessageRequest wsRequest, Long senderId) {
        Message.SendMessageRequest grpcRequest = messageConverter.toGrpcRequest(wsRequest, senderId);

        Message.MessageResult grpcResult = blockingStub.sendMessage(grpcRequest);

        return proccessMessageResult(grpcResult);
    }

    public MessageResult editMessage(EditMessageRequest wsRequest, Long editorId) {
        Message.EditMessageRequest grpcRequest = messageConverter.toGrpcRequest(wsRequest, editorId);

        Message.MessageResult grpcResult = blockingStub.editMessage(grpcRequest);

        return proccessMessageResult(grpcResult);
    }

    public MessageResult markAsRead(MarkMessageAsReadRequest wsRequest, Long readerId) {
        Message.MarkAsReadRequest grpcRequest = messageConverter.toGrpcRequest(wsRequest, readerId);

        Message.MessageResult grpcResult = blockingStub.markAsRead(grpcRequest);

        return proccessMessageResult(grpcResult);
    }

    private MessageResult proccessMessageResult(Message.MessageResult grpcResult) {
        switch (grpcResult.getResultCase()) {
            case SUCCESS:
                Message.MessageResponse grpcSuccess = grpcResult.getSuccess();
                MessageResponse successResponse = messageConverter.fromGrpcResponse(grpcSuccess);
                return MessageResult.success(successResponse);

            case ERROR:
                Message.ErrorResponse grpcError = grpcResult.getError();
                ErrorResponse errorResponse = messageConverter.fromGrpcResponse(grpcError);
                return MessageResult.error(errorResponse);

            case RESULT_NOT_SET:
            default:
                ErrorResponse unknownError = ErrorResponse.builder()
                        .errorCode(500)
                        .error("UNKNOWN_ERROR")
                        .message("No result received from gRPC service")
                        .timestamp(Instant.now())
                        .build();

                return MessageResult.error(unknownError);
        }
    }

}
