package converter;

import com.google.protobuf.Timestamp;
import dto.request.SendMessageRequest;
import enums.ChatType;
import enums.MessageType;
import dto.request.MarkMessageAsReadRequest;
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

    public SendMessageRequest fromGrpcRequest(Message.SendMessageRequest grpcRequest) {
        return SendMessageRequest.builder()
                .chatId(grpcRequest.getChatId())
                .content(grpcRequest.getContent())
                .chatType(convertChatType(grpcRequest.getChatType()))
                .messageType(convertMessageType(grpcRequest.getMessageType()))
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

    public MarkMessageAsReadRequest fromGrpcRequest(Message.MarkAsReadRequest grpcRequest) {
        return MarkMessageAsReadRequest.builder()
                .messageId(grpcRequest.getId())
                .chatId(grpcRequest.getChatId())
                .chatType(convertChatType(grpcRequest.getChatType()))
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
                .createdAt(toInstant(grpcResponse.getCreatedAt()))
                .readAt(toInstant(grpcResponse.getReadAt()))
                .build();
    }

    public Message.MessageResponse toGrpcResponse(MessageResponse response) {
        Message.MessageResponse.Builder builder = Message.MessageResponse.newBuilder()
                .setId(response.id())
                .setChatId(response.chatId())
                .setChatType(convertChatType(response.chatType()))
                .setSenderId(response.senderId())
                .setContent(response.content());

        if (response.createdAt() != null) {
            builder.setCreatedAt(toTimestamp(response.createdAt()));
        }

        if (response.readAt() != null) {
            builder.setReadAt(toTimestamp(response.readAt()));
        }

        return builder.build();
    }

    public ErrorResponse fromGrpcResponse(Message.ErrorResponse grpcResponse) {
        return ErrorResponse.builder()
                .errorCode(grpcResponse.getErrorCode())
                .error(grpcResponse.getError())
                .message(grpcResponse.getMessage())
                .timestamp(toInstant(grpcResponse.getTimestamp()))
                .build();
    }

    public Message.ErrorResponse toGrpcResponse(ErrorResponse response) {
        Message.ErrorResponse.Builder builder = Message.ErrorResponse.newBuilder()
                .setErrorCode(response.errorCode())
                .setError(response.error())
                .setMessage(response.error());

        if (response.timestamp() != null) {
            builder.setTimestamp(toTimestamp(response.timestamp()));
        }

        return builder.build();
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

    private Instant toInstant(Timestamp timeStamp) {
        return Instant.ofEpochSecond(timeStamp.getSeconds(), timeStamp.getNanos());
    }

    private Timestamp toTimestamp(Instant instant) {
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}
