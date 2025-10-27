package servicios.interfaces;

import java.util.List;
import modelo.Cliente;

public interface IClienteServicio {

    List<Cliente> obtenerClientes();

    List<Cliente> obtenerClientesPorUsuario(int idUsuario);

    Cliente obtenerClientePorId(int idCliente);

    boolean registrarCliente(Cliente cliente);

    boolean actualizarCliente(Cliente cliente);

    boolean eliminarCliente(int idCliente);
}
