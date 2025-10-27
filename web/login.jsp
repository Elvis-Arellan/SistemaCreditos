<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Sistema de Créditos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #0a0a0a;
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: "Poppins", sans-serif;
        }

        .login-card {
            background-color: #111827; 
            border-radius: 20px;
            padding: 40px;
            width: 340px;
            box-shadow: 0 0 25px rgba(0, 0, 0, 0.6);
            text-align: center;
        }

        .login-title {
            color: #e5e7eb; 
            font-weight: 600;
            font-size: 1.6rem;
            margin-bottom: 25px;
        }

        label {
            color: #d1d5db;
            font-weight: 500;
            font-size: 0.9rem;
            display: block;
            text-align: left;
            margin-bottom: 6px;
        }

        .form-control {
            background-color: #1f2937;
            border: 1px solid #374151;
            border-radius: 20px;
            color: #f9fafb;
            padding: 10px 15px;
        }

        .form-control:focus {
            background-color: #1f2937;
            border-color: #2563eb;
            box-shadow: 0 0 0 2px rgba(37,99,235,0.3);
            color: #fff;
        }

        .btn-login {
            background-color: #2563eb;
            border: none;
            border-radius: 6px;
            color: white;
            padding: 10px;
            font-weight: 600;
            width: 100%;
            transition: 0.3s ease;
        }

        .btn-login:hover {
            background-color: #1e40af;
        }

        .form-check-label {
            color: #9ca3af;
        }

        .footer-text {
            color: #6b7280;
            font-size: 0.8rem;
            margin-top: 20px;
        }
    </style>
</head>
<body>

    <div class="login-card">
        <h3 class="login-title">Login</h3>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger py-2" role="alert">
                <%= request.getAttribute("error") %>
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
                       required autofocus>
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
