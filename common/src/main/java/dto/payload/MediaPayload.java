package dto.payload;

import lombok.Builder;

@Builder
public record MediaPayload(
        Long mediaId
) implements MessagePayload {}
