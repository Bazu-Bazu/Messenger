package messenger.message.service.config;

import converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConverterConfig {

    @Bean
    public MessageConverter getMessageConverter() {
        return new MessageConverter();
    }
}
