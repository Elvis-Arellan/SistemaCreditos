<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Movimiento"%>
<%@page import="modelo.Cliente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (session == null || session.getAttribute("idUsuario") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    Cliente cliente = (Cliente) request.getAttribute("cliente");
    List<Movimiento> movimientos = (List<Movimiento>) request.getAttribute("movimientos");
    BigDecimal totalFiado = (BigDecimal) request.getAttribute("totalFiado");
    BigDecimal totalPagado = (BigDecimal) request.getAttribute("totalPagado");

    if (cliente == null) {
        response.sendRedirect(request.getContextPath() + "/clientes/listar");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Historial de Movimientos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/estilos.css" />
</head>

<body>
    <header style="background: #192AE6; padding: 15px 0; margin-bottom: 20px;">
        <div class="container d-flex justify-content-between align-items-center">
            <a href="${pageContext.request.contextPath}/clientes/listar" class="titulo text-white text-decoration-none">
                ← Volver a Clientes
            </a>

            <div class="d-flex align-items-center gap-3">
                <span class="text-white">
                    Bienvenido: <strong><%= session.getAttribute("nombreCompleto") != null ? session.getAttribute("nombreCompleto") : "Usuario"%></strong>
                </span>

                <a href="${pageContext.request.contextPath}/login?action=logout" 
                   class="btn btn-danger btn-sm">
                    Cerrar Sesión
                </a>
            </div>
        </div>
    </header>

    <div class="container mt-3">
        <%
            String mensaje = (String) session.getAttribute("mensaje");
            String error = (String) session.getAttribute("error");

            if (mensaje != null) {
                session.removeAttribute("mensaje");
        %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <strong>✅ </strong> <%= mensaje%>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% } %>

        <% if (error != null) {
                session.removeAttribute("error");
        %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>❌ </strong> <%= error%>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% } %>
    </div>


    <main class="container py-5">

        <div class="carrito-container p-4 rounded mb-4">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <h3 class="fw-bold text-white mb-2">
                        <%= cliente.getNombre()%> <%= cliente.getApellido()%>
                    </h3>
                    <p class="text-secondary mb-1">
                        <strong>Teléfono:</strong> <%= cliente.getTelefono()%>
                    </p>
                    <p class="text-secondary mb-0">
                        <strong>Email:</strong> <%= cliente.getEmail() != null ? cliente.getEmail() : "No registrado"%>
                    </p>
                </div>
                <div class="col-md-6 text-end">
                    <h5 class="text-secondary mb-2">Saldo Actual</h5>
                    <h2 class="fw-bold" style="color: <%= cliente.getSaldo().compareTo(BigDecimal.ZERO) > 0 ? "#E64A19" : "#28a745" %>;">
                        S/. <%= String.format("%.2f", cliente.getSaldo())%>
                    </h2>
                </div>
            </div>
        </div>

        <div class="text-end mb-4">
            <a href="${pageContext.request.contextPath}/movimientos/registrar?id=<%= cliente.getId_cliente()%>" 
               class="btn btn-success btn-lg fw-bold px-5">
                + Registrar Movimiento
            </a>
        </div>

        <div class="row g-4">

            <div class="col-lg-9">
                <div class="carrito-container p-4 rounded">
                    <h4 class="fw-bold mb-4 text-white">Historial de Movimientos</h4>

                    <table class="table table-transparent text-center align-middle mb-0">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Fecha</th>
                                <th>Tipo</th>
                                <th>Descripción</th>
                                <th>Monto</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (movimientos != null && !movimientos.isEmpty()) {
                                    int count = 1;
                                    for (Movimiento m : movimientos) {%>
                            <tr>
                                <td class="text-white fw-bold"><%= count%></td>
                                <td>
                                    <%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(m.getFecha_movimiento())%>
                                </td>
                                <td>
                                    <% if ("FIADO".equals(m.getTipo_movimiento())) {%>
                                    <span class="badge bg-danger">FIADO</span>
                                    <% } else {%>
                                    <span class="badge bg-success">PAGO</span>
                                    <% }%>
                                </td>
                                <td class="text-start">
                                    <%= m.getDescripcion() != null ? m.getDescripcion() : "-"%>
                                </td>
                                <td class="fw-bold" style="color: <%= "FIADO".equals(m.getTipo_movimiento()) ? "#E64A19" : "#28a745" %>;">
                                    S/. <%= String.format("%.2f", m.getMonto())%>
                                </td>
                                <td>
                                    <button class="btn btn-danger btn-sm" 
                                            onclick="confirmarEliminacion(<%= m.getId_movimiento()%>, <%= cliente.getId_cliente()%>)">
                                        Eliminar
                                    </button>
                                </td>
                            </tr>
                            <% count++;
                                }
                            } else { %>
                            <tr>
                                <td colspan="6" class="text-muted">
                                    No hay movimientos registrados para este cliente
                                </td>
                            </tr>
                            <% }%>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="col-lg-3 d-flex flex-column gap-4">

                <div class="carrito-container p-4 rounded text-center">
                    <h5 class="fw-bold mb-2 text-white">Total Fiado</h5>
                    <h3 class="fw-bold" style="color: #E64A19;">
                        S/. <%= totalFiado != null ? String.format("%.2f", totalFiado) : "0.00"%>
                    </h3>
                </div>

                <div class="carrito-container p-4 rounded text-center">
                    <h5 class="fw-bold mb-2 text-white">Total Pagado</h5>
                    <h3 class="fw-bold" style="color: #28a745;">
                        S/. <%= totalPagado != null ? String.format("%.2f", totalPagado) : "0.00"%>
                    </h3>
                </div>

                <div class="carrito-container p-4 rounded text-center">
                    <h5 class="fw-bold mb-2 text-white">Movimientos</h5>
                    <h3 class="fw-bold" style="color: #2563eb;">
                        <%= (movimientos != null) ? movimientos.size() : 0%>
                    </h3>
                </div>

                <a href="${pageContext.request.contextPath}/clientes/editar?id=<%= cliente.getId_cliente()%>" 
                   class="btn btn-warning fw-bold w-100 py-2">
                    Editar Cliente
                </a>

            </div>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        function confirmarEliminacion(idMovimiento, idCliente) {
            if (confirm("⚠️ ¿Eliminar este movimiento?\nEsto actualizará el saldo del cliente.")) {
                window.location.href = 
                    '${pageContext.request.contextPath}/movimientos/eliminar?id=' + idMovimiento + '&idCliente=' + idCliente;
            }
        }
    </script>

</body>
</html>