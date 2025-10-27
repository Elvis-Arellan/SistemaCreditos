package controlador;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import modelo.Usuario;
import servicios.impl.UsuarioServiciosImpl;
import servicios.interfaces.IUsuarioServicio;

@WebServlet(name = "UsuarioLoginControlador", urlPatterns = {"/login"})
public class UsuarioLoginControlador extends HttpServlet {

    private IUsuarioServicio usuarioServicio;

    @Override
    public void init() throws ServletException {
        usuarioServicio = new UsuarioServiciosImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("logout".equals(action)) {
            cerrarSesion(request, response);
        } else {
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("login".equals(action)) {
            procesarLogin(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    private void procesarLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String username = request.getParameter("username");
            String clave = request.getParameter("clave");

            if (username == null || username.trim().isEmpty()) {
                request.setAttribute("error", "El nombre de usuario es obligatorio");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            if (clave == null || clave.trim().isEmpty()) {
                request.setAttribute("error", "La contraseña es obligatoria");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            Usuario usuario = usuarioServicio.login(username, clave);

            if (usuario != null) {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                session.setAttribute("idUsuario", usuario.getId());
                session.setAttribute("nombreUsuario", usuario.getUsername());
                session.setAttribute("nombreCompleto", usuario.getNombre_completo());
                session.setAttribute("rol", usuario.getRol());
                session.setMaxInactiveInterval(30 * 60);

                System.out.println("Login exitoso para: " + usuario.getUsername() + " (Rol: " + usuario.getRol() + ")");

                if ("ADMIN".equals(usuario.getRol())) {
                    response.sendRedirect(request.getContextPath() + "/usuarios/listar");
                } else {
                    response.sendRedirect(request.getContextPath() + "/clientes/listar");
                }

            } else {
                System.out.println("Login fallido para usuario: " + username);
                request.setAttribute("error", "Usuario o contraseña incorrectos");
                request.setAttribute("username", username);
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("Error en procesarLogin: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar el login. Intente nuevamente.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private void cerrarSesion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            String nombreUsuario = (String) session.getAttribute("nombreUsuario");
            System.out.println("Usuario cerró sesión: " + nombreUsuario);
            session.invalidate();
        }

        request.setAttribute("mensaje", "Sesión cerrada exitosamente");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controlador de Login de Usuarios";
    }
}
