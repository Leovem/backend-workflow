package com.workflow.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbChecker {

    @Bean
    CommandLineRunner checkConnection(MongoTemplate mongoTemplate) {
        return args -> {
            try {
                // Intenta obtener el nombre de la base de datos
                String dbName = mongoTemplate.getDb().getName();
                System.out.println("--- ✅ CONEXIÓN EXITOSA ---");
                System.out.println("Base de datos activa: " + dbName);
            } catch (Exception e) {
                System.out.println("--- ❌ ERROR DE CONEXIÓN ---");
                System.err.println("Motivo: " + e.getMessage());
            }
        };
    }
}