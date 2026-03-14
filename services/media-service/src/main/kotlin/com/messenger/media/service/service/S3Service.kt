package com.messenger.media.service.service

import com.messenger.media.service.config.S3Properties
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Service
class S3Service(
    private val s3Client: S3Client,
    private val s3Properties: S3Properties,
) {

    fun uploadFile(file: MultipartFile, key: String) {
        val request = PutObjectRequest.builder()
            .bucket(s3Properties.bucket)
            .key(key)
            .contentType(file.contentType)
            .contentLength(file.size)
            .build()

        s3Client.putObject(request, RequestBody.fromInputStream(file.inputStream, file.size))
    }
}