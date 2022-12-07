package com.viniciusaugusto.orderapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket apiDoc() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.viniciusaugusto.orderapi.controllers"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Orders API")
                .description("API to control clients, products and their orders.")
                .termsOfServiceUrl("https://example.io")
                .contact(new Contact("Vinicius Augusto",
                        "https://medium.com/@vinicius-augusto",
                        "vinicius_augusto_@msn.com"))
                .license("Apache License Version 2.0")
                .licenseUrl("https://example.io/EXAMPLE")
                .version("1.0")
                .build();
    }
}
