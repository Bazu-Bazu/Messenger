package messenger.media.service.exception.mapper

import messenger.media.service.dto.response.ErrorResponse
import messenger.media.service.exception.FileEmptyException
import messenger.media.service.exception.FileSizeException
import messenger.media.service.exception.MediaNotFoundException
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class ErrorMapper {

    fun from(e: Exception): ErrorResponse {
        return ErrorResponse(
            errorCode = getErrorCode(e),
            error = e::class.simpleName,
            message = e.message,
            timestamp = Instant.now()
        )
    }

    private fun getErrorCode(e: Throwable): Int {
        return when (e) {
            is FileEmptyException -> 400
            is FileSizeException -> 400
            is MediaNotFoundException -> 404
            else -> 500
        }
    }
}