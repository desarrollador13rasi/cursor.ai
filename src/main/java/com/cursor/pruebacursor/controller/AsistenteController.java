package com.cursor.pruebacursor.controller;

import com.cursor.pruebacursor.config.OpenAIConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/asistente")
public class AsistenteController {

    @Autowired
    private OpenAIConfig openAIConfig;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody Map<String, String> request) {
        try {
            String mensaje = request.get("mensaje");
            
            if (mensaje == null || mensaje.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El mensaje no puede estar vacío"));
            }
            
            // Configurar los headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAIConfig.getApiKey());

            // Preparar el cuerpo de la petición
            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-3.5-turbo");
            body.put("messages", new Object[] {
                Map.of(
                    "role", "system",
                    "content", "Eres un asistente virtual especializado en inventario y gestión de productos. Responde de manera amigable y profesional."
                ),
                Map.of(
                    "role", "user",
                    "content", mensaje
                )
            });
            body.put("temperature", 0.7);

            // Crear la petición
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Hacer la petición a OpenAI
            ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.openai.com/v1/chat/completions",
                HttpMethod.POST,
                requestEntity,
                Map.class
            );

            // Extraer y devolver la respuesta
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                @SuppressWarnings("unchecked")
                Map<String, Object>[] choices = (Map<String, Object>[]) responseBody.get("choices");
                if (choices.length > 0) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> choice = choices[0];
                    @SuppressWarnings("unchecked")
                    Map<String, Object> message = (Map<String, Object>) choice.get("message");
                    return ResponseEntity.ok(Map.of("respuesta", message.get("content")));
                }
            }

            return ResponseEntity.badRequest().body(Map.of("error", "No se pudo obtener una respuesta"));
        } catch (HttpClientErrorException.Unauthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Error de autenticación. Por favor, verifica la API key."));
        } catch (HttpClientErrorException.Forbidden e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "Acceso denegado. La API key no tiene permisos suficientes."));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(Map.of("error", "Error en la petición: " + e.getResponseBodyAsString()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }
} 