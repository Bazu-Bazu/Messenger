package messenger.media.service.domain.entity

import messenger.media.service.domain.enums.MediaType
import jakarta.persistence.*

@Entity
@Table(name = "media_metadata")
data class MediaMetadata(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val mediaName: String,

    @Column(nullable = false, unique = true)
    val url: String,

    @Column(nullable = false)
    val size: Long,

    @Enumerated(EnumType.STRING)
    val type: MediaType
)