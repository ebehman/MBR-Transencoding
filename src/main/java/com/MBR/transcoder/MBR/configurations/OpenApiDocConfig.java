package com.MBR.transcoder.MBR.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiDocConfig {
    private static final String INGRESS_HOST_IP = "INGRESS_HOST_IP";

    @Bean
    public OpenAPI customOpenAPI() {
       // String hostIp = CommonUtil.getEnvValue(INGRESS_HOST_IP);
        return new OpenAPI().addServersItem(new Server().url("http://" + "localhost/"))
                .info(new Info().title("MBR Transencoding Manager APIs").version("1.0.0")
                        .license(new License().name("Manas.xyz.com, Inc.").url("https://manas.xyz.com")));
    }
}

