package com.cursor.pruebacursor.controller;

import com.cursor.pruebacursor.dto.ProductoDTO;
import com.cursor.pruebacursor.entity.Producto;
import com.cursor.pruebacursor.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<ProductoDTO> getAllProductos() {
        return productoService.getAllProductos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductoById(@PathVariable Long id) {
        try {
            ProductoDTO producto = productoService.getProductoById(id);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createProducto(@RequestBody Producto producto) {
        try {
            ProductoDTO savedProducto = productoService.createProducto(producto);
            return ResponseEntity.ok(savedProducto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            ProductoDTO updatedProducto = productoService.updateProducto(id, producto);
            return ResponseEntity.ok(updatedProducto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable Long id) {
        try {
            productoService.deleteProducto(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
} 