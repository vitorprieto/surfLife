package com.br.originalTruta.surfLife.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI surfLifeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SurfLife API")
                        .description("API do MVP SurfLife para surf, atividades e alertas.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Vitor Prieto")
                                .email("vitor.originaltruta@gmail.com"))
                        .license(new License()
                                .name("Private API")
                                .url("https://github.com/vitorprieto/surfLife")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Ambiente local")
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositório do projeto")
                        .url("https://github.com/vitorprieto/surfLife"));
    }
}
