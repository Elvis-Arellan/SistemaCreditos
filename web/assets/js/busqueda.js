function inicializarBuscador(inputId, tablaId) {
    const inputBusqueda = document.getElementById(inputId);
    const tabla = document.getElementById(tablaId);

    if (!inputBusqueda || !tabla) {
        console.warn(`No se encontró el input '${inputId}' o la tabla '${tablaId}'`);
        return;
    }

    inputBusqueda.addEventListener('keyup', function () {
        const filtro = this.value.toLowerCase().trim();
        const filas = tabla.getElementsByTagName('tr');

        for (let i = 0; i < filas.length; i++) {
            const fila = filas[i];
            const celdas = fila.getElementsByTagName('td');

            if (celdas.length > 0) {
                let textoFila = '';

                for (let j = 0; j < celdas.length; j++) {
                    textoFila += celdas[j].textContent || celdas[j].innerText;
                }

                textoFila = textoFila.toLowerCase();

                if (textoFila.indexOf(filtro) > -1) {
                    fila.style.display = '';
                } else {
                    fila.style.display = 'none';
                }
            }
        }

        mostrarMensajeSinResultados(tabla, filtro);
    });
}

function mostrarMensajeSinResultados(tabla, filtro) {
    const filas = tabla.getElementsByTagName('tr');
    let hayResultados = false;

    for (let i = 0; i < filas.length; i++) {
        const celdas = filas[i].getElementsByTagName('td');
        if (celdas.length > 0 && filas[i].style.display !== 'none') {
            hayResultados = true;
            break;
        }
    }

    const mensajeAnterior = tabla.querySelector('.mensaje-sin-resultados');
    if (mensajeAnterior) {
        mensajeAnterior.remove();
    }

    if (!hayResultados && filtro !== '') {
        const tbody = tabla.getElementsByTagName('tbody')[0];
        const numColumnas = tabla.getElementsByTagName('th').length;

        const fila = tbody.insertRow(0);
        fila.className = 'mensaje-sin-resultados';
        const celda = fila.insertCell(0);
        celda.colSpan = numColumnas;
        celda.style.textAlign = 'center';
        celda.style.padding = '20px';
        celda.style.color = '#94a3b8';
        celda.innerHTML = '🔍 No se encontraron resultados para: <strong>"' + filtro + '"</strong>';
    }
}

document.addEventListener('DOMContentLoaded', function () {

    inicializarBuscador('buscarUsuario', 'tablaUsuarios');


    inicializarBuscador('buscarCliente', 'tablaClientes');


    inicializarBuscador('buscarMovimiento', 'tablaMovimientos');
});

