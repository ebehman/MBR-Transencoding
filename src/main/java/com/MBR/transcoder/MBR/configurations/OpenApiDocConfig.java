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
        return new OpenAPI().addServersItem(new Server().url("http://" + "localhost"+"/Api/"))
                .info(new Info().title("RPD Service Manager APIs").version("23.2.0")
                        .license(new License().name("Cisco Systems, Inc.").url("https://www.cisco.com")));
    }
}

