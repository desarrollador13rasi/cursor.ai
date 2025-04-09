package com.cursor.pruebacursor.dto;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer cantidad;
    private Double precio;
} 