package com.MBR.transcoder.MBR.configurations;

/*
 * Copyright © 2017 Cisco Systems.  All rights reserved.
 *
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
@Import(SpringDataRestConfiguration.class)
/**
 * Swagger configuration to specify basic mapping,
 * header and title information
 */

public class SwaggerConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(SwaggerConfiguration.class);
    @Bean
    public Docket docket(){
        Docket transcoder = new Docket(DocumentationType.SWAGGER_2)
                .groupName("MBR-Transencoding")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/Api*"))
                .build();
        return transcoder;
    }
    /**
     * Custom Header information
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MBR transencoding APIs")
                .description("Tansencoding APIs")
                .version("1.0.0")
                .contact(new Contact("ManasRanjan", "https://manas.xyz.com", "bapani094@gmail.com"))
                .license("bapani094@gmail.com")
                .licenseUrl("https://manas.xyz.com")
                .build();
    }
}

