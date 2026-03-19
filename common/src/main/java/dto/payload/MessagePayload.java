package dto.payload;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextPayload.class, name = "TEXT"),
        @JsonSubTypes.Type(value = MediaPayload.class, name = "MEDIA")
})
public sealed interface MessagePayload
    permits TextPayload, MediaPayload {}
