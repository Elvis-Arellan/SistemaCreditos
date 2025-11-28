<%@page import="modelo.Cliente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (session == null || session.getAttribute("idUsuario") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    Cliente cliente = (Cliente) request.getAttribute("cliente");
    if (cliente == null) {
        response.sendRedirect(request.getContextPath() + "/clientes/listar");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Registrar Movimiento</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/movimientos.css" />
    </head>

    <body class="movimientos-registro-body">
        <div class="movimientos-container-registro">


            <% if (request.getAttribute("error") != null) {%>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>❌ Error:</strong> <%= request.getAttribute("error")%>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="alert"></button>
            </div>
            <% }%>


            <div class="movimientos-btn-actions">
                <a href="${pageContext.request.contextPath}/movimientos/historial?id=<%= cliente.getId_cliente()%>" 
                   class="movimientos-btn-regresar">
                    ← Regresar
                </a>

                <button type="submit" form="formRegistrarMovimiento" class="movimientos-btn-guardar">
                    Guardar Movimiento
                </button>
            </div>


            <div class="movimientos-card-registro">


                <div class="movimientos-cliente-info">
                    <h5>Cliente: <%= cliente.getNombre()%> <%= cliente.getApellido()%></h5>
                    <p><strong>Teléfono:</strong> <%= cliente.getTelefono()%></p>
                    <p class="movimientos-saldo-actual">
                        Saldo Actual: S/. <%= String.format("%.2f", cliente.getSaldo())%>
                    </p>
                </div>

                <h3 class="movimientos-card-title">Registrar Movimiento</h3>

                <form action="${pageContext.request.contextPath}/movimientos/guardar"
                      method="POST"
                      id="formRegistrarMovimiento">

                    <input type="hidden" name="id_cliente" value="<%= cliente.getId_cliente()%>">


                    <label class="movimientos-form-label">Tipo de Movimiento <span class="movimientos-required">*</span></label>
                    <select name="tipo_movimiento" id="tipo_movimiento" class="movimientos-form-select" required>
                        <option value="">-- Seleccione --</option>
                        <option value="FIADO">FIADO (Aumenta la deuda)</option>
                        <option value="PAGO">PAGO (Reduce la deuda)</option>
                    </select>


                    <label class="movimientos-form-label">Monto (S/.) <span class="movimientos-required">*</span></label>
                    <input type="number" 
                           name="monto" 
                           id="monto" 
                           class="movimientos-form-control"
                           step="0.01"
                           min="0.01"
                           placeholder="0.00"
                           required>


                    <label class="movimientos-form-label">Descripción (Opcional)</label>
                    <textarea name="descripcion" 
                              id="descripcion" 
                              class="movimientos-form-control movimientos-textarea"
                              placeholder="Ejemplo: Compra de arroz, aceite, azúcar..."></textarea>


                    <div class="movimientos-info-box">
                        <p class="movimientos-info-text">
                            <strong>💡 Nota:</strong><br>
                            • <strong>FIADO:</strong> Suma el monto al saldo (el cliente debe más)<br>
                            • <strong>PAGO:</strong> Resta el monto al saldo (el cliente pagó)
                        </p>
                    </div>

                </form>

            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

        <script>

            document.getElementById('formRegistrarMovimiento').addEventListener('submit', function (e) {
                const tipoMovimiento = document.getElementById('tipo_movimiento').value;
                const monto = parseFloat(document.getElementById('monto').value);

                if (tipoMovimiento === "") {
                    e.preventDefault();
                    alert("❌ Debe seleccionar un tipo de movimiento");
                    return;
                }

                if (isNaN(monto) || monto <= 0) {
                    e.preventDefault();
                    alert("❌ El monto debe ser mayor a cero");
                    return;
                }


                const textoConfirmacion = tipoMovimiento === "FIADO"
                        ? `¿Registrar FIADO de S/. ${monto.toFixed(2)}?\n\nEsto AUMENTARÁ la deuda del cliente.`
                        : `¿Registrar PAGO de S/. ${monto.toFixed(2)}?\n\nEsto REDUCIRÁ la deuda del cliente.`;

                if (!confirm(textoConfirmacion)) {
                    e.preventDefault();
                }
            });


            document.getElementById('monto').addEventListener('input', function () {
                this.value = this.value.replace(/[^0-9.]/g, '');
                const parts = this.value.split('.');
                if (parts.length > 2) {
                    this.value = parts[0] + '.' + parts.slice(1).join('');
                }
            });


            document.getElementById('tipo_movimiento').addEventListener('change', function () {
                if (this.value === 'FIADO') {
                    this.style.borderColor = '#E64A19';
                    this.style.color = '#E64A19';
                } else if (this.value === 'PAGO') {
                    this.style.borderColor = '#28a745';
                    this.style.color = '#28a745';
                } else {
                    this.style.borderColor = '#334155';
                    this.style.color = '#f1f5f9';
                }
            });
        </script>

    </body>
</html>