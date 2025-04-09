package com.cursor.pruebacursor.repository;

import com.cursor.pruebacursor.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByProductoId(Long productoId);
    
    void deleteByProductoId(Long productoId);
} 