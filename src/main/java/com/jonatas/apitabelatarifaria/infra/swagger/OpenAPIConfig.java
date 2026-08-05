package com.jonatas.apitabelatarifaria.infra.swagger;

import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openApi(BuildProperties properties) {
        return new OpenAPI().info(info(properties));
    }

    private Info info(BuildProperties properties) {
        return new Info()
                .title("API de Tabela Tarifária de Água")
                .version(properties.getVersion());
    }
}
