<%@page import="java.math.BigDecimal"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, modelo.Cliente"%>
<%
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
    BigDecimal saldoTotal = (BigDecimal) request.getAttribute("saldoTotal");
    System.out.println("🟡 JSP listar.jsp cargado - Clientes: " + (clientes != null ? clientes.size() : "null"));
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Sistema de Créditos</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/estilos.css" />
    </head>

    <body>

        <div class="container mt-3">
            <div class="alert alert-info">
                <strong>Depuración:</strong> 
                Clientes en request: <%= clientes != null ? clientes.size() : "null"%>
            </div>
        </div>


        <header style="background: #192AE6; padding: 15px 0; margin-bottom: 20px;">
            <div class="container d-flex justify-content-between align-items-center">
                <a href="${pageContext.request.contextPath}/clientes/listar" class="titulo text-white text-decoration-none">
                     Sistema de créditos para MiniMarkets
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
            <div class="text mb-4">
                <a href="${pageContext.request.contextPath}/clientes/registrar.jsp" class="btn" style="background-color: #192AE6; color: white;">Agregar Clientes --></a>
            </div>

            <div class="row g-4">

                <div class="col-lg-8">
                    <div class="carrito-container p-4 rounded tabla-container">

                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h4 class="fw-bold mb-0">Listado de Clientes</h4>
                            <div class="input-group w-50">
                                <input type="text" class="form-control rounded-pill" placeholder="Buscar...">
                            </div>
                        </div>

                        <table class="table table-transparent text-center align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Nombres</th>
                                    <th>Fecha Registro</th>
                                    <th>Estado</th>
                                    <th>Teléfono</th>
                                    <th>Saldo</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (clientes != null && !clientes.isEmpty()) {
                                        int count = 1;
                                        for (Cliente c : clientes) {%>
                                <tr>
                                    <td class="text-danger fw-bold"><%= count%></td>
                                    <td><%= c.getNombre()%> <%= c.getApellido()%></td>
                                    <td><%= c.getFecha_registro()%></td>
                                    <td>
                                        <% if ("ACTIVO".equals(c.getEstado())) {%>
                                        <span class="text-success fw-bold"><%= c.getEstado()%></span>
                                        <% } else {%>
                                        <span class="text-danger fw-bold"><%= c.getEstado()%></span>
                                        <% }%>
                                    </td>
                                    <td><%= c.getTelefono()%></td>
                                    <td class="text-danger fw-bold">
                                        S/. <%= c.getSaldo() != null ? c.getSaldo() : "0.00"%>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/clientes/editar?id=<%= c.getId_cliente()%>" class="btn btn-warning btn-sm fw-bold">Editar</a>
                                        <a href="${pageContext.request.contextPath}/movimientos/historial.jsp?id=<%= c.getId_cliente()%>" class="btn btn-danger btn-sm fw-bold">Historial</a>
                                    </td>
                                </tr>
                                <% count++;
                                    }
                                } else { %>
                                <tr>
                                    <td colspan="7" ">  
                                         No tienes clientes registrados.
                                    </td>
                                </tr>
                                <% }%>
                            </tbody>
                        </table>
                    </div>
                </div>


                <div class="col-lg-4 d-flex flex-column gap-4">
                    <div class="carrito-container p-4 rounded text-center">
                        <h4 class="fw-bold mb-2">Saldo Total</h4>
                        <h3 class="fw-bold" style="color: #E64A19;"> S/. <%= saldoTotal != null ? String.format("%.2f", saldoTotal) : "0.00"%></h3>
                    </div>

                    <div class="carrito-container p-4 rounded text-center">
                        <h4 class="fw-bold mb-2">Total de Clientes</h4>
                        <h3 class="fw-bold" style="color: #E64A19;">
                            <%= (clientes != null) ? clientes.size() : 0%>
                        </h3>
                    </div>
                </div>
            </div>
        </main>

        <footer>
            <div class="container">
                <div class="row mb-4">
                    <div class="col-md-3">
                        <h4>Sistema de Créditos</h4>
                        <p class="small text-secondary">Venta de abarrotes y víveres para consumo diario</p>
                    </div>
                    <div class="col-md-3">
                        <h4>Tienda</h4>
                        <ul class="list-unstyled">
                            <li><a href="${pageContext.request.contextPath}/index.jsp">Todos los clientes</a></li>
                            <li><a href="#">Nuevos clientes</a></li>
                            <li><a href="#">Deudores</a></li>
                        </ul>
                    </div>
                    <div class="col-md-3">
                        <h4>Empresa</h4>
                        <ul class="list-unstyled">
                            <li><a href="${pageContext.request.contextPath}/nosotros.jsp">Nosotros</a></li>
                            <li><a href="https://www.google.com.pe/maps/place/Gamarra,+La+Victoria+15018" target="_blank">Ubicación</a></li>
                        </ul>
                    </div>
                    <div class="col-md-3">
                        <h4>Contacto</h4>
                        <p class="text-secondary mb-1">Av. Javier Prado Este, 4200 Santiago de Surco</p>
                        <p class="text-secondary mb-1">+51 992567855</p>
                        <p class="text-secondary mb-1">minimarket@gmail.com</p>
                    </div>
                </div>

                <div class="footer-bottom">
                    <p>© 2025 Elvis Arellan. Todos los derechos reservados.</p>
                    <div>
                        <a href="#">Política de privacidad</a>
                        <a href="#" class="ms-3">Términos del servicio</a>
                    </div>
                </div>
            </div>
        </footer>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>