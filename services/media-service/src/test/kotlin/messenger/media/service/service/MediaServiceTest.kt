package messenger.media.service.service

import messenger.media.service.config.S3Properties
import messenger.media.service.domain.entity.MediaMetadata
import messenger.media.service.domain.enums.MediaType
import messenger.media.service.domain.repository.MediaRepository
import messenger.media.service.exception.FileEmptyException
import messenger.media.service.exception.FileSizeException
import messenger.media.service.exception.MediaNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.mock.web.MockMultipartFile
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.services.s3.model.GetObjectResponse
import java.util.*
import kotlin.test.assertFailsWith

@ExtendWith(MockitoExtension::class)
class MediaServiceTest {

    @Mock
    lateinit var s3Service: S3Service

    @Mock
    lateinit var s3Properties: S3Properties

    @Mock
    lateinit var mediaRepository: MediaRepository

    @InjectMocks
    lateinit var mediaService: MediaService

    private val maxFileSizeBytes = 10_000L

    @BeforeEach
    fun setup() {
        val maxFileSize = org.springframework.util.unit.DataSize.ofBytes(maxFileSizeBytes)
        val field = MediaService::class.java.getDeclaredField("maxFileSize")
        field.isAccessible = true
        field.set(mediaService, maxFileSize)
    }

    @Test
    fun `upload should save file and return response`() {
        `when`(s3Properties.bucket).thenReturn("test-bucket")
        `when`(s3Properties.endpoint).thenReturn("http://localhost:9000")

        val file = MockMultipartFile(
            "file",
            "test.jpg",
            "image/jpeg",
            "Hello".toByteArray()
        )

        val savedMetadata = MediaMetadata(
            id = 1L,
            mediaName = "test.jpg",
            url = "http://localhost/test-bucket/uploads/test-key",
            size = file.size,
            type = MediaType.IMAGE
        )

        `when`(mediaRepository.save(any(MediaMetadata::class.java))).thenReturn(savedMetadata)

        val response = mediaService.upload(file)

        assertEquals(1L, response.id)
        assertEquals("test.jpg", response.mediaName)
        assertEquals(MediaType.IMAGE, response.type)
        assertEquals(file.size, response.size)
        assert(response.url.contains("uploads/"))
        verify(mediaRepository).save(any(MediaMetadata::class.java))
    }

    @Test
    fun `upload should throw FileEmptyException for empty file`() {
        val emptyFile = MockMultipartFile(
            "file",
            "empty.txt",
            "text/plain",
            ByteArray(0)
        )

        assertFailsWith<FileEmptyException> {
            mediaService.upload(emptyFile)
        }
    }

    @Test
    fun `upload should throw FileSizeException for too large file`() {
        val largeFile = MockMultipartFile(
            "file",
            "large.txt",
            "text/plain",
            ByteArray((maxFileSizeBytes + 1).toInt())
        )

        assertFailsWith<FileSizeException> {
            mediaService.upload(largeFile)
        }
    }

    @Test
    fun `download should call s3Service with correct key`() {
        val metadata = MediaMetadata(
            id = 1L,
            mediaName = "file.txt",
            url = "http://localhost:9000/test-bucket/uploads/file.txt",
            size = 10,
            type = MediaType.FILE
        )

        `when`(s3Properties.bucket).thenReturn("test-bucket")
        `when`(mediaRepository.findById(1L)).thenReturn(Optional.of(metadata))
        val responseInputStream = mock(ResponseInputStream::class.java) as ResponseInputStream<GetObjectResponse>
        `when`(s3Service.downloadFile(anyString())).thenReturn(responseInputStream)

        val result = mediaService.download(1L)

        assertEquals(responseInputStream, result)
        verify(s3Service).downloadFile("uploads/file.txt")
    }

    @Test
    fun `download should throw MediaNotFoundException for missing media`() {
        `when`(mediaRepository.findById(1L)).thenReturn(Optional.empty())
        assertFailsWith<MediaNotFoundException> {
            mediaService.download(1L)
        }
    }
}