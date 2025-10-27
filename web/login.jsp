<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Sistema de Créditos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
</head>
<body>
    <div class="login-card">
        <h3 class="login-title">Login</h3>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger py-2" role="alert">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>
        
        <% if (request.getAttribute("mensaje") != null) { %>
            <div class="alert alert-success py-2" role="alert">
                <%= request.getAttribute("mensaje") %>
            </div>
        <% } %>
        
        <form action="${pageContext.request.contextPath}/login" method="POST">
            <input type="hidden" name="action" value="login">
            
            <div class="mb-3">
                <label for="username">Usuario:</label>
                <input type="text" 
                       class="form-control" 
                       id="username" 
                       name="username" 
                       placeholder="Ingrese su usuario"
                       value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>"
                       required 
                       autofocus>
            </div>
            
            <div class="mb-3">
                <label for="clave">Contraseña:</label>
                <input type="password" 
                       class="form-control" 
                       id="clave" 
                       name="clave" 
                       placeholder="Ingrese su contraseña"
                       required>
            </div>
            
            <button type="submit" class="btn btn-login">
                Ingresar 
            </button>
        </form>
        
        <div class="footer-text">Sistema de Créditos</div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>