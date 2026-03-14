package com.messenger.media.service.domain.repository

import com.messenger.media.service.domain.entity.MediaMetadata
import org.springframework.data.jpa.repository.JpaRepository

interface MediaRepository : JpaRepository<MediaMetadata, Long>