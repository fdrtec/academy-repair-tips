package br.com.fdrtec.repair_tips_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI repairTipsOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Repair Tips API")
                .version("1.0.0")
                .description("API para gerenciamento de equipamentos, peças e suas relações de compatibilidade."))
            .addTagsItem(new Tag().name("Parts").description("Operações de peças"))
            .addTagsItem(new Tag().name("Equipaments").description("Operações de equipamentos"));
    }
}