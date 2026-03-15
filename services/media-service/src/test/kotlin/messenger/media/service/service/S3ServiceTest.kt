package messenger.media.service.service

import messenger.media.service.config.S3Properties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.springframework.mock.web.MockMultipartFile
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*

@ExtendWith(MockitoExtension::class)
class S3ServiceTest {

    @Mock
    lateinit var s3Client: S3Client

    @Mock
    lateinit var s3Properties: S3Properties

    @InjectMocks
    lateinit var s3Service: S3Service

    @Test
    fun `uploadFile should call putObject with correct parameters`() {
        `when`(s3Properties.bucket).thenReturn("test-bucket")

        val fileContent = "Hello, S3!".toByteArray()
        val multipartFile = MockMultipartFile(
            "file",
            "hello.txt",
            "text/plain",
            fileContent
        )

        s3Service.uploadFile(multipartFile, "test-key")

        val requestCaptor = argumentCaptor<PutObjectRequest>()
        val bodyCaptor = argumentCaptor<RequestBody>()
        verify(s3Client).putObject(requestCaptor.capture(), bodyCaptor.capture())

        val request = requestCaptor.firstValue
        assertEquals("test-bucket", request.bucket())
        assertEquals("test-key", request.key())
        assertEquals("text/plain", request.contentType())
        assertEquals(fileContent.size.toLong(), request.contentLength())
    }

    @Test
    fun `downloadFile should call getObject with correct parameters`() {
        `when`(s3Properties.bucket).thenReturn("test-bucket")
        val key = "test-key"
        val responseInputStream = mock(ResponseInputStream::class.java) as ResponseInputStream<GetObjectResponse>
        `when`(s3Client.getObject(any<GetObjectRequest>())).thenReturn(responseInputStream)

        val result = s3Service.downloadFile(key)

        val requestCaptor = argumentCaptor<GetObjectRequest>()
        verify(s3Client).getObject(requestCaptor.capture())
        val request = requestCaptor.firstValue

        assertEquals("test-bucket", request.bucket())
        assertEquals(key, request.key())
        assertEquals(responseInputStream, result)
    }
}