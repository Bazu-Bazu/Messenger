package com.burkina.messenger.exception.handler

import com.burkina.messenger.dto.response.ErrorResponse
import com.burkina.messenger.exception.FileEmptyException
import com.burkina.messenger.exception.FileSizeException
import com.burkina.messenger.exception.MediaNotFoundException
import com.burkina.messenger.exception.mapper.ErrorMapper
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