package messenger.media.service.dto.response

import java.time.Instant

data class ErrorResponse(
    val errorCode: Int,
    val error: String?,
    val message: String?,
    val timestamp: Instant
)
