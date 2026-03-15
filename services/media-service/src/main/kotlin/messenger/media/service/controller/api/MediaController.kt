package messenger.media.service.controller.api

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import messenger.media.service.dto.response.MediaResponse
import messenger.media.service.service.MediaService
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/media")
@SecurityRequirement(name = "bearerAuth")
class MediaController(
    private val mediaService: MediaService
) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(@RequestParam("file") file: MultipartFile): ResponseEntity<MediaResponse> {
        val response: MediaResponse = mediaService.upload(file)

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{mediaId}")
    fun download(@PathVariable mediaId: Long): ResponseEntity<InputStreamResource> {
        val stream = mediaService.download(mediaId)

        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(InputStreamResource(stream))
    }
}