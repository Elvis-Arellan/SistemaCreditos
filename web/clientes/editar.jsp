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
    <title>Editar Cliente</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            background-color: #0a0a0a;
            min-height: 100vh;
            padding: 40px 20px;
            font-family: "Segoe UI", sans-serif;
        }

        .container-editar {
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
        }
        .btn-regresar:hover { background-color: #1d4ed8; }

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

        .btn-eliminar {
            background-color: #dc2626;
            border: none;
            color: white;
            padding: 12px 30px;
            border-radius: 8px;
            font-weight: 600;
            font-size: 16px;
            cursor: pointer;
            transition: .3s;
        }
        .btn-eliminar:hover { background-color: #b91c1c; }

        .card-editar {
            background-color: #1e293b;
            border-radius: 12px;
            padding: 35px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.5);
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

        .form-control {
            background-color: #0f172a;
            border: 1px solid #334155;
            border-radius: 25px;
            color: #f1f5f9;
            padding: 12px 20px;
            font-size: 15px;
            margin-bottom: 20px;
        }

        .form-control[readonly] {
            background-color: #1e293b;
            cursor: not-allowed;
            opacity: 0.7;
        }

        .row-estado-fecha {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin-top: 10px;
        }

        .radio-group { display: flex; gap: 10px; }
        .radio-container { position: relative; }

        .radio-container input[type="radio"] {
            position: absolute;
            opacity: 0;
        }

        .radio-label {
            background-color: #0f172a;
            border: 1px solid #334155;
            border-radius: 8px;
            padding: 10px 20px;
            color: #94a3b8;
            cursor: pointer;
            transition: .3s;
            font-size: 14px;
            font-weight: 500;
        }

        .radio-container input[type="radio"]:checked + .radio-label {
            background-color: #16a34a;
            border-color: #22c55e;
            color: white;
        }
    </style>
</head>

<body>
    <div class="container-editar">

        
        <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>❌ Error:</strong> <%= request.getAttribute("error") %>
            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="alert"></button>
        </div>
        <% } %>

        
        <div class="btn-actions">
            <a href="${pageContext.request.contextPath}/clientes/listar" class="btn-regresar text-decoration-none">
                Regresar
            </a>

            <button type="submit" form="formEditarCliente" class="btn-guardar">
                Guardar Cambios
            </button>

            <button type="button"
                    class="btn-eliminar"
                    onclick="confirmarEliminacion(<%= cliente.getId_cliente() %>, '<%= cliente.getNombre() %> <%= cliente.getApellido() %>')">
                Eliminar Cliente
            </button>
        </div>

        
        <div class="card-editar">

            <h3 class="card-title">Editar Cliente</h3>

            <form action="${pageContext.request.contextPath}/clientes/actualizar"
                  method="POST"
                  id="formEditarCliente">

                <input type="hidden" name="id_cliente" value="<%= cliente.getId_cliente() %>">

                
                <label>Nombres</label>
                <input type="text" name="nombre" id="nombre" class="form-control"
                       value="<%= cliente.getNombre() %>" required>

                
                <label>Apellidos</label>
                <input type="text" name="apellido" id="apellido" class="form-control"
                       value="<%= cliente.getApellido() %>" required>

                
                <label>Email</label>
                <input type="email" name="email" id="email" class="form-control"
                       value="<%= cliente.getEmail() != null ? cliente.getEmail() : "" %>">

                
                <label>Teléfono</label>
                <input type="text" name="telefono" id="telefono" class="form-control"
                       value="<%= cliente.getTelefono() %>" required>

                
                <div class="row-estado-fecha">

                    <div>
                        <label>Estado</label>
                        <div class="radio-group">

                            <div class="radio-container">
                                <input type="radio" name="estado" id="activo" value="ACTIVO"
                                       <%= "ACTIVO".equals(cliente.getEstado()) ? "checked" : "" %>>
                                <label class="radio-label" for="activo">Activo</label>
                            </div>

                            <div class="radio-container">
                                <input type="radio" name="estado" id="inactivo" value="INACTIVO"
                                       <%= "INACTIVO".equals(cliente.getEstado()) ? "checked" : "" %>>
                                <label class="radio-label" for="inactivo">Inactivo</label>
                            </div>

                        </div>
                    </div>

                    <div>
                        <label>Fecha Registro</label>
                        <input type="text"
                               value="<%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(cliente.getFecha_registro()) %>"
                               class="form-control" readonly>
                    </div>

                </div>

                
                <input type="hidden" name="saldo"
                       value="<%= cliente.getSaldo() != null ? cliente.getSaldo() : "0.00" %>">

            </form>

        </div>
    </div>


    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        
        document.getElementById('formEditarCliente').addEventListener('submit', function(e) {

            const nombre = nombre.value.trim();
            const apellido = apellido.value.trim();
            const telefono = telefono.value.trim();
            const email = email.value.trim();

            if (nombre === "" || apellido === "" || telefono === "") {
                e.preventDefault();
                alert("❌ Complete todos los campos obligatorios");
                return;
            }

            if (!/^[0-9]{7,15}$/.test(telefono)) {
                e.preventDefault();
                alert("❌ El teléfono debe tener 7 a 15 dígitos");
                return;
            }

            if (email !== "" && !/^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(email)) {
                e.preventDefault();
                alert("❌ El email no es válido");
            }
        });


        document.getElementById("telefono").addEventListener("input", function(){
            this.value = this.value.replace(/[^0-9]/g, "");
        });


        function confirmarEliminacion(id, nombre) {
            if (confirm("⚠️ ¿Eliminar a " + nombre + "?\nEsto NO se puede deshacer.")) {
                window.location.href =
                    '${pageContext.request.contextPath}/clientes/eliminar?id=' + id;
            }
        }
    </script>

</body>
</html>
