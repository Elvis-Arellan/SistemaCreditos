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

    <style>
        body {
            background-color: #0a0a0a;
            min-height: 100vh;
            padding: 40px 20px;
            font-family: "Segoe UI", sans-serif;
        }

        .container-registro {
            max-width: 700px;
            margin: 0 auto;
        }

        .btn-actions {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-bottom: 30px;
        }

        .btn-regresar {
            background-color: #2563eb;
            border: none;
            color: white;
            padding: 12px 30px;
            border-radius: 8px;
            font-weight: 600;
            font-size: 16px;
            cursor: pointer;
            transition: .3s;
            text-decoration: none;
        }
        .btn-regresar:hover { 
            background-color: #1d4ed8;
            color: white;
        }

        .btn-guardar {
            background-color: #16a34a;
            border: 2px solid #22c55e;
            color: white;
            padding: 12px 40px;
            border-radius: 8px;
            font-weight: 600;
            font-size: 16px;
            cursor: pointer;
            transition: .3s;
        }
        .btn-guardar:hover {
            background-color: #15803d;
            border-color: #16a34a;
        }

        .card-registro {
            background-color: #1e293b;
            border-radius: 12px;
            padding: 35px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.5);
        }

        .cliente-info {
            background-color: #0f172a;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 25px;
            border-left: 4px solid #2563eb;
        }

        .cliente-info h5 {
            color: #f1f5f9;
            margin-bottom: 10px;
        }

        .cliente-info p {
            color: #94a3b8;
            margin-bottom: 5px;
            font-size: 14px;
        }

        .saldo-actual {
            font-size: 24px;
            font-weight: 700;
            color: #E64A19;
            margin-top: 10px;
        }

        .card-title {
            color: #f1f5f9;
            font-size: 22px;
            font-weight: 600;
            margin-bottom: 30px;
        }

        label {
            color: #94a3b8;
            font-size: 14px;
            font-weight: 500;
            margin-bottom: 8px;
            display: block;
        }

        .form-control, .form-select {
            background-color: #0f172a;
            border: 1px solid #334155;
            border-radius: 8px;
            color: #f1f5f9;
            padding: 12px 20px;
            font-size: 15px;
            margin-bottom: 20px;
        }

        .form-control:focus, .form-select:focus {
            background-color: #0f172a;
            border-color: #2563eb;
            color: #f1f5f9;
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
        }

        .form-select option {
            background-color: #0f172a;
            color: #f1f5f9;
        }

        textarea.form-control {
            min-height: 100px;
            resize: vertical;
        }
    </style>
</head>

<body>
    <div class="container-registro">

        <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>❌ Error:</strong> <%= request.getAttribute("error") %>
            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="alert"></button>
        </div>
        <% } %>

        <div class="btn-actions">
            <a href="${pageContext.request.contextPath}/movimientos/historial?id=<%= cliente.getId_cliente()%>" 
               class="btn-regresar">
                ← Regresar
            </a>

            <button type="submit" form="formRegistrarMovimiento" class="btn-guardar">
                Guardar Movimiento
            </button>
        </div>

        <div class="card-registro">

            <div class="cliente-info">
                <h5>Cliente: <%= cliente.getNombre()%> <%= cliente.getApellido()%></h5>
                <p><strong>Teléfono:</strong> <%= cliente.getTelefono()%></p>
                <p class="saldo-actual">
                    Saldo Actual: S/. <%= String.format("%.2f", cliente.getSaldo())%>
                </p>
            </div>

            <h3 class="card-title">Registrar Movimiento</h3>

            <form action="${pageContext.request.contextPath}/movimientos/guardar"
                  method="POST"
                  id="formRegistrarMovimiento">

                <input type="hidden" name="id_cliente" value="<%= cliente.getId_cliente()%>">

                <label>Tipo de Movimiento <span style="color: #ef4444;">*</span></label>
                <select name="tipo_movimiento" id="tipo_movimiento" class="form-select" required>
                    <option value="">-- Seleccione --</option>
                    <option value="FIADO">FIADO (Aumenta la deuda)</option>
                    <option value="PAGO">PAGO (Reduce la deuda)</option>
                </select>

                <label>Monto (S/.) <span style="color: #ef4444;">*</span></label>
                <input type="number" 
                       name="monto" 
                       id="monto" 
                       class="form-control"
                       step="0.01"
                       min="0.01"
                       placeholder="0.00"
                       required>

                <label>Descripción (Opcional)</label>
                <textarea name="descripcion" 
                          id="descripcion" 
                          class="form-control"
                          placeholder="Ejemplo: Compra de arroz, aceite, azúcar..."></textarea>

                <div style="background-color: #0f172a; padding: 15px; border-radius: 8px; border-left: 3px solid #2563eb;">
                    <p style="color: #94a3b8; margin: 0; font-size: 13px;">
                        <strong style="color: #f1f5f9;">💡 Nota:</strong><br>
                        • <strong>FIADO:</strong> Suma el monto al saldo (el cliente debe más)<br>
                        • <strong>PAGO:</strong> Resta el monto al saldo (el cliente pagó)
                    </p>
                </div>

            </form>

        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        document.getElementById('formRegistrarMovimiento').addEventListener('submit', function(e) {
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

        document.getElementById('monto').addEventListener('input', function() {
            this.value = this.value.replace(/[^0-9.]/g, '');
            const parts = this.value.split('.');
            if (parts.length > 2) {
                this.value = parts[0] + '.' + parts.slice(1).join('');
            }
        });

        document.getElementById('tipo_movimiento').addEventListener('change', function() {
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