package com.burkina.messenger.domain.repository

import com.burkina.messenger.domain.entity.MediaMetadata
import org.springframework.data.jpa.repository.JpaRepository

interface MediaRepository : JpaRepository<MediaMetadata, Long>