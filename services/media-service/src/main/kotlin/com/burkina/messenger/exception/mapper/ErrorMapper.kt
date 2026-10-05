package com.burkina.messenger.exception.mapper

import com.burkina.messenger.dto.response.ErrorResponse
import com.burkina.messenger.exception.FileEmptyException
import com.burkina.messenger.exception.FileSizeException
import com.burkina.messenger.exception.MediaNotFoundException
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