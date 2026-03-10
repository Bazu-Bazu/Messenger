package messenger.message.service.controller.grpc;

import converter.MessageConverter;
import dto.request.MarkMessageAsReadRequest;
import dto.request.SendMessageRequest;
import dto.response.ErrorResponse;
import dto.response.MessageResponse;
import io.grpc.stub.StreamObserver;
import message.Message;
import messenger.message.service.exception.mapper.ErrorMapper;
import messenger.message.service.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageGrpcServerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private MessageConverter messageConverter;

    @Mock
    private ErrorMapper errorMapper;

    @Mock
    private StreamObserver<Message.MessageResult> responseObserver;

    @Mock
    private Message.SendMessageRequest grpcSendRequest;

    @Mock
    private Message.MarkAsReadRequest grpcMarkRequest;

    @Mock
    private SendMessageRequest sendMessageRequest;

    @Mock
    private MarkMessageAsReadRequest markMessageAsReadRequest;

    @Mock
    private MessageResponse messageResponse;

    @Mock
    private message.Message.MessageResponse grpcMessageResponse;

    @Mock
    private ErrorResponse errorResponse;

    @Mock
    private Message.ErrorResponse grpcErrorResponse;

    @InjectMocks
    private MessageGrpcServer grpcServer;

    @Test
    void sendMessage_success_shouldCallServiceAndReturnGrpcResponse() {
        when(grpcSendRequest.getSenderId()).thenReturn(1L);
        when(messageConverter.fromGrpcRequest(grpcSendRequest)).thenReturn(sendMessageRequest);
        when(messageService.sendMessage(sendMessageRequest, 1L)).thenReturn(messageResponse);
        when(messageConverter.toGrpcResponse(messageResponse)).thenReturn(grpcMessageResponse);

        grpcServer.sendMessage(grpcSendRequest, responseObserver);

        ArgumentCaptor<Message.MessageResult> captor = ArgumentCaptor.forClass(Message.MessageResult.class);
        verify(responseObserver).onNext(captor.capture());
        verify(responseObserver).onCompleted();

        Message.MessageResult result = captor.getValue();
        assertEquals(grpcMessageResponse, result.getSuccess());
    }

    @Test
    void sendMessage_exception_shouldCallSendError() {
        when(grpcSendRequest.getSenderId()).thenReturn(1L);
        when(messageConverter.fromGrpcRequest(grpcSendRequest)).thenReturn(sendMessageRequest);
        when(messageService.sendMessage(sendMessageRequest, 1L))
                .thenThrow(new RuntimeException("fail"));

        when(errorMapper.from(any())).thenReturn(errorResponse);
        when(messageConverter.toGrpcResponse(errorResponse)).thenReturn(grpcErrorResponse);

        grpcServer.sendMessage(grpcSendRequest, responseObserver);

        ArgumentCaptor<Message.MessageResult> captor = ArgumentCaptor.forClass(Message.MessageResult.class);
        verify(responseObserver).onNext(captor.capture());
        verify(responseObserver).onCompleted();

        Message.MessageResult result = captor.getValue();
        assertEquals(grpcErrorResponse, result.getError());
    }

    @Test
    void markAsRead_success_shouldCallServiceAndReturnGrpcResponse() {
        when(grpcMarkRequest.getReaderId()).thenReturn(2L);
        when(messageConverter.fromGrpcRequest(grpcMarkRequest)).thenReturn(markMessageAsReadRequest);
        when(messageService.markMessageAsRead(2L, markMessageAsReadRequest)).thenReturn(messageResponse);
        when(messageConverter.toGrpcResponse(messageResponse)).thenReturn(grpcMessageResponse);

        grpcServer.markAsRead(grpcMarkRequest, responseObserver);

        ArgumentCaptor<Message.MessageResult> captor = ArgumentCaptor.forClass(Message.MessageResult.class);
        verify(responseObserver).onNext(captor.capture());
        verify(responseObserver).onCompleted();

        Message.MessageResult result = captor.getValue();
        assertEquals(grpcMessageResponse, result.getSuccess());
    }

    @Test
    void markAsRead_exception_shouldCallSendError() {
        when(grpcMarkRequest.getReaderId()).thenReturn(2L);
        when(messageConverter.fromGrpcRequest(grpcMarkRequest)).thenReturn(markMessageAsReadRequest);
        when(messageService.markMessageAsRead(2L, markMessageAsReadRequest))
                .thenThrow(new RuntimeException("fail"));

        when(errorMapper.from(any())).thenReturn(errorResponse);
        when(messageConverter.toGrpcResponse(errorResponse)).thenReturn(grpcErrorResponse);

        grpcServer.markAsRead(grpcMarkRequest, responseObserver);

        ArgumentCaptor<Message.MessageResult> captor = ArgumentCaptor.forClass(Message.MessageResult.class);
        verify(responseObserver).onNext(captor.capture());
        verify(responseObserver).onCompleted();

        Message.MessageResult result = captor.getValue();
        assertEquals(grpcErrorResponse, result.getError());
    }
}