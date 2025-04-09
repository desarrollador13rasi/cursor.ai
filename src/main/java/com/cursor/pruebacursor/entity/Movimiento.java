package com.cursor.pruebacursor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
@Data
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipoMovimiento", nullable = false)
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    private LocalDateTime fecha;

    @PrePersist
    protected void onCreate() {
        fecha = LocalDateTime.now();
    }
} 