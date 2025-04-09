// Configuración de la API de OpenAI
const OPENAI_API_KEY = 'TU_API_KEY_AQUI'; // Reemplaza con tu API key
const OPENAI_API_URL = 'https://api.openai.com/v1/chat/completions';

// Función para agregar un mensaje al chat
function agregarMensaje(mensaje, tipo = 'assistant') {
    const chatMessages = document.getElementById('chatMessages');
    const mensajeDiv = document.createElement('div');
    mensajeDiv.className = `message ${tipo === 'user' ? 'user-message' : tipo === 'error' ? 'error-message' : 'assistant-message'} mb-2 p-2 rounded`;
    mensajeDiv.textContent = mensaje;
    chatMessages.appendChild(mensajeDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

// Función para enviar mensaje a la IA
async function enviarMensaje() {
    const mensaje = document.getElementById('userInput').value.trim();
    if (!mensaje) return;

    // Mostrar el mensaje del usuario
    agregarMensaje(mensaje, 'user');
    document.getElementById('userInput').value = '';

    // Mostrar indicador de carga
    const loadingDiv = document.createElement('div');
    loadingDiv.className = 'message assistant-message mb-2 p-2 rounded';
    loadingDiv.innerHTML = '<div class="message-content"><div class="loading">Pensando...</div></div>';
    document.getElementById('chatMessages').appendChild(loadingDiv);
    document.getElementById('chatMessages').scrollTop = document.getElementById('chatMessages').scrollHeight;

    try {
        const response = await fetch('/api/asistente/chat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ mensaje })
        });

        const data = await response.json();

        // Eliminar el indicador de carga
        document.getElementById('chatMessages').removeChild(loadingDiv);

        if (response.ok) {
            agregarMensaje(data.respuesta, 'assistant');
        } else {
            let mensajeError = 'Error al procesar la solicitud';
            
            if (response.status === 401) {
                mensajeError = 'Error de autenticación. Por favor, verifica la API key de OpenAI.';
            } else if (response.status === 403) {
                mensajeError = 'Acceso denegado. La API key no tiene permisos suficientes.';
            } else if (data.error) {
                mensajeError = data.error;
            }
            
            agregarMensaje(mensajeError, 'error');
        }
    } catch (error) {
        // Eliminar el indicador de carga
        document.getElementById('chatMessages').removeChild(loadingDiv);
        agregarMensaje('Error de conexión. Por favor, intenta nuevamente.', 'error');
    }
}

// Permitir enviar mensaje con Enter
document.getElementById('userInput').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        enviarMensaje();
    }
}); 