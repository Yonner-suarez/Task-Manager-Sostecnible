package com.sostecnible.TaskManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sostecnible.TaskManager.infraestructure.security.JwtAuthenticationFilter;

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
    // Configuración Security
    @Configuration
    @EnableWebSecurity
    public static class SecurityConfig {

        @Autowired
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                .cors(Customizer.withDefaults()) // Activa CORS
                .csrf(csrf -> csrf.disable())    // Desactiva CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .authorizeHttpRequests(auth -> auth
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll() 
                  .requestMatchers("/user/**").permitAll() 
                  .anyRequest().authenticated()
                );

            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }
    }
} 