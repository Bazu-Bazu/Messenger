package messenger.media.service.exception.handler

import messenger.media.service.dto.response.ErrorResponse
import messenger.media.service.exception.FileEmptyException
import messenger.media.service.exception.FileSizeException
import messenger.media.service.exception.MediaNotFoundException
import messenger.media.service.exception.mapper.ErrorMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler(
    private val errorMapper: ErrorMapper
) {

    @ExceptionHandler(
        FileEmptyException::class,
        FileSizeException::class
    )
    fun handleBadRequest(e: RuntimeException): ResponseEntity<ErrorResponse> {
        val response = errorMapper.from(e)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    @ExceptionHandler(MediaNotFoundException::class)
    fun handleNotFoundException(e: MediaNotFoundException): ResponseEntity<ErrorResponse> {
        val response = errorMapper.from(e)

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
    }
}