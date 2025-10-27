package controlador;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import modelo.Usuario;
import servicios.impl.UsuarioServiciosImpl;
import servicios.interfaces.IUsuarioServicio;

@WebServlet(name = "UsuarioRegistroControlador", urlPatterns = {"/usuarios/registro"})
public class UsuarioRegistroControlador extends HttpServlet {

    private IUsuarioServicio usuarioServicio;

    @Override
    public void init() throws ServletException {
        usuarioServicio = new UsuarioServiciosImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/usuarios/registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("registrar".equals(action)) {
            procesarRegistro(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/usuarios/registro.jsp");
        }
    }

    private void procesarRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String nombreCompleto = request.getParameter("nombre_completo");
            String username = request.getParameter("username");
            String clave = request.getParameter("clave");
            String claveConfirmacion = request.getParameter("clave_confirmacion");

            System.out.println("Intento de registro - Usuario: " + username);

            if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
                request.setAttribute("error", "El nombre completo es obligatorio");
                request.setAttribute("username", username);
                request.getRequestDispatcher("/usuarios/registro.jsp").forward(request, response);
                return;
            }

            if (username == null || username.trim().isEmpty()) {
                request.setAttribute("error", "El nombre de usuario es obligatorio");
                request.setAttribute("nombre_completo", nombreCompleto);
                request.getRequestDispatcher("/usuarios/registro.jsp").forward(request, response);
                return;
            }

            if (clave == null || clave.trim().isEmpty()) {
                request.setAttribute("error", "La contraseña es obligatoria");
                request.setAttribute("nombre_completo", nombreCompleto);
                request.setAttribute("username", username);
                request.getRequestDispatcher("/usuarios/registro.jsp").forward(request, response);
                return;
            }

            if (claveConfirmacion == null || claveConfirmacion.trim().isEmpty()) {
                request.setAttribute("error", "Debe confirmar la contraseña");
                request.setAttribute("nombre_completo", nombreCompleto);
                request.setAttribute("username", username);
                request.getRequestDispatcher("/usuarios/registro.jsp").forward(request, response);
                return;
            }

            if (nombreCompleto.trim().length() < 3) {
                request.setAttribute("error", "El nombre completo debe tener al menos 3 caracteres");
                request.setAttribute("username", username);
                request.getRequestDispatcher("/usuarios/registro.jsp").forward(request, response);
                return;
            }

            if (!username.matches("^[a-zA-Z0-9_]{3,20}$")) {
                request.setAttribute("error", "El usuario debe tener entre 3-20 caracteres (solo letras, números y guión bajo)");
                request.setAttribute("nombre_completo", nombreCompleto);
                request.getRequestDispatcher("/usuarios/registro.jsp").forward(request, response);
                return;
            }

            if (clave.length() < 4) {
                request.setAttribute("error", "La contraseña debe tener al menos 4 caracteres");
                request.setAttribute("nombre_completo", nombreCompleto);
                request.setAttribute("username", username);
                request.getRequestDispatcher("/usuarios/registro.jsp").forward(request, response);
                return;
            }

            if (!clave.equals(claveConfirmacion)) {
                request.setAttribute("error", "Las contraseñas no coinciden");
                request.setAttribute("nombre_completo", nombreCompleto);
                request.setAttribute("username", username);
                request.getRequestDispatcher("/usuarios/registro.jsp").forward(request, response);
                return;
            }

            Usuario usuarioExistente = usuarioServicio.obtenerUsuarioPorUsername(username.trim());
            if (usuarioExistente != null) {
                request.setAttribute("error", "El nombre de usuario ya está en uso. Por favor, elige otro.");
                request.setAttribute("nombre_completo", nombreCompleto);
                request.getRequestDispatcher("/usuarios/registro.jsp").forward(request, response);
                return;
            }

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre_completo(nombreCompleto.trim());
            nuevoUsuario.setUsername(username.trim().toLowerCase());
            nuevoUsuario.setClave(clave);

            boolean registroExitoso = usuarioServicio.registrarUsuario(nuevoUsuario);

            if (registroExitoso) {
                System.out.println("✅ Usuario registrado exitosamente: " + username);

                request.setAttribute("mensaje", "¡Cuenta creada exitosamente! Ya puedes iniciar sesión.");
                request.setAttribute("username", username.toLowerCase());
                request.getRequestDispatcher("/login.jsp").forward(request, response);

            } else {
                System.out.println("❌ Error al registrar usuario: " + username);
                request.setAttribute("error", "Error al crear la cuenta. Por favor, intenta nuevamente.");
                request.setAttribute("nombre_completo", nombreCompleto);
                request.setAttribute("username", username);
                request.getRequestDispatcher("/usuarios/registro.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("❌ Error en procesarRegistro: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar el registro. Intente nuevamente.");
            request.getRequestDispatcher("/usuarios/registro.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Controlador de Registro de Usuarios";
    }
}
