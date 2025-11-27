package controlador;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import modelo.Movimiento;
import modelo.Cliente;
import servicios.impl.MovimientoServiciosImpl;
import servicios.impl.ClienteServiciosImpl;

@WebServlet(name = "MovimientoControlador", urlPatterns = {
    "/movimientos/historial",
    "/movimientos/registrar",
    "/movimientos/guardar",
    "/movimientos/eliminar"
})
public class MovimientoControlador extends HttpServlet {

    private MovimientoServiciosImpl movimientoService = new MovimientoServiciosImpl();
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
            System.out.println("Los administradores no gestionan movimientos");
            response.sendRedirect(request.getContextPath() + "/usuarios/listar");
            return;
        }

        if ("/movimientos/historial".equals(path)) {
            mostrarHistorial(request, response);
        } else if ("/movimientos/registrar".equals(path)) {
            mostrarFormularioRegistro(request, response);
        } else if ("/movimientos/eliminar".equals(path)) {
            eliminarMovimiento(request, response);
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

        if ("/movimientos/guardar".equals(path)) {
            guardarMovimiento(request, response);
        }
    }

    private void mostrarHistorial(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idClienteParam = request.getParameter("id");

            if (idClienteParam == null || idClienteParam.trim().isEmpty()) {
                System.err.println("❌ ID de cliente no proporcionado");
                request.getSession().setAttribute("error", "ID de cliente inválido");
                response.sendRedirect(request.getContextPath() + "/clientes/listar");
                return;
            }

            int idCliente = Integer.parseInt(idClienteParam);
            System.out.println("📊 Cargando historial de movimientos - Cliente ID: " + idCliente);

            // Verificar que el cliente pertenece al usuario logueado
            HttpSession session = request.getSession();
            Integer idUsuario = (Integer) session.getAttribute("idUsuario");

            Cliente cliente = clienteService.obtenerClientePorId(idCliente);

            if (cliente == null) {
                System.err.println("❌ Cliente no encontrado con ID: " + idCliente);
                request.getSession().setAttribute("error", "Cliente no encontrado");
                response.sendRedirect(request.getContextPath() + "/clientes/listar");
                return;
            }

            if (cliente.getId_usuario() != idUsuario) {
                System.err.println("⛔ Usuario " + idUsuario + " intentó ver movimientos de cliente de otro usuario");
                request.getSession().setAttribute("error", "No tienes permiso para ver este historial");
                response.sendRedirect(request.getContextPath() + "/clientes/listar");
                return;
            }

            // Obtener movimientos del cliente
            List<Movimiento> movimientos = movimientoService.obtenerMovimientosPorCliente(idCliente);
            System.out.println("📋 Movimientos encontrados: " + (movimientos != null ? movimientos.size() : 0));

            // Calcular totales
            BigDecimal totalFiado = BigDecimal.ZERO;
            BigDecimal totalPagado = BigDecimal.ZERO;

            if (movimientos != null && !movimientos.isEmpty()) {
                for (Movimiento m : movimientos) {
                    if ("FIADO".equals(m.getTipo_movimiento())) {
                        totalFiado = totalFiado.add(m.getMonto());
                    } else if ("PAGO".equals(m.getTipo_movimiento())) {
                        totalPagado = totalPagado.add(m.getMonto());
                    }
                }
            }

            request.setAttribute("cliente", cliente);
            request.setAttribute("movimientos", movimientos);
            request.setAttribute("totalFiado", totalFiado);
            request.setAttribute("totalPagado", totalPagado);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/movimientos/historial.jsp");
            dispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            System.err.println("❌ ID de cliente inválido: " + e.getMessage());
            request.getSession().setAttribute("error", "ID de cliente inválido");
            response.sendRedirect(request.getContextPath() + "/clientes/listar");
        } catch (Exception e) {
            System.err.println("❌ Error al mostrar historial: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cargar el historial");
            response.sendRedirect(request.getContextPath() + "/clientes/listar");
        }
    }

    private void mostrarFormularioRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idClienteParam = request.getParameter("id");

            if (idClienteParam == null || idClienteParam.trim().isEmpty()) {
                System.err.println("❌ ID de cliente no proporcionado");
                request.getSession().setAttribute("error", "ID de cliente inválido");
                response.sendRedirect(request.getContextPath() + "/clientes/listar");
                return;
            }

            int idCliente = Integer.parseInt(idClienteParam);

            // Verificar que el cliente pertenece al usuario logueado
            HttpSession session = request.getSession();
            Integer idUsuario = (Integer) session.getAttribute("idUsuario");

            Cliente cliente = clienteService.obtenerClientePorId(idCliente);

            if (cliente == null) {
                System.err.println("❌ Cliente no encontrado con ID: " + idCliente);
                request.getSession().setAttribute("error", "Cliente no encontrado");
                response.sendRedirect(request.getContextPath() + "/clientes/listar");
                return;
            }

            if (cliente.getId_usuario() != idUsuario) {
                System.err.println("⛔ Usuario " + idUsuario + " intentó registrar movimiento para cliente de otro usuario");
                request.getSession().setAttribute("error", "No tienes permiso para registrar movimientos a este cliente");
                response.sendRedirect(request.getContextPath() + "/clientes/listar");
                return;
            }

            System.out.println("✅ Mostrando formulario de registro de movimiento para cliente: " + cliente.getNombre());

            request.setAttribute("cliente", cliente);
            request.getRequestDispatcher("/movimientos/registrar.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("❌ Error al mostrar formulario de registro: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cargar el formulario");
            response.sendRedirect(request.getContextPath() + "/clientes/listar");
        }
    }

    private void guardarMovimiento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            Integer idUsuario = (Integer) session.getAttribute("idUsuario");

            String idClienteParam = request.getParameter("id_cliente");
            String tipoMovimiento = request.getParameter("tipo_movimiento");
            String montoStr = request.getParameter("monto");
            String descripcion = request.getParameter("descripcion");

            System.out.println("📝 Intentando guardar movimiento:");
            System.out.println("   Cliente ID: " + idClienteParam);
            System.out.println("   Tipo: " + tipoMovimiento);
            System.out.println("   Monto: " + montoStr);

            if (idClienteParam == null || idClienteParam.trim().isEmpty()) {
                request.setAttribute("error", "ID de cliente no proporcionado");
                response.sendRedirect(request.getContextPath() + "/clientes/listar");
                return;
            }

            int idCliente = Integer.parseInt(idClienteParam);

            // Verificar permisos
            Cliente cliente = clienteService.obtenerClientePorId(idCliente);
            if (cliente == null || cliente.getId_usuario() != idUsuario) {
                request.getSession().setAttribute("error", "No tienes permiso para realizar esta operación");
                response.sendRedirect(request.getContextPath() + "/clientes/listar");
                return;
            }

            // Validaciones
            if (tipoMovimiento == null || tipoMovimiento.trim().isEmpty()) {
                request.setAttribute("error", "El tipo de movimiento es obligatorio");
                request.setAttribute("cliente", cliente);
                request.getRequestDispatcher("/movimientos/registrar.jsp").forward(request, response);
                return;
            }

            if (montoStr == null || montoStr.trim().isEmpty()) {
                request.setAttribute("error", "El monto es obligatorio");
                request.setAttribute("cliente", cliente);
                request.getRequestDispatcher("/movimientos/registrar.jsp").forward(request, response);
                return;
            }

            BigDecimal monto;
            try {
                monto = new BigDecimal(montoStr);

                if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                    request.setAttribute("error", "El monto debe ser mayor a cero");
                    request.setAttribute("cliente", cliente);
                    request.getRequestDispatcher("/movimientos/registrar.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "El monto debe ser un número válido");
                request.setAttribute("cliente", cliente);
                request.getRequestDispatcher("/movimientos/registrar.jsp").forward(request, response);
                return;
            }

            // Crear movimiento
            Movimiento nuevoMovimiento = new Movimiento();
            nuevoMovimiento.setId_cliente(idCliente);
            nuevoMovimiento.setTipo_movimiento(tipoMovimiento.toUpperCase());
            nuevoMovimiento.setMonto(monto);
            nuevoMovimiento.setDescripcion(descripcion != null && !descripcion.trim().isEmpty() ? descripcion.trim() : null);

            boolean guardado = movimientoService.registrarMovimiento(nuevoMovimiento);

            if (guardado) {
                System.out.println("✅ Movimiento registrado exitosamente");
                session.setAttribute("mensaje", "Movimiento registrado exitosamente");
                response.sendRedirect(request.getContextPath() + "/movimientos/historial?id=" + idCliente);
            } else {
                System.out.println("❌ Error al guardar el movimiento");
                request.setAttribute("error", "Error al registrar el movimiento. Intente nuevamente.");
                request.setAttribute("cliente", cliente);
                request.getRequestDispatcher("/movimientos/registrar.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("❌ Error en guardarMovimiento: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al procesar el movimiento");
            response.sendRedirect(request.getContextPath() + "/clientes/listar");
        }
    }

    private void eliminarMovimiento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idMovimientoParam = request.getParameter("id");
            String idClienteParam = request.getParameter("idCliente");

            if (idMovimientoParam == null || idMovimientoParam.trim().isEmpty()) {
                request.getSession().setAttribute("error", "ID de movimiento inválido");
                response.sendRedirect(request.getContextPath() + "/clientes/listar");
                return;
            }

            int idMovimiento = Integer.parseInt(idMovimientoParam);
            int idCliente = Integer.parseInt(idClienteParam);

            HttpSession session = request.getSession();
            Integer idUsuario = (Integer) session.getAttribute("idUsuario");

            // Verificar permisos
            Movimiento movimiento = movimientoService.obtenerMovimientoPorId(idMovimiento);
            if (movimiento == null) {
                request.getSession().setAttribute("error", "Movimiento no encontrado");
                response.sendRedirect(request.getContextPath() + "/movimientos/historial?id=" + idCliente);
                return;
            }

            Cliente cliente = clienteService.obtenerClientePorId(movimiento.getId_cliente());
            if (cliente == null || cliente.getId_usuario() != idUsuario) {
                System.err.println("⛔ Usuario " + idUsuario + " intentó eliminar movimiento de otro usuario");
                request.getSession().setAttribute("error", "No tienes permiso para eliminar este movimiento");
                response.sendRedirect(request.getContextPath() + "/clientes/listar");
                return;
            }

            boolean eliminado = movimientoService.eliminarMovimiento(idMovimiento);

            if (eliminado) {
                System.out.println("✅ Movimiento eliminado exitosamente - ID: " + idMovimiento);
                session.setAttribute("mensaje", "Movimiento eliminado exitosamente");
            } else {
                System.err.println("❌ No se pudo eliminar el movimiento - ID: " + idMovimiento);
                session.setAttribute("error", "No se pudo eliminar el movimiento");
            }

            response.sendRedirect(request.getContextPath() + "/movimientos/historial?id=" + idCliente);

        } catch (Exception e) {
            System.err.println("❌ Error al eliminar movimiento: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al eliminar el movimiento");
            response.sendRedirect(request.getContextPath() + "/clientes/listar");
        }
    }
}