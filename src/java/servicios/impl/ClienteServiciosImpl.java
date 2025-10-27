package servicios.impl;

import java.math.BigDecimal;
import java.util.List;
import modelo.Cliente;
import dao.impl.ClienteDaoImpl;
import dao.interfaces.IClienteDao;
import servicios.interfaces.IClienteServicio;

public class ClienteServiciosImpl implements IClienteServicio {

    private final IClienteDao clienteDAO = new ClienteDaoImpl();

    @Override
    public List<Cliente> obtenerClientes() {
        return clienteDAO.listarClientes();
    }

    @Override
    public List<Cliente> obtenerClientesPorUsuario(int idUsuario) {
        return clienteDAO.listarClientesPorUsuario(idUsuario);
    }

    @Override
    public Cliente obtenerClientePorId(int idCliente) {
        return clienteDAO.obtenerPorId(idCliente);
    }

    @Override
    public boolean registrarCliente(Cliente cliente) {
        try {
            if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
                System.err.println("El nombre es obligatorio");
                return false;
            }

            if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty()) {
                System.err.println("El apellido es obligatorio");
                return false;
            }

            if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) {
                System.err.println("El teléfono es obligatorio");
                return false;
            }

            if (cliente.getId_usuario() <= 0) {
                System.err.println("El ID de usuario es inválido");
                return false;
            }

            if (cliente.getSaldo() != null && cliente.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
                System.err.println("El saldo no puede ser negativo");
                return false;
            }

            if (cliente.getSaldo() == null) {
                cliente.setSaldo(BigDecimal.ZERO);
            }

            if (cliente.getEstado() == null || cliente.getEstado().trim().isEmpty()) {
                cliente.setEstado("ACTIVO");
            }

            return clienteDAO.insertar(cliente);

        } catch (Exception e) {
            System.err.println("Error en el servicio al registrar cliente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizarCliente(Cliente cliente) {
        try {
            if (cliente.getId_cliente() <= 0) {
                System.err.println("ID de cliente inválido");
                return false;
            }

            if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
                System.err.println("El nombre es obligatorio");
                return false;
            }

            if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty()) {
                System.err.println("El apellido es obligatorio");
                return false;
            }

            return clienteDAO.actualizar(cliente);

        } catch (Exception e) {
            System.err.println("Error en el servicio al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarCliente(int idCliente) {
        if (idCliente <= 0) {
            System.err.println("ID de cliente inválido");
            return false;
        }

        return clienteDAO.eliminar(idCliente);
    }
}
