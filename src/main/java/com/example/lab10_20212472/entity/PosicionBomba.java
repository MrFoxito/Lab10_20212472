package com.example.lab10_20212472.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posicionbomba")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PosicionBomba {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBomba;
    
    @Column(name = "coordenadaX")
    private Integer coordenadaX;
    
    @Column(name = "coordenadaY")
    private Integer coordenadaY;
    
    @Column(name = "idMina")
    private Long idMina;
}
