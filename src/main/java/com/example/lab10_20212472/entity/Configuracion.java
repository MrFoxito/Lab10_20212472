package com.example.lab10_20212472.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "configuracion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuracion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMina;
    
    @Column(name = "dimMinaX")
    private Integer dimMinaX;
    
    @Column(name = "dimMinaY")
    private Integer dimMinaY;
    
    @Column(name = "cantBombas")
    private Integer cantBombas;
    
    @Column(name = "cantIntentos")
    private Integer cantIntentos;
    
    @Column(name = "cantIntentosActual")
    private Integer cantIntentosActual;
}
