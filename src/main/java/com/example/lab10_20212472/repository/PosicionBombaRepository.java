package com.example.lab10_20212472.repository;

import com.example.lab10_20212472.entity.PosicionBomba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository


public interface PosicionBombaRepository extends JpaRepository<PosicionBomba, Long> {
    List<PosicionBomba> findByIdMina(Long idMina);
    PosicionBomba findByCoordenadaXAndCoordenadaYAndIdMina(Integer x, Integer y, Long idMina);
}
