package messenger.media.service.dto.response

import messenger.media.service.domain.enums.MediaType

data class MediaResponse(
    val id: Long?,
    val mediaName: String,
    val url: String,
    val size: Long,
    val type: MediaType
)