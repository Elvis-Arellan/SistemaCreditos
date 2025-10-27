<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sign Up - Sistema de Créditos</title>
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

        .registro-card {
            background-color: #111827;
            border-radius: 20px;
            padding: 40px;
            width: 360px;
            box-shadow: 0 0 25px rgba(0, 0, 0, 0.6);
            text-align: center;
        }

        .registro-title {
            color: #e5e7eb;
            font-weight: 600;
            font-size: 1.6rem;
            margin-bottom: 20px;
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
            box-shadow: 0 0 0 2px rgba(37,99,235,0.16);
            color: #fff;
        }

        .password-strength {
            height: 6px;
            margin-top: 6px;
            border-radius: 6px;
            transition: all 0.3s ease;
            background: transparent;
        }
        .strength-weak { background: #ef4444; width: 33%; }
        .strength-medium { background: #f59e0b; width: 66%; }
        .strength-strong { background: #10b981; width: 100%; }

        .btn-crear {
            background-color: #10b981;
            border: none;
            border-radius: 8px;
            color: white;
            padding: 10px;
            font-weight: 600;
            width: 100%;
            transition: 0.2s ease;
        }

        .btn-crear[disabled] {
            opacity: 0.6;
            cursor: not-allowed;
        }

        .btn-crear:hover:not([disabled]) {
            background-color: #059669;
        }

        .btn-volver {
            background-color: #2563eb;
            border: none;
            border-radius: 8px;
            color: white;
            padding: 10px;
            font-weight: 600;
            width: 100%;
            margin-top: 10px;
            transition: 0.3s ease;
        }

        .btn-volver:hover {
            background-color: #1e40af;
        }

        .help-text {
            font-size: 0.85rem;
            color: #9ca3af;
            margin-top: 6px;
            text-align: left;
        }
        .match-true { color: #10b981; font-weight: 600; }
        .match-false { color: #ef4444; font-weight: 600; }
        .footer-text {
            color: #6b7280;
            font-size: 0.8rem;
            margin-top: 20px;
        }
    </style>
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
                <input type="text" class="form-control" id="username" name="username" required>
            </div>

            <div class="mb-3">
                <label for="clave">Crear contraseña:</label>
                <input type="password" class="form-control" id="clave" name="clave" required minlength="4">
                <div class="password-strength" id="passwordStrength"></div>
                <div class="help-text" id="passwordHelp">Mínimo 4 caracteres</div>
            </div>

            <div class="mb-3">
                <label for="clave_confirmacion">Confirmar contraseña:</label>
                <input type="password" class="form-control" id="clave_confirmacion" name="clave_confirmacion" required minlength="4">
                <div class="help-text" id="matchMessage"></div>
            </div>

            <div class="mb-3">
                <label for="nombre_completo">Nombres completos:</label>
                <input type="text" class="form-control" id="nombre_completo" name="nombre_completo" required>
            </div>

            <button type="submit" class="btn btn-crear" id="btnRegistrar" >Crear usuario</button>

            <a href="${pageContext.request.contextPath}/usuarios/listar" class="btn btn-volver">Volver</a>
        </form>

        <div class="footer-text">Sistema de Créditos</div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    
</body>
</html>