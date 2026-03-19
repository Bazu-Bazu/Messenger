package converter;

import com.google.protobuf.Timestamp;
import dto.payload.MediaPayload;
import dto.payload.TextPayload;
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
        var builder = Message.SendMessageRequest.newBuilder()
                .setSenderId(senderId)
                .setChatId(wsRequest.chatId())
                .setChatType(convertChatType(wsRequest.chatType()))
                .setMessageType(convertMessageType(wsRequest.messageType()));

        var payload = wsRequest.payload();

        if (payload instanceof TextPayload t) {
            builder.setText(t.text());
        } else if (payload instanceof MediaPayload m) {
            builder.setMediaId(m.mediaId());
        } else {
            throw new IllegalArgumentException("Unknow payload");
        }

        return builder.build();
    }

    public SendMessageRequest fromGrpcRequest(Message.SendMessageRequest grpcRequest) {
        var builder = SendMessageRequest.builder()
                .chatId(grpcRequest.getChatId())
                .chatType(convertChatType(grpcRequest.getChatType()))
                .messageType(convertMessageType(grpcRequest.getMessageType()));

        var payload =  grpcRequest.getPayloadCase();

        switch (payload) {
            case TEXT -> builder.payload(new TextPayload(grpcRequest.getText()));
            case MEDIA_ID -> builder.payload(new MediaPayload(grpcRequest.getMediaId()));
            case PAYLOAD_NOT_SET -> throw new IllegalArgumentException("Payload not set");
        }

        return builder.build();
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
        var builder = MessageResponse.builder()
                .id(grpcResponse.getId())
                .chatId(grpcResponse.getChatId())
                .chatType(convertChatType(grpcResponse.getChatType()))
                .senderId(grpcResponse.getSenderId())
                .messageType(convertMessageType(grpcResponse.getMessageType()))
                .createdAt(toInstant(grpcResponse.getCreatedAt()))
                .readAt(toInstant(grpcResponse.getReadAt()));

        var payload =  grpcResponse.getPayloadCase();

        switch (payload) {
            case TEXT -> builder.payload(new TextPayload(grpcResponse.getText()));
            case MEDIA_ID -> builder.payload(new MediaPayload(grpcResponse.getMediaId()));
            case PAYLOAD_NOT_SET -> throw new IllegalArgumentException("Payload not set");
        }

        return builder.build();
    }

    public Message.MessageResponse toGrpcResponse(MessageResponse response) {
        var builder = Message.MessageResponse.newBuilder()
                .setId(response.id())
                .setChatId(response.chatId())
                .setChatType(convertChatType(response.chatType()))
                .setSenderId(response.senderId());

        if (response.createdAt() != null) {
            builder.setCreatedAt(toTimestamp(response.createdAt()));
        }
        if (response.readAt() != null) {
            builder.setReadAt(toTimestamp(response.readAt()));
        }

        var payload = response.payload();

        if (payload instanceof TextPayload t) {
            builder.setText(t.text());
        } else if (payload instanceof MediaPayload m) {
            builder.setMediaId(m.mediaId());
        } else {
            throw new IllegalArgumentException("Unknow payload");
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
