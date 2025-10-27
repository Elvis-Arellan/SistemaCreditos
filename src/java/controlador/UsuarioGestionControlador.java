package controlador;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import modelo.Usuario;
import servicios.impl.UsuarioServiciosImpl;
import servicios.interfaces.IUsuarioServicio;

@WebServlet(name = "UsuarioGestionControlador", urlPatterns = {"/usuarios/listar", "/usuarios/eliminar"})
public class UsuarioGestionControlador extends HttpServlet {

    private IUsuarioServicio usuarioServicio;

    @Override
    public void init() throws ServletException {
        usuarioServicio = new UsuarioServiciosImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("rol") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String rol = (String) session.getAttribute("rol");

        if (!"ADMIN".equals(rol)) {
            System.out.println("Acceso denegado: Usuario sin privilegios de ADMIN");
            response.sendRedirect(request.getContextPath() + "/clientes/listar");
            return;
        }

        String path = request.getServletPath();

        if ("/usuarios/listar".equals(path)) {
            listarUsuarios(request, response);
        } else if ("/usuarios/eliminar".equals(path)) {
            eliminarUsuario(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            System.out.println("Listando todos los usuarios del sistema");

            List<Usuario> usuarios = usuarioServicio.obtenerTodosLosUsuarios();

            System.out.println("Usuarios encontrados: " + (usuarios != null ? usuarios.size() : 0));

            request.setAttribute("usuarios", usuarios);
            request.getRequestDispatcher("/usuarios/listar.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar la lista de usuarios");
            request.getRequestDispatcher("/usuarios/listar.jsp").forward(request, response);
        }
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idParam = request.getParameter("id");

            if (idParam == null || idParam.trim().isEmpty()) {
                request.setAttribute("error", "ID de usuario inválido");
                response.sendRedirect(request.getContextPath() + "/usuarios/listar");
                return;
            }

            int idUsuario = Integer.parseInt(idParam);

            HttpSession session = request.getSession();
            Integer idUsuarioLogueado = (Integer) session.getAttribute("idUsuario");

            if (idUsuario == idUsuarioLogueado) {
                System.out.println("No se puede eliminar al usuario actual");
                request.getSession().setAttribute("error", "No puedes eliminar tu propia cuenta");
                response.sendRedirect(request.getContextPath() + "/usuarios/listar");
                return;
            }

            if (idUsuario == 1) {
                System.out.println("No se puede eliminar al administrador principal");
                request.getSession().setAttribute("error", "No se puede eliminar al administrador principal del sistema");
                response.sendRedirect(request.getContextPath() + "/usuarios/listar");
                return;
            }

            boolean eliminado = usuarioServicio.eliminarUsuario(idUsuario);

            if (eliminado) {
                System.out.println("Usuario eliminado correctamente (ID: " + idUsuario + ")");
                request.getSession().setAttribute("mensaje", "Usuario eliminado exitosamente");
            } else {
                System.out.println("No se pudo eliminar el usuario (ID: " + idUsuario + ")");
                request.getSession().setAttribute("error", "No se pudo eliminar el usuario. Puede tener clientes asociados.");
            }

            response.sendRedirect(request.getContextPath() + "/usuarios/listar");

        } catch (NumberFormatException e) {
            System.err.println("ID de usuario inválido");
            request.getSession().setAttribute("error", "ID de usuario inválido");
            response.sendRedirect(request.getContextPath() + "/usuarios/listar");
        } catch (Exception e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al eliminar el usuario");
            response.sendRedirect(request.getContextPath() + "/usuarios/listar");
        }
    }

    @Override
    public String getServletInfo() {
        return "Controlador de Gestión de Usuarios (solo ADMIN)";
    }
}
