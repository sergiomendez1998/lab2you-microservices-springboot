package com.example.finalprojectbackend;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class FinalProjectBackendApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {

        SpringApplication.run(FinalProjectBackendApplication.class, args);

    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FinalProjectBackendApplication.class);
    }

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("lab2you app Backend")
                        .version("1.0.0")
                        .description("documentation about endpoints available in lab2you app backend")
                        .termsOfService("https://swagger.io/terms")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")));
    }
}
