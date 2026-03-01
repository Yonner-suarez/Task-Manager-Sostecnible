package com.sostecnible.TaskManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class TaskManagerApplication {

  public static void main(String[] args) {
      
        Dotenv dotenv = Dotenv.load();
        
        //Cargar varables esntorno para conexion DB
        System.setProperty("spring.datasource.url", dotenv.get("DB_URL"));
        System.setProperty("spring.datasource.username", dotenv.get("DB_USER"));
        System.setProperty("spring.datasource.password", dotenv.get("DB_PASSWORD"));
        
        System.setProperty("FRONTEND_URL", dotenv.get("FRONTEND_URL"));

        SpringApplication.run(TaskManagerApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(@Value("${FRONTEND_URL}") String frontendUrl) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") 
                        .allowedOrigins(frontendUrl) 
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}