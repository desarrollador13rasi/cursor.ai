package com.cursor.pruebacursor.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MovimientoDTO {
    private Long id;
    private Long productoId;
    private String tipo;
    private Integer cantidad;
    private LocalDateTime fecha;
} 