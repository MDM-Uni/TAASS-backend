package com.example.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.Objects;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class ApiGatewayApplication {

   private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

   @Autowired
   private GatewayProperties props;


   public static void main(String[] args) {
      SpringApplication.run(ApiGatewayApplication.class, args);
   }

   @PostConstruct
   public void init() {
      logger.info(Objects.toString(props));
   }

   @Bean
   public WebMvcConfigurer corsConfigurer() {
      return new WebMvcConfigurer() {
         @Override
         public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**").allowedOrigins("*");
         }
      };
   }
}
