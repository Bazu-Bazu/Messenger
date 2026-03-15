package messenger.media.service.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Value("\${api-gateway.url}")
    private lateinit var apiGatewayUrl: String

    @Bean
    fun customOpenAPI(): OpenAPI {
        val server = Server()
            .url(apiGatewayUrl)
            .description("API Gateway")

        return OpenAPI()
            .info(
                Info()
                    .title("Media Service API")
                    .version("1.0")
            )
            .servers(listOf(server))
    }
}