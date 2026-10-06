package com.burkina.messenger.dto.request;

import com.burkina.common.enums.messenger.ChatType;
import com.burkina.common.enums.messenger.MessageType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendMessageRequest {

    private Long chatId;
    private ChatType chatType;
    private MessageType messageType;
    private String text;
    private Long mediaId;

    public void validate() {
        if (chatId == null) {
            throw new IllegalArgumentException("chatId is required");
        }
        if (chatType == null) {
            throw new IllegalArgumentException("chatType is required");
        }
        if (messageType == null) {
            throw new IllegalArgumentException("messageType is required");
        }
        if (messageType == MessageType.TEXT) {
            if (text == null || text.isBlank()) {
                throw new IllegalArgumentException(
                        "text is required for TEXT message"
                );
            }
            if (mediaId != null) {
                throw new IllegalArgumentException(
                        "mediaId must be null for TEXT message"
                );
            }
        } else {
            if (mediaId == null) {
                throw new IllegalArgumentException(
                        "mediaId is required for " + messageType
                );
            }

            if (text != null && !text.isBlank()) {
                throw new IllegalArgumentException(
                        "text must be null for " + messageType
                );
            }
        }
    }
}
