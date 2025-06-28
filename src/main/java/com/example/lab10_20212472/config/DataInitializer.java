package com.example.lab10_20212472.config;

import com.example.lab10_20212472.service.BuscaminasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private BuscaminasService buscaminasService;
    
    @Override
    public void run(String... args) throws Exception {
        // Inicializar el juego con la configuración por defecto
        buscaminasService.inicializarJuego();
    }
}
