package com.cursor.pruebacursor.service;

import com.cursor.pruebacursor.dto.MovimientoDTO;
import com.cursor.pruebacursor.entity.Movimiento;
import com.cursor.pruebacursor.entity.Producto;
import com.cursor.pruebacursor.entity.TipoMovimiento;
import com.cursor.pruebacursor.repository.MovimientoRepository;
import com.cursor.pruebacursor.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<MovimientoDTO> getAllMovimientos() {
        return movimientoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MovimientoDTO getMovimientoById(Long id) {
        return movimientoRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
    }

    @Transactional
    public MovimientoDTO createMovimiento(MovimientoDTO movimientoDTO) {
        // Validar que el tipo de movimiento sea válido
        if (movimientoDTO.getTipo() == null) {
            throw new RuntimeException("El tipo de movimiento es obligatorio");
        }

        TipoMovimiento tipoMovimiento;
        try {
            tipoMovimiento = TipoMovimiento.valueOf(movimientoDTO.getTipo().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de movimiento inválido. Debe ser ENTRADA o SALIDA");
        }

        // Obtener el producto
        Producto producto = productoRepository.findById(movimientoDTO.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Validar la cantidad
        if (movimientoDTO.getCantidad() == null || movimientoDTO.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }

        // Crear el movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setCantidad(movimientoDTO.getCantidad());

        // Actualizar stock del producto
        if (tipoMovimiento == TipoMovimiento.ENTRADA) {
            producto.setCantidad(producto.getCantidad() + movimiento.getCantidad());
        } else {
            if (producto.getCantidad() < movimiento.getCantidad()) {
                throw new RuntimeException("No hay suficiente stock disponible. Stock actual: " + producto.getCantidad());
            }
            producto.setCantidad(producto.getCantidad() - movimiento.getCantidad());
        }

        // Guardar los cambios
        productoRepository.save(producto);
        Movimiento savedMovimiento = movimientoRepository.save(movimiento);
        return convertToDTO(savedMovimiento);
    }

    private MovimientoDTO convertToDTO(Movimiento movimiento) {
        MovimientoDTO dto = new MovimientoDTO();
        dto.setId(movimiento.getId());
        dto.setProductoId(movimiento.getProducto().getId());
        dto.setTipo(movimiento.getTipoMovimiento().name());
        dto.setCantidad(movimiento.getCantidad());
        dto.setFecha(movimiento.getFecha());
        return dto;
    }
} 