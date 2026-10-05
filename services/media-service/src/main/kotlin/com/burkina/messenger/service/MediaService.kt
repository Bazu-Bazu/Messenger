package com.burkina.messenger.service

import com.burkina.messenger.config.S3Properties
import com.burkina.messenger.domain.entity.MediaMetadata
import com.burkina.messenger.domain.enums.MediaType
import com.burkina.messenger.domain.repository.MediaRepository
import com.burkina.messenger.dto.response.MediaResponse
import com.burkina.messenger.exception.FileEmptyException
import com.burkina.messenger.exception.FileSizeException
import com.burkina.messenger.exception.MediaNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.unit.DataSize
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.services.s3.model.GetObjectResponse
import java.util.UUID

@Service
class MediaService(
    private val s3Service: S3Service,
    private val s3Properties: S3Properties,
    private val mediaRepository: MediaRepository,
) {

    @Value("\${media.max-file-size}")
    private lateinit var maxFileSize: DataSize

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

    fun download(mediaId: Long): ResponseInputStream<GetObjectResponse> {
        val metadata = findMetadataById(mediaId)

        val key = metadata.url.substringAfter("${s3Properties.bucket}/")

        return s3Service.downloadFile(key)
    }

    private fun validateFile(file: MultipartFile) {
        if (file.isEmpty) {
            throw FileEmptyException("File is empty")
        }

        if (file.size > maxFileSize.toBytes()) {
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

    private fun findMetadataById(mediaId: Long): MediaMetadata {
        return mediaRepository.findById(mediaId)
            .orElseThrow() {
                MediaNotFoundException(
                    String.format("Media %d not found", mediaId))
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