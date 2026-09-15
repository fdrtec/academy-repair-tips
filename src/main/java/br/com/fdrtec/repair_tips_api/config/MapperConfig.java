package br.com.fdrtec.repair_tips_api.config;

import br.com.fdrtec.repair_tips_api.mapper.GenericMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
            .findAndAddModules()
            .build();
    }

    @Bean
    public GenericMapper genericMapper(ObjectMapper objectMapper) {
        return () -> objectMapper;
    }
}