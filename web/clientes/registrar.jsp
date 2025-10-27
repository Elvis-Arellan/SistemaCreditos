<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (session == null || session.getAttribute("idUsuario") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Sistema de Créditos</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="../assets/estilos.css">
    </head>

    <body>
        <header>
            <div class="container d-flex justify-content-between align-items-center">
                <h3 class="titulo">Registrar Cliente</h3>
            </div>
        </header>

        <main class="container mt-5 pt-5">

            <% if (request.getAttribute("error") != null) {%>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>❌ Error:</strong> <%= request.getAttribute("error")%>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <% }%>

            <div class="d-flex justify-content-center gap-3 mb-4">
                <div class="text mb-4">
                    <a href="${pageContext.request.contextPath}/clientes/listar" class="btn" style="background-color: #192AE6; color: white;">Regresar</a>
                </div>
                <button type="submit" form="formCliente" class="btn btn-success btn-lg fw-bold px-5 py-3 rounded-4">Guardar Cliente</button>
                <a href="${pageContext.request.contextPath}/clientes/listar" 
                   class="btn btn-danger btn-lg fw-bold px-5 py-3 rounded-4 text-decoration-none text-white">
                    Cancelar
                </a>
            </div>

            <div class="carrito-container p-4 rounded-4 mx-auto" style="max-width: 600px;">
                <h4 class="fw-bold mb-4 text-white">Agregar Cliente</h4>

                <form action="${pageContext.request.contextPath}/clientes/guardar" method="POST" id="formCliente">
                    <div class="mb-3">
                        <label class="form-label text-secondary">Nombres</label>
                        <input type="text" 
                               name="nombre" 
                               id="nombre"
                               class="form-control rounded-pill bg-transparent text-white border-light"
                               value="<%= request.getAttribute("nombre") != null ? request.getAttribute("nombre") : ""%>"
                               required
                               autofocus>
                    </div>

                    <div class="mb-3">
                        <label class="form-label text-secondary">Apellidos</label>
                        <input type="text" 
                               name="apellido" 
                               id="apellido"
                               class="form-control rounded-pill bg-transparent text-white border-light"
                               value="<%= request.getAttribute("apellido") != null ? request.getAttribute("apellido") : ""%>"
                               required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label text-secondary">Email</label>
                        <input type="email" 
                               name="email" 
                               id="email"
                               class="form-control rounded-pill bg-transparent text-white border-light"
                               value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : ""%>">
                    </div>

                    <div class="mb-3">
                        <label class="form-label text-secondary">Teléfono</label>
                        <input type="text" 
                               name="telefono" 
                               id="telefono"
                               class="form-control rounded-pill bg-transparent text-white border-light"
                               pattern="[0-9]{7,15}"
                               title="Solo números, entre 7 y 15 dígitos"
                               value="<%= request.getAttribute("telefono") != null ? request.getAttribute("telefono") : ""%>"
                               required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label text-secondary">Saldo</label>
                        <input type="number" 
                               name="saldo" 
                               id="saldo"
                               value="0.00"
                               step="0.01"
                               min="0"
                               class="form-control rounded-pill bg-transparent text-white border-light">
                    </div>

                    <div class="row mb-3">
                        <div class="col">
                            <label class="form-label text-secondary d-block">Estado</label>
                            <div class="btn-group" role="group">
                                <input type="radio" class="btn-check" name="estado" id="activo" value="ACTIVO" checked>
                                <label class="btn btn-outline-light rounded-pill me-2" for="activo">Activo</label>

                                <input type="radio" class="btn-check" name="estado" id="inactivo" value="INACTIVO">
                                <label class="btn btn-outline-light rounded-pill" for="inactivo">Inactivo</label>
                            </div>
                        </div>
                        <div class="col">
                            <label class="form-label text-secondary">Fecha</label>
                            <input type="text" 
                                   value="<%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>" 
                                   class="form-control rounded-pill bg-transparent text-white border-light text-center"
                                   readonly>
                        </div>
                    </div>
                </form>
            </div>
        </main>


        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>