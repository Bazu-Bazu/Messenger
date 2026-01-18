package messenger.web.socket.service.converter;

import com.google.protobuf.Timestamp;
import enums.ChatType;
import enums.MessageType;
import dto.request.EditMessageRequest;
import dto.request.MarkMessageAsReadRequest;
import dto.request.SendMessageRequest;
import dto.response.ErrorResponse;
import dto.response.MessageResponse;
import message.Message;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class MessageConverter {

    public Message.SendMessageRequest toGrpcRequest(SendMessageRequest wsRequest, Long senderId) {
        return Message.SendMessageRequest.newBuilder()
                .setSenderId(senderId)
                .setChatId(wsRequest.chatId())
                .setChatType(convertChatType(wsRequest.chatType()))
                .setContent(wsRequest.content())
                .setMessageType(convertMessageType(wsRequest.messageType()))
                .build();
    }

    public Message.EditMessageRequest toGrpcRequest(EditMessageRequest wsRequest, Long editorId) {
        return Message.EditMessageRequest.newBuilder()
                .setEditorId(editorId)
                .setId(wsRequest.messageId())
                .setChatId(wsRequest.chatId())
                .setChatType(convertChatType(wsRequest.chatType()))
                .setContent(wsRequest.content())
                .setMessageType(convertMessageType(wsRequest.messageType()))
                .build();
    }

    public Message.MarkAsReadRequest toGrpcRequest(MarkMessageAsReadRequest wsRequest, Long readerId) {
        return Message.MarkAsReadRequest.newBuilder()
                .setReaderId(readerId)
                .setId(wsRequest.messageId())
                .setChatId(wsRequest.chatId())
                .setChatType(convertChatType(wsRequest.chatType()))
                .build();
    }

    public MessageResponse fromGrpcResponse(Message.MessageResponse grpcResponse) {
        return MessageResponse.builder()
                .id(grpcResponse.getId())
                .chatId(grpcResponse.getChatId())
                .chatType(convertChatType(grpcResponse.getChatType()))
                .senderId(grpcResponse.getSenderId())
                .content(grpcResponse.getContent())
                .messageType(convertMessageType(grpcResponse.getMessageType()))
                .createdAt(convertTimeStamp(grpcResponse.getCreatedAt()))
                .editedAt(convertTimeStamp(grpcResponse.getEditedAt()))
                .readAt(convertTimeStamp(grpcResponse.getReadAt()))
                .build();
    }

    public ErrorResponse fromGrpcResponse(Message.ErrorResponse grpcResponse) {
        return ErrorResponse.builder()
                .errorCode(grpcResponse.getErrorCode())
                .error(grpcResponse.getError())
                .message(grpcResponse.getMessage())
                .timestamp(convertTimeStamp(grpcResponse.getTimestamp()))
                .build();
    }

    private Message.ChatType convertChatType(ChatType chatType) {
        return Message.ChatType.valueOf(chatType.name());
    }

    private ChatType convertChatType(Message.ChatType chatType) {
        return ChatType.valueOf(chatType.name());
    }

    private Message.MessageType convertMessageType(MessageType messageType) {
        return Message.MessageType.valueOf(messageType.name());
    }

    private MessageType convertMessageType(Message.MessageType messageType) {
        return MessageType.valueOf(messageType.name());
    }

    private Instant convertTimeStamp(Timestamp timeStamp) {
        return Instant.ofEpochSecond(timeStamp.getSeconds(), timeStamp.getNanos());
    }

}
