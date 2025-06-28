package com.example.lab10_20212472.service;

import com.example.lab10_20212472.entity.Configuracion;
import com.example.lab10_20212472.entity.Movimiento;
import com.example.lab10_20212472.entity.PosicionBomba;
import com.example.lab10_20212472.repository.ConfiguracionRepository;
import com.example.lab10_20212472.repository.MovimientoRepository;
import com.example.lab10_20212472.repository.PosicionBombaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuscaminasService {
    
    @Autowired
    private ConfiguracionRepository configuracionRepository;
    
    @Autowired
    private PosicionBombaRepository posicionBombaRepository;
    
    @Autowired
    private MovimientoRepository movimientoRepository;
    
    public Configuracion obtenerConfiguracion() {
        List<Configuracion> configs = configuracionRepository.findAll();
        if (configs.isEmpty()) {
            // Crear configuración por defecto según las imágenes
            Configuracion config = new Configuracion(null, 6, 6, 5, 2, 2);
            return configuracionRepository.save(config);
        }
        return configs.get(0);
    }
    
    public void inicializarJuego() {
        // Limpiar movimientos previos
        movimientoRepository.deleteAll();
        
        // Reiniciar intentos
        Configuracion config = obtenerConfiguracion();
        config.setCantIntentosActual(config.getCantIntentos());
        configuracionRepository.save(config);
        
        // Crear bombas según la configuración de las imágenes
        posicionBombaRepository.deleteAll();
        Long idMina = config.getIdMina();
        
        // Bombas según la tabla mostrada en las imágenes
        posicionBombaRepository.save(new PosicionBomba(null, 3, 2, idMina)); // Bomba 1
        posicionBombaRepository.save(new PosicionBomba(null, 4, 6, idMina)); // Bomba 2
        posicionBombaRepository.save(new PosicionBomba(null, 5, 4, idMina)); // Bomba 3
        posicionBombaRepository.save(new PosicionBomba(null, 4, 5, idMina)); // Bomba 4
        posicionBombaRepository.save(new PosicionBomba(null, 6, 2, idMina)); // Bomba 5
    }
    
    public String realizarMovimiento(int x, int y) {
        Configuracion config = obtenerConfiguracion();
        Long idMina = config.getIdMina();
        
        // Verificar si ya fue jugada esta posición
        Movimiento movimientoExistente = movimientoRepository.findByCoordenadaXAndCoordenadaYAndIdMina(x, y, idMina);
        if (movimientoExistente != null && movimientoExistente.getDescubierta()) {
            return "Ya fue descubierta esta posición";
        }
        
        // Verificar si es bomba
        PosicionBomba bomba = posicionBombaRepository.findByCoordenadaXAndCoordenadaYAndIdMina(x, y, idMina);
        boolean esBomba = bomba != null;
        
        if (esBomba) {
            // Es bomba, reducir intentos
            config.setCantIntentosActual(config.getCantIntentosActual() - 1);
            configuracionRepository.save(config);
            
            // Guardar movimiento
            Movimiento movimiento = new Movimiento(null, x, y, idMina, true, true, 0);
            movimientoRepository.save(movimiento);
            
            if (config.getCantIntentosActual() <= 0) {
                return "¡Usted ha perdido el juego!";
            } else {
                return "Encontró una bomba, le queda(n) " + config.getCantIntentosActual() + " intento(s)!";
            }
        } else {
            // No es bomba, calcular números vecinos
            int numeroVecinas = contarBombasVecinas(x, y, idMina);
            
            // Guardar movimiento
            Movimiento movimiento = new Movimiento(null, x, y, idMina, false, true, numeroVecinas);
            movimientoRepository.save(movimiento);
            
            // Si no hay bombas vecinas, expandir automáticamente
            if (numeroVecinas == 0) {
                expandirCasillasVecinas(x, y, idMina);
            }
            
            // Verificar si ganó
            if (verificarVictoria(idMina)) {
                return "¡Usted ha ganado el juego!";
            }
            
            return "Casilla descubierta";
        }
    }
    
    private int contarBombasVecinas(int x, int y, Long idMina) {
        int contador = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i == x && j == y) continue;
                if (i < 1 || i > 6 || j < 1 || j > 6) continue;
                
                PosicionBomba bomba = posicionBombaRepository.findByCoordenadaXAndCoordenadaYAndIdMina(i, j, idMina);
                if (bomba != null) {
                    contador++;
                }
            }
        }
        return contador;
    }
    
    private void expandirCasillasVecinas(int x, int y, Long idMina) {
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i == x && j == y) continue;
                if (i < 1 || i > 6 || j < 1 || j > 6) continue;
                
                // Verificar si ya fue descubierta
                Movimiento movimientoExistente = movimientoRepository.findByCoordenadaXAndCoordenadaYAndIdMina(i, j, idMina);
                if (movimientoExistente != null && movimientoExistente.getDescubierta()) {
                    continue;
                }
                
                // Verificar si no es bomba
                PosicionBomba bomba = posicionBombaRepository.findByCoordenadaXAndCoordenadaYAndIdMina(i, j, idMina);
                if (bomba == null) {
                    int numeroVecinas = contarBombasVecinas(i, j, idMina);
                    Movimiento nuevoMovimiento = new Movimiento(null, i, j, idMina, false, true, numeroVecinas);
                    movimientoRepository.save(nuevoMovimiento);
                    
                    // Recursión solo si no hay bombas vecinas
                    if (numeroVecinas == 0) {
                        expandirCasillasVecinas(i, j, idMina);
                    }
                }
            }
        }
    }
    
    private boolean verificarVictoria(Long idMina) {
        List<Movimiento> movimientos = movimientoRepository.findByIdMina(idMina);
        List<PosicionBomba> bombas = posicionBombaRepository.findByIdMina(idMina);
        
        // Contar casillas sin bomba descubiertas
        long casillasDescubiertas = movimientos.stream()
                .filter(m -> m.getDescubierta() && !m.getEsBomba())
                .count();
        
        // Total de casillas sin bomba = 36 - número de bombas
        int totalCasillasSinBomba = 36 - bombas.size();
        
        return casillasDescubiertas == totalCasillasSinBomba;
    }
    
    public List<Movimiento> obtenerMovimientos() {
        Configuracion config = obtenerConfiguracion();
        return movimientoRepository.findByIdMina(config.getIdMina());
    }
    
    public String obtenerEstadoJuego() {
        Configuracion config = obtenerConfiguracion();
        
        if (config.getCantIntentosActual() <= 0) {
            return "perdido";
        }
        
        if (verificarVictoria(config.getIdMina())) {
            return "ganado";
        }
        
        return "jugando";
    }
}
