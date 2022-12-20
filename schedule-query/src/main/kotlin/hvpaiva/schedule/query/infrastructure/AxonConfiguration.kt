package hvpaiva.schedule.query.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import org.axonframework.serialization.Serializer
import org.axonframework.serialization.json.JacksonSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class AxonConfiguration {
    @Bean
    @Primary
    fun axonJacksonSerializer(objectMapper: ObjectMapper): Serializer =
        JacksonSerializer.builder()
            .objectMapper(objectMapper)
            .defaultTyping()
            .build()
}