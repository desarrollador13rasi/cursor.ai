// Cargar productos en el select
function cargarProductos() {
    fetch('/api/productos')
        .then(response => response.json())
        .then(productos => {
            const select = document.getElementById('productoId');
            select.innerHTML = '<option value="">Seleccione un producto</option>';
            productos.forEach(producto => {
                const option = document.createElement('option');
                option.value = producto.id;
                option.textContent = producto.nombre;
                select.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error al cargar productos:', error);
            mostrarMensaje('Error al cargar los productos');
        });
}

// Cargar movimientos en la tabla
function cargarMovimientos() {
    fetch('/api/movimientos')
        .then(response => response.json())
        .then(movimientos => {
            const tbody = document.getElementById('movimientosTableBody');
            tbody.innerHTML = '';
            movimientos.forEach(movimiento => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${movimiento.id}</td>
                    <td>${movimiento.productoId}</td>
                    <td>${movimiento.fecha}</td>
                    <td>${movimiento.tipo}</td>
                    <td>${movimiento.cantidad}</td>
                `;
                tbody.appendChild(tr);
            });
        })
        .catch(error => {
            console.error('Error al cargar movimientos:', error);
            mostrarMensaje('Error al cargar los movimientos');
        });
}

// Realizar un nuevo movimiento
function realizarMovimiento() {
    const productoId = document.getElementById('productoId').value;
    const tipo = document.getElementById('tipoMovimiento').value;
    const cantidad = document.getElementById('cantidad').value;

    // Validaciones
    if (!productoId) {
        mostrarMensaje('Por favor seleccione un producto');
        return;
    }
    if (!tipo) {
        mostrarMensaje('Por favor seleccione el tipo de movimiento');
        return;
    }
    if (!cantidad || cantidad <= 0) {
        mostrarMensaje('Por favor ingrese una cantidad válida');
        return;
    }

    const movimiento = {
        productoId: parseInt(productoId),
        tipo: tipo,
        cantidad: parseInt(cantidad)
    };

    fetch('/api/movimientos', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(movimiento)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(data => {
                throw new Error(data.error || 'Error al realizar el movimiento');
            });
        }
        return response.json();
    })
    .then(() => {
        mostrarMensaje('Movimiento realizado con éxito');
        document.getElementById('movimientoForm').reset();
        cargarMovimientos();
        cargarProductos(); // Recargar productos para actualizar el stock
    })
    .catch(error => {
        mostrarMensaje(error.message);
    });
}

// Mostrar mensaje en el modal
function mostrarMensaje(mensaje) {
    document.getElementById('mensajeTexto').textContent = mensaje;
    const modal = new bootstrap.Modal(document.getElementById('mensajeModal'));
    modal.show();
}

// Cargar datos al iniciar la página
document.addEventListener('DOMContentLoaded', () => {
    cargarProductos();
    cargarMovimientos();
}); 