package com.messenger.media.service.dto.response

import com.messenger.media.service.domain.enums.MediaType

data class MediaResponse(
    val id: Long?,
    val mediaName: String,
    val url: String,
    val size: Long,
    val type: MediaType
)