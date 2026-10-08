package com.example.b4subvention;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class B4SubventionApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(B4SubventionApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}