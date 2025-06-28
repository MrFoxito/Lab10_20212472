package com.example.lab10_20212472.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movimiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "coordenadaX")
    private Integer coordenadaX;
    
    @Column(name = "coordenadaY")
    private Integer coordenadaY;
    
    @Column(name = "idMina")
    private Long idMina;
    
    @Column(name = "esBomba")
    private Boolean esBomba;
    
    @Column(name = "descubierta")
    private Boolean descubierta;
    
    @Column(name = "numeroVecinas")
    private Integer numeroVecinas;
}
