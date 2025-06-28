package com.example.lab10_20212472.repository;

import com.example.lab10_20212472.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByIdMina(Long idMina);
    Movimiento findByCoordenadaXAndCoordenadaYAndIdMina(Integer x, Integer y, Long idMina);
}
