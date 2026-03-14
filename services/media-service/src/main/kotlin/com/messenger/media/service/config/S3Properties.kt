package com.messenger.media.service.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "aws.s3")
data class S3Properties(
    val keyId: String,
    val secretKey: String,
    val bucket: String,
    val region: String,
    val endpoint: String,
)
