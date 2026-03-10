package messenger.message.service.controller.grpc;

import converter.MessageConverter;
import dto.request.MarkMessageAsReadRequest;
import dto.request.SendMessageRequest;
import dto.response.ErrorResponse;
import dto.response.MessageResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import message.Message;
import message.MessageServiceGrpc;
import messenger.message.service.exception.mapper.ErrorMapper;
import messenger.message.service.service.MessageService;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class MessageGrpcServer extends MessageServiceGrpc.MessageServiceImplBase {

    private final MessageService messageService;
    private final MessageConverter messageConverter;
    private final ErrorMapper errorMapper;

    @Override
    public void sendMessage(
            Message.SendMessageRequest grpcRequest,
            StreamObserver<Message.MessageResult> responseObserver
    ) {
        try {
            SendMessageRequest request = messageConverter.fromGrpcRequest(grpcRequest);
            Long senderId = grpcRequest.getSenderId();

            MessageResponse messageResponse = messageService.sendMessage(request, senderId);
            sendSuccess(messageResponse, responseObserver);
        } catch (Exception e) {
            sendError(e, responseObserver);
        }
    }

    @Override
    public void markAsRead(
            Message.MarkAsReadRequest grpcRequest,
            StreamObserver<Message.MessageResult> responseObserver
    ) {
        try {
            MarkMessageAsReadRequest request = messageConverter.fromGrpcRequest(grpcRequest);
            Long readerId = grpcRequest.getReaderId();

            MessageResponse messageResponse = messageService.markMessageAsRead(readerId, request);
            sendSuccess(messageResponse, responseObserver);
        } catch (Exception e) {
            sendError(e, responseObserver);
        }
    }

    private void sendSuccess(
            MessageResponse messageResponse,
            StreamObserver<Message.MessageResult> responseObserver
    ) {
        Message.MessageResponse grpcMessageResponse = messageConverter.toGrpcResponse(messageResponse);
        Message.MessageResult grpcMessageResult = Message.MessageResult.newBuilder()
                .setSuccess(grpcMessageResponse)
                .build();

        responseObserver.onNext(grpcMessageResult);
        responseObserver.onCompleted();
    }

    private void sendError(
            Exception e,
            StreamObserver<Message.MessageResult> responseObserver
    ) {
        ErrorResponse errorResponse = errorMapper.from(e);
        Message.ErrorResponse grpcErrorResponse = messageConverter.toGrpcResponse(errorResponse);
        Message.MessageResult grpcMessageResult = Message.MessageResult.newBuilder()
                .setError(grpcErrorResponse)
                .build();

        responseObserver.onNext(grpcMessageResult);
        responseObserver.onCompleted();
    }
}
