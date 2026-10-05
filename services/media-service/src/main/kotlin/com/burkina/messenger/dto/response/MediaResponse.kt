package com.burkina.messenger.dto.response

import com.burkina.messenger.domain.enums.MediaType

data class MediaResponse(
    val id: Long?,
    val mediaName: String,
    val url: String,
    val size: Long,
    val type: MediaType
)