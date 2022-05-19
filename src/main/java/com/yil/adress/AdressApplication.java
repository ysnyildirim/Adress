package com.yil.adress;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.yil"})
@OpenAPIDefinition(info = @Info(title = "Adress Api", version = "1.0", description = "Yıldırım Information"))
@SpringBootApplication
public class AdressApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdressApplication.class, args);
    }

}
