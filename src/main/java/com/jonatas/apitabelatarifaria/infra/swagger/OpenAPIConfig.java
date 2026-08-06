package com.jonatas.apitabelatarifaria.infra.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import java.util.Optional;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openApi(@Autowired(required = false) BuildProperties properties) {
        return new OpenAPI().info(info(properties));
    }

    private Info info(BuildProperties properties) {
        var version = Optional.ofNullable(properties)
                .map(BuildProperties::getVersion)
                .orElse("dev");
        return new Info()
                .title("API de Tabela Tarifária de Água")
                .version(version);
    }
}
