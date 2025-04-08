package com.cursor.pruebacursor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "movimientos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Carga perezosa es generalmente recomendada
    @JoinColumn(name = "producto_id", nullable = false) // Define la columna de clave foránea
    private producto producto;

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING) // Almacena el nombre del Enum como String
    @Column(nullable = false)
    private TipoMovimiento tipoMovimiento; // Asume que existe un Enum TipoMovimiento

    @Column(nullable = false)
    private Integer cantidad;

}
