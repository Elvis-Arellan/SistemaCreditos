package controlador;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import modelo.Cliente;
import servicios.impl.ClienteServiciosImpl;

@WebServlet(name = "ClienteControlador", urlPatterns = {
    "/clientes/listar",
    "/clientes/registrar",
    "/clientes/guardar"
})
public class ClienteControlador extends HttpServlet {

    private ClienteServiciosImpl clienteService = new ClienteServiciosImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            System.out.println("No hay sesión activa. Redirigiendo al login.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String rol = (String) session.getAttribute("rol");
        if ("ADMIN".equals(rol)) {
            System.out.println("Los administradores no gestionan clientes");
            response.sendRedirect(request.getContextPath() + "/usuarios/listar");
            return;
        }

        if ("/clientes/listar".equals(path)) {
            listarClientes(request, response);
        } else if ("/clientes/registrar".equals(path)) {
            mostrarFormularioRegistro(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if ("/clientes/guardar".equals(path)) {
            guardarCliente(request, response);
        }
    }

    private void listarClientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            System.out.println("ClienteControlador - listarClientes ejecutado");

            HttpSession session = request.getSession();
            Integer idUsuario = (Integer) session.getAttribute("idUsuario");

            System.out.println("Usuario logueado ID: " + idUsuario);

            List<Cliente> clientes = clienteService.obtenerClientesPorUsuario(idUsuario);
            System.out.println("📊 Clientes del usuario " + idUsuario + ": " + (clientes != null ? clientes.size() : 0));

            BigDecimal saldoTotal = BigDecimal.ZERO;
            if (clientes != null && !clientes.isEmpty()) {
                for (Cliente c : clientes) {
                    if (c.getSaldo() != null) {
                        saldoTotal = saldoTotal.add(c.getSaldo());
                    }
                    System.out.println("👤 Cliente: " + c.getNombre() + " " + c.getApellido()
                            + " - Saldo: S/." + c.getSaldo());
                }
            } else {
                System.out.println("Este usuario no tiene clientes registrados");
            }

            System.out.println("Saldo Total: S/." + saldoTotal);

            request.setAttribute("clientes", clientes);
            request.setAttribute("saldoTotal", saldoTotal);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/clientes/listar.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            System.err.println("Error en listarClientes: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar la lista de clientes");
            request.getRequestDispatcher("/clientes/listar.jsp").forward(request, response);
        }
    }

    private void mostrarFormularioRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("Mostrando formulario de registro de cliente");
        request.getRequestDispatcher("/clientes/registrar.jsp").forward(request, response);
    }

    private void guardarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            Integer idUsuario = (Integer) session.getAttribute("idUsuario");

            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            String saldoStr = request.getParameter("saldo");
            String estado = request.getParameter("estado");

            System.out.println("📝 Intentando guardar cliente:");
            System.out.println("   Nombre: " + nombre);
            System.out.println("   Apellido: " + apellido);
            System.out.println("   Usuario ID: " + idUsuario);

            if (nombre == null || nombre.trim().isEmpty()) {
                request.setAttribute("error", "El nombre es obligatorio");
                request.setAttribute("apellido", apellido);
                request.setAttribute("email", email);
                request.setAttribute("telefono", telefono);
                request.getRequestDispatcher("/clientes/registrar.jsp").forward(request, response);
                return;
            }

            if (apellido == null || apellido.trim().isEmpty()) {
                request.setAttribute("error", "El apellido es obligatorio");
                request.setAttribute("nombre", nombre);
                request.setAttribute("email", email);
                request.setAttribute("telefono", telefono);
                request.getRequestDispatcher("/clientes/registrar.jsp").forward(request, response);
                return;
            }

            if (telefono == null || telefono.trim().isEmpty()) {
                request.setAttribute("error", "El teléfono es obligatorio");
                request.setAttribute("nombre", nombre);
                request.setAttribute("apellido", apellido);
                request.setAttribute("email", email);
                request.getRequestDispatcher("/clientes/registrar.jsp").forward(request, response);
                return;
            }

            if (!telefono.matches("^[0-9]{7,15}$")) {
                request.setAttribute("error", "El teléfono debe tener entre 7 y 15 dígitos numéricos");
                request.setAttribute("nombre", nombre);
                request.setAttribute("apellido", apellido);
                request.setAttribute("email", email);
                request.getRequestDispatcher("/clientes/registrar.jsp").forward(request, response);
                return;
            }

            if (email != null && !email.trim().isEmpty()) {
                if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                    request.setAttribute("error", "El formato del email no es válido");
                    request.setAttribute("nombre", nombre);
                    request.setAttribute("apellido", apellido);
                    request.setAttribute("telefono", telefono);
                    request.getRequestDispatcher("/clientes/registrar.jsp").forward(request, response);
                    return;
                }
            }

            BigDecimal saldo;
            try {
                saldo = new BigDecimal(saldoStr != null && !saldoStr.trim().isEmpty() ? saldoStr : "0");

                if (saldo.compareTo(BigDecimal.ZERO) < 0) {
                    request.setAttribute("error", "El saldo no puede ser negativo");
                    request.setAttribute("nombre", nombre);
                    request.setAttribute("apellido", apellido);
                    request.setAttribute("email", email);
                    request.setAttribute("telefono", telefono);
                    request.getRequestDispatcher("/clientes/registrar.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "El saldo debe ser un número válido");
                request.setAttribute("nombre", nombre);
                request.setAttribute("apellido", apellido);
                request.setAttribute("email", email);
                request.setAttribute("telefono", telefono);
                request.getRequestDispatcher("/clientes/registrar.jsp").forward(request, response);
                return;
            }

            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setId_usuario(idUsuario);
            nuevoCliente.setNombre(nombre.trim());
            nuevoCliente.setApellido(apellido.trim());
            nuevoCliente.setEmail(email != null && !email.trim().isEmpty() ? email.trim() : null);
            nuevoCliente.setTelefono(telefono.trim());
            nuevoCliente.setSaldo(saldo);
            nuevoCliente.setEstado(estado != null && !estado.isEmpty() ? estado : "ACTIVO");

            boolean guardado = clienteService.registrarCliente(nuevoCliente);

            if (guardado) {
                System.out.println("Cliente registrado exitosamente");
                session.setAttribute("mensaje", "Cliente registrado exitosamente");
                response.sendRedirect(request.getContextPath() + "/clientes/listar");
            } else {
                System.out.println("Error al guardar el cliente");
                request.setAttribute("error", "Error al registrar el cliente. Intente nuevamente.");
                request.setAttribute("nombre", nombre);
                request.setAttribute("apellido", apellido);
                request.setAttribute("email", email);
                request.setAttribute("telefono", telefono);
                request.getRequestDispatcher("/clientes/registrar.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("Error en guardarCliente: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar el registro del cliente");
            request.getRequestDispatcher("/clientes/registrar.jsp").forward(request, response);
        }
    }
}
