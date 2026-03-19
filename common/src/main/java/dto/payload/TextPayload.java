package dto.payload;

import lombok.Builder;

@Builder
public record TextPayload(
        String text
) implements MessagePayload {}
