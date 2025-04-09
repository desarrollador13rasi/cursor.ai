// Cargar productos en la tabla
function cargarProductos() {
    fetch('/api/productos')
        .then(response => response.json())
        .then(productos => {
            const tbody = document.getElementById('productosTableBody');
            tbody.innerHTML = '';
            productos.forEach(producto => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${producto.id}</td>
                    <td>${producto.nombre}</td>
                    <td>${producto.descripcion || ''}</td>
                    <td>${producto.cantidad}</td>
                    <td>${producto.precio}</td>
                    <td>
                        <button class="btn btn-sm btn-warning" onclick="editarProducto(${producto.id})">
                            Editar
                        </button>
                        <button class="btn btn-sm btn-danger" onclick="eliminarProducto(${producto.id})">
                            Eliminar
                        </button>
                    </td>
                `;
                tbody.appendChild(tr);
            });
        })
        .catch(error => {
            console.error('Error al cargar productos:', error);
            mostrarMensaje('Error al cargar los productos');
        });
}

// Guardar producto (nuevo o edición)
function guardarProducto() {
    const productoId = document.getElementById('productoId').value;
    const nombre = document.getElementById('nombre').value;
    const descripcion = document.getElementById('descripcion').value;
    const cantidad = document.getElementById('cantidad').value;
    const precio = document.getElementById('precio').value;

    if (!nombre || !cantidad || !precio) {
        mostrarMensaje('Por favor complete todos los campos requeridos');
        return;
    }

    const producto = {
        nombre: nombre,
        descripcion: descripcion,
        cantidad: parseInt(cantidad),
        precio: parseFloat(precio)
    };

    const url = productoId ? `/api/productos/${productoId}` : '/api/productos';
    const method = productoId ? 'PUT' : 'POST';

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(producto)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(data => {
                throw new Error(data.error || 'Error al guardar el producto');
            });
        }
        return response.json();
    })
    .then(() => {
        mostrarMensaje('Producto guardado con éxito');
        document.getElementById('productoForm').reset();
        const modal = bootstrap.Modal.getInstance(document.getElementById('productoModal'));
        modal.hide();
        cargarProductos();
    })
    .catch(error => {
        mostrarMensaje(error.message);
    });
}

// Editar producto
function editarProducto(id) {
    fetch(`/api/productos/${id}`)
        .then(response => response.json())
        .then(producto => {
            document.getElementById('productoId').value = producto.id;
            document.getElementById('nombre').value = producto.nombre;
            document.getElementById('descripcion').value = producto.descripcion || '';
            document.getElementById('cantidad').value = producto.cantidad;
            document.getElementById('precio').value = producto.precio;
            document.getElementById('modalTitle').textContent = 'Editar Producto';
            
            const modal = new bootstrap.Modal(document.getElementById('productoModal'));
            modal.show();
        })
        .catch(error => {
            console.error('Error al cargar producto:', error);
            mostrarMensaje('Error al cargar el producto');
        });
}

// Eliminar producto
function eliminarProducto(id) {
    if (confirm('¿Está seguro de eliminar este producto?')) {
        fetch(`/api/productos/${id}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(data => {
                    throw new Error(data.error || 'Error al eliminar el producto');
                });
            }
            mostrarMensaje('Producto eliminado con éxito');
            cargarProductos();
        })
        .catch(error => {
            mostrarMensaje(error.message);
        });
    }
}

// Mostrar mensaje en el modal
function mostrarMensaje(mensaje) {
    document.getElementById('mensajeTexto').textContent = mensaje;
    const modal = new bootstrap.Modal(document.getElementById('mensajeModal'));
    modal.show();
}

// Limpiar formulario al cerrar el modal
document.getElementById('productoModal').addEventListener('hidden.bs.modal', function () {
    document.getElementById('productoForm').reset();
    document.getElementById('productoId').value = '';
    document.getElementById('modalTitle').textContent = 'Nuevo Producto';
});

// Cargar datos al iniciar la página
document.addEventListener('DOMContentLoaded', () => {
    cargarProductos();
}); 