package com.cursor.pruebacursor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/productos")
    public String productos() {
        return "productos";
    }

    @GetMapping("/movimientos")
    public String movimientos() {
        return "movimientos";
    }

    @GetMapping("/asistente")
    public String asistente() {
        return "asistente";
    }
} 