package com.thd.ecommercespringmvc;

import com.thd.ecommercespringmvc.database.DatabaseConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Application implements WebMvcConfigurer {
    public static DatabaseConnection database;
    public static void main(String[] args) {
        database = new DatabaseConnection();
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/public_img/**")
                .addResourceLocations("file:public/")
                .setCachePeriod(0);
    }
}
