package com.cursor.pruebacursor.service;

import com.cursor.pruebacursor.dto.ProductoDTO;
import com.cursor.pruebacursor.entity.Producto;
import com.cursor.pruebacursor.repository.ProductoRepository;
import com.cursor.pruebacursor.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    public List<ProductoDTO> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO getProductoById(Long id) {
        return productoRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Transactional
    public ProductoDTO createProducto(Producto producto) {
        validateProducto(producto);
        Producto savedProducto = productoRepository.save(producto);
        return convertToDTO(savedProducto);
    }

    @Transactional
    public ProductoDTO updateProducto(Long id, Producto producto) {
        validateProducto(producto);
        Producto existingProducto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        existingProducto.setNombre(producto.getNombre());
        existingProducto.setDescripcion(producto.getDescripcion());
        existingProducto.setCantidad(producto.getCantidad());
        existingProducto.setPrecio(producto.getPrecio());

        Producto updatedProducto = productoRepository.save(existingProducto);
        return convertToDTO(updatedProducto);
    }

    @Transactional
    public void deleteProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Primero eliminamos los movimientos asociados
        movimientoRepository.deleteByProductoId(id);
        // Luego eliminamos el producto
        productoRepository.delete(producto);
    }

    private void validateProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del producto es obligatorio");
        }
        if (producto.getCantidad() == null || producto.getCantidad() < 0) {
            throw new RuntimeException("La cantidad debe ser mayor o igual a 0");
        }
        if (producto.getPrecio() == null || producto.getPrecio() < 0) {
            throw new RuntimeException("El precio debe ser mayor o igual a 0");
        }
    }

    private ProductoDTO convertToDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setCantidad(producto.getCantidad());
        dto.setPrecio(producto.getPrecio());
        return dto;
    }
} 