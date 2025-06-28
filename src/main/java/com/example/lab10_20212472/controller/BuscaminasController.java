package com.example.lab10_20212472.controller;

import com.example.lab10_20212472.entity.Configuracion;
import com.example.lab10_20212472.entity.Movimiento;
import com.example.lab10_20212472.service.BuscaminasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class BuscaminasController {
    
    @Autowired
    private BuscaminasService buscaminasService;

    public static class GameResponse {
        private Configuracion configuracion;
        private Map<String, Movimiento> gridMap;
        private String estadoJuego;
        private String mensaje;

        public GameResponse(Configuracion configuracion, Map<String, Movimiento> gridMap, 
                           String estadoJuego, String mensaje) {
            this.configuracion = configuracion;
            this.gridMap = gridMap;
            this.estadoJuego = estadoJuego;
            this.mensaje = mensaje;
        }

        public Configuracion getConfiguracion() { return configuracion; }
        public Map<String, Movimiento> getGridMap() { return gridMap; }
        public String getEstadoJuego() { return estadoJuego; }
        public String getMensaje() { return mensaje; }
    }



    @GetMapping("/buscaminas")
    public String mostrarJuego(Model model) {
        Configuracion config = buscaminasService.obtenerConfiguracion();
        List<Movimiento> movimientos = buscaminasService.obtenerMovimientos();
        String estadoJuego = buscaminasService.obtenerEstadoJuego();
        
        // Crear un mapa para facilitar el acceso a los movimientos en Thymeleaf
        Map<String, Movimiento> gridMap = new HashMap<>();
        for (Movimiento mov : movimientos) {
            if (mov.getDescubierta()) {
                gridMap.put(mov.getCoordenadaX() + "_" + mov.getCoordenadaY(), mov);
            }
        }
        
        model.addAttribute("configuracion", config);
        model.addAttribute("movimientos", movimientos);
        model.addAttribute("gridMap", gridMap);
        model.addAttribute("estadoJuego", estadoJuego);
        model.addAttribute("mensaje", "");
        
        return "buscaminas";
    }



    @PostMapping("/buscaminas/explotar")
    public String explotar(@RequestParam("coordenadas") String coordenadas, Model model) {
        String mensaje = "";
        
        try {
            String[] coords = coordenadas.trim().split("\\s+");
            if (coords.length != 2) {
                mensaje = "Formato incorrecto. Use: x y (ejemplo: 1 5)";
            } else {
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                
                if (x < 1 || x > 6 || y < 1 || y > 6) {
                    mensaje = "Coordenadas fuera de rango. Use valores entre 1 y 6";
                } else {
                    mensaje = buscaminasService.realizarMovimiento(x, y);
                }
            }
        } catch (NumberFormatException e) {
            mensaje = "Coordenadas inválidas. Use números enteros";
        }




        
        Configuracion config = buscaminasService.obtenerConfiguracion();
        List<Movimiento> movimientos = buscaminasService.obtenerMovimientos();
        String estadoJuego = buscaminasService.obtenerEstadoJuego();
        
        Map<String, Movimiento> gridMap = new HashMap<>();
        for (Movimiento mov : movimientos) {
            if (mov.getDescubierta()) {
                gridMap.put(mov.getCoordenadaX() + "_" + mov.getCoordenadaY(), mov);
            }
        }



        
        model.addAttribute("configuracion", config);
        model.addAttribute("movimientos", movimientos);
        model.addAttribute("gridMap", gridMap);
        model.addAttribute("estadoJuego", estadoJuego);
        model.addAttribute("mensaje", mensaje);
        
        return "buscaminas";
    }



    @PostMapping("/buscaminas/reiniciar")
    public String reiniciarJuego() {
        buscaminasService.inicializarJuego();
        return "redirect:/buscaminas";
    }




    @GetMapping("/api/buscaminas/estado")
    public ResponseEntity<GameResponse> obtenerEstadoJuego() {
        Configuracion config = buscaminasService.obtenerConfiguracion();
        List<Movimiento> movimientos = buscaminasService.obtenerMovimientos();
        String estadoJuego = buscaminasService.obtenerEstadoJuego();
        
        Map<String, Movimiento> gridMap = new HashMap<>();
        for (Movimiento mov : movimientos) {
            if (mov.getDescubierta()) {
                gridMap.put(mov.getCoordenadaX() + "_" + mov.getCoordenadaY(), mov);
            }
        }
        
        GameResponse response = new GameResponse(config, gridMap, estadoJuego, "");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/buscaminas/movimiento")
    public ResponseEntity<GameResponse> realizarMovimiento(@RequestParam("x") int x, 
                                                          @RequestParam("y") int y) {
        String mensaje = buscaminasService.realizarMovimiento(x, y);
        
        Configuracion config = buscaminasService.obtenerConfiguracion();
        List<Movimiento> movimientos = buscaminasService.obtenerMovimientos();
        String estadoJuego = buscaminasService.obtenerEstadoJuego();
        
        Map<String, Movimiento> gridMap = new HashMap<>();
        for (Movimiento mov : movimientos) {
            if (mov.getDescubierta()) {
                gridMap.put(mov.getCoordenadaX() + "_" + mov.getCoordenadaY(), mov);
            }
        }
        
        GameResponse response = new GameResponse(config, gridMap, estadoJuego, mensaje);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/buscaminas/reiniciar")
    public ResponseEntity<String> reiniciarJuegoApi() {
        buscaminasService.inicializarJuego();
        return ResponseEntity.ok("Juego reiniciado");
    }
}
