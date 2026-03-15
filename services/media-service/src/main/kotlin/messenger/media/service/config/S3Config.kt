package messenger.media.service.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

@Configuration
class S3Config(
    private val s3Properties: S3Properties
) {

    @Bean
    fun s3Client() : S3Client {
        val credentials = AwsBasicCredentials.create(s3Properties.keyId, s3Properties.secretKey)

        return S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .region(Region.of(s3Properties.region))
            .endpointOverride(URI.create(s3Properties.endpoint))
            .build()
    }
}