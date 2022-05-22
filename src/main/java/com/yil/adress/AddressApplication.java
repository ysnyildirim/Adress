package com.yil.adress;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.yil"})
@OpenAPIDefinition(info = @Info(title = "Address Api", version = "1.0", description = "Yıldırım Information"))
@SpringBootApplication
public class AddressApplication {

    public static void main(String[] args) {
        SpringApplication.run(AddressApplication.class, args);
    }

}
