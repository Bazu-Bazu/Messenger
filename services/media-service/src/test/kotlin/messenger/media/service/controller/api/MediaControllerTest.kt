package messenger.media.service.controller.api

import messenger.media.service.dto.response.MediaResponse
import messenger.media.service.service.MediaService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.services.s3.model.GetObjectResponse

@ExtendWith(MockitoExtension::class)
class MediaControllerTest {

    @Mock
    lateinit var mediaService: MediaService

    @InjectMocks
    lateinit var mediaController: MediaController

    private val mockMvc: MockMvc by lazy {
        MockMvcBuilders.standaloneSetup(mediaController).build()
    }

    @Test
    fun `upload should return CREATED and MediaResponse`() {
        val file = MockMultipartFile(
            "file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "Hello".toByteArray()
        )

        val mediaResponse = MediaResponse(
            id = 1L,
            url = "http://localhost/uploads/test.jpg",
            size = file.size,
            type = messenger.media.service.domain.enums.MediaType.IMAGE,
            mediaName = "test.jpg"
        )

        `when`(mediaService.upload(file)).thenReturn(mediaResponse)

        mockMvc.perform(multipart("/media")
            .file(file)
            .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.mediaName").value("test.jpg"))
            .andExpect(jsonPath("$.type").value("IMAGE"))
    }

    @Test
    fun `download should return OK and InputStreamResource`() {
        val mediaId = 1L
        val responseInputStream = mock(ResponseInputStream::class.java) as ResponseInputStream<GetObjectResponse>
        `when`(mediaService.download(mediaId)).thenReturn(responseInputStream)

        mockMvc.perform(get("/media/$mediaId"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
    }
}