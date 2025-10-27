<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sign Up - Sistema de Créditos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/registro.css">
</head>
<body>

    <div class="registro-card">
        <h3 class="registro-title">Sign Up</h3>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger py-2" role="alert">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/usuarios/registro" method="POST" id="formRegistro">
            <input type="hidden" name="action" value="registrar">

            <div class="mb-3">
                <label for="username">Crear usuario:</label>
                <input type="text" 
                       class="form-control" 
                       id="username" 
                       name="username" 
                       placeholder="usuario123"
                       pattern="[a-zA-Z0-9_]{3,20}"
                       title="Solo letras, números y guión bajo (3-20 caracteres)"
                       required>
            </div>

            <div class="mb-3">
                <label for="clave">Crear contraseña:</label>
                <input type="password" 
                       class="form-control" 
                       id="clave" 
                       name="clave" 
                       placeholder="Min. 4 caracteres"
                       required 
                       minlength="4">
                <div class="password-strength" id="passwordStrength"></div>
                <div class="help-text" id="passwordHelp">Mínimo 4 caracteres</div>
            </div>

            <div class="mb-3">
                <label for="clave_confirmacion">Confirmar contraseña:</label>
                <input type="password" 
                       class="form-control" 
                       id="clave_confirmacion" 
                       name="clave_confirmacion" 
                       placeholder="Repita la contraseña"
                       required 
                       minlength="4">
                <div class="help-text" id="matchMessage"></div>
            </div>

            <div class="mb-3">
                <label for="nombre_completo">Nombres completos:</label>
                <input type="text" 
                       class="form-control" 
                       id="nombre_completo" 
                       name="nombre_completo" 
                       placeholder="Juan Pérez García"
                       required>
            </div>

            <button type="submit" class="btn btn-crear" id="btnRegistrar">Crear usuario</button>

            <a href="${pageContext.request.contextPath}/usuarios/listar" class="btn btn-volver">Volver</a>
        </form>

        <div class="footer-text">Sistema de Créditos</div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/validacion-registro.js"></script>
    
</body>
</html>