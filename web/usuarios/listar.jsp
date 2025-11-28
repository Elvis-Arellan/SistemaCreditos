<%@page import="java.util.List, modelo.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String rol = (String) session.getAttribute("rol");
    if (rol == null || !"ADMIN".equals(rol)) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Gestión de Usuarios - ADMIN</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-panel.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/estilos.css" />
    </head>

    <body>
        <header style="background: #192AE6; padding: 15px 0; margin-bottom: 20px;">
            <div class="container d-flex justify-content-between align-items-center">
                <a href="${pageContext.request.contextPath}/usuarios/listar" class="titulo text-white text-decoration-none">
                    Panel de Administración
                </a>

                <div class="d-flex align-items-center gap-3">
                    <span class="admin-badge">ADMIN</span>

                    <span class="text-white">
                        <strong><%= session.getAttribute("nombreCompleto") != null ? session.getAttribute("nombreCompleto") : "Administrador"%></strong>
                    </span>

                    <a href="${pageContext.request.contextPath}/login?action=logout" 
                       class="btn btn-danger btn-sm">
                        Cerrar Sesión
                    </a>
                </div>
            </div>
        </header>

        <main class="container py-5">

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

            <div class="row g-4">

                <div class="col-lg-9">
                    <div class="carrito-container p-4 rounded tabla-container">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h4 class="fw-bold mb-0">Usuarios del Sistema</h4>
                            <div class="input-group w-50">
                                <input type="text" 
                                       id="buscarUsuario" 
                                       class="form-control rounded-pill" 
                                       placeholder="🔍 Buscar usuario...">
                            </div>
                        </div>

                        <table class="table table-transparent text-center align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Usuario</th>
                                    <th>Nombre Completo</th>
                                    <th>Rol</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody id="tablaUsuarios">
                                <% if (usuarios != null && !usuarios.isEmpty()) {
                                        int count = 1;
                                        for (Usuario u : usuarios) {%>
                                <tr>
                                    <td class="text-danger fw-bold"><%= count%></td>
                                    <td><%= u.getUsername()%></td>
                                    <td><%= u.getNombre_completo()%></td>
                                    <td>
                                        <% if ("ADMIN".equals(u.getRol())) { %>
                                        <span class="admin-badge">ADMIN</span>
                                        <% } else { %>
                                        <span class="usuario-badge">USUARIO</span>
                                        <% } %>
                                    </td>
                                    <td>
                                        <% if (u.getId() == 1) { %>
                                        <span class="text-muted admin-badge">
                                            🔒 Protegido
                                        </span>
                                        <% } else if (u.getId() == (Integer) session.getAttribute("idUsuario")) { %>
                                        <span class="text-warning">
                                            Tu cuenta
                                        </span>
                                        <% } else {%>
                                        <button class="btn btn-danger btn-sm fw-bold" 
                                                onclick="confirmarEliminacion(<%= u.getId()%>, '<%= u.getUsername()%>')">
                                            Eliminar
                                        </button>
                                        <% } %>
                                    </td>
                                </tr>
                                <% count++;
                                    }
                                } else { %>
                                <tr>
                                    <td colspan="5" class="text-muted">
                                        No hay usuarios registrados
                                    </td>
                                </tr>
                                <% }%>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="col-lg-3 d-flex flex-column gap-4">
                    <div class="carrito-container p-4 rounded text-center">
                        <h4 class="fw-bold mb-2">Total Usuarios</h4>
                        <h3 class="fw-bold" style="color: #E64A19;">
                            <%= (usuarios != null) ? usuarios.size() : 0%>
                        </h3>
                    </div>

                    <div class="carrito-container p-4 rounded text-center">
                        <h4 class="fw-bold mb-2">Administradores</h4>
                        <h3 class="fw-bold" style="color: #667eea;">
                            <%
                                int countAdmin = 0;
                                if (usuarios != null) {
                                    for (Usuario u : usuarios) {
                                        if ("ADMIN".equals(u.getRol())) {
                                            countAdmin++;
                                        }
                                    }
                                }
                            %>
                            <%= countAdmin%>
                        </h3>
                    </div>

                    <div class="carrito-container p-4 rounded text-center">
                        <h4 class="fw-bold mb-2">Usuarios Normales</h4>
                        <h3 class="fw-bold" style="color: #28a745;">
                            <%
                                int countUsuarios = 0;
                                if (usuarios != null) {
                                    for (Usuario u : usuarios) {
                                        if ("USUARIO".equals(u.getRol())) {
                                            countUsuarios++;
                                        }
                                    }
                                }
                            %>
                            <%= countUsuarios%>
                        </h3>
                    </div>
                    <a href="${pageContext.request.contextPath}/usuarios/registro" 
                       class="usuario-badge w-100 text-center">
                        Crear Nuevo Usuario
                    </a>  
                </div>
            </div>
        </main>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/busqueda.js"></script>

        <script>
            function confirmarEliminacion(id, username) {
                if (confirm("⚠️ ¿Eliminar al usuario '" + username + "'?\nEsto NO se puede deshacer.")) {
                    window.location.href = '${pageContext.request.contextPath}/usuarios/eliminar?id=' + id;
                }
            }
        </script>

    </body>
</html>