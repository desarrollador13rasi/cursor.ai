package com.cursor.pruebacursor.controller;

import com.cursor.pruebacursor.dto.MovimientoDTO;
import com.cursor.pruebacursor.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @GetMapping
    public List<MovimientoDTO> getAllMovimientos() {
        return movimientoService.getAllMovimientos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovimientoById(@PathVariable Long id) {
        try {
            MovimientoDTO movimiento = movimientoService.getMovimientoById(id);
            return ResponseEntity.ok(movimiento);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createMovimiento(@RequestBody MovimientoDTO movimientoDTO) {
        try {
            MovimientoDTO savedMovimiento = movimientoService.createMovimiento(movimientoDTO);
            return ResponseEntity.ok(savedMovimiento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
} 