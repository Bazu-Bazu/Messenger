package com.messenger.media.service.service

import com.messenger.media.service.config.S3Properties
import com.messenger.media.service.domain.entity.MediaMetadata
import com.messenger.media.service.domain.enums.MediaType
import com.messenger.media.service.domain.repository.MediaRepository
import com.messenger.media.service.dto.response.MediaResponse
import com.messenger.media.service.exception.FileEmptyException
import com.messenger.media.service.exception.FileSizeException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class MediaService(
    private val s3Service: S3Service,
    private val s3Properties: S3Properties,
    private val mediaRepository: MediaRepository,

    @Value("\${media.max-file-size}")
    private val MAX_FILE_SIZE: Long
) {

    @Transactional
    fun upload(file: MultipartFile): MediaResponse {
        validateFile(file)

        val key = "uploads/${UUID.randomUUID()}-${file.originalFilename}"

        s3Service.uploadFile(file, key)

        val url = "${s3Properties.endpoint}/${s3Properties.bucket}/$key"

        val metadata = MediaMetadata(
            mediaName = file.originalFilename ?: "file",
            url = url,
            size = file.size,
            type = resolveType(file.contentType)
        )

        val saved = mediaRepository.save(metadata)

        return createMediaResponse(saved)
    }

    private fun validateFile(file: MultipartFile) {
        if (file.isEmpty) {
            throw FileEmptyException("File is empty")
        }

        if (file.size > MAX_FILE_SIZE) {
            throw FileSizeException("File size is too large")
        }
    }

    private fun resolveType(contentType: String?): MediaType {
        return when {
            contentType?.startsWith("image") == true -> MediaType.IMAGE
            contentType?.startsWith("video") == true -> MediaType.VIDEO
            else -> MediaType.FILE
        }
    }

    private fun createMediaResponse(mediaMetadata: MediaMetadata): MediaResponse {
        return MediaResponse(
            id = mediaMetadata.id,
            url = mediaMetadata.url,
            size = mediaMetadata.size,
            type = mediaMetadata.type,
            mediaName = mediaMetadata.mediaName
        )
    }
}