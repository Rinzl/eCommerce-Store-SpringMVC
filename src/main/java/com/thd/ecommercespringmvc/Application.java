package com.thd.ecommercespringmvc;

import com.thd.ecommercespringmvc.database.DatabaseConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static DatabaseConnection database;
    public static void main(String[] args) {
        database = new DatabaseConnection();
        SpringApplication.run(Application.class, args);
    }
}
