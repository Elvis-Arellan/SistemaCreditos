package dao.interfaces;

import java.util.List;
import modelo.Cliente;

public interface IClienteDao {

    List<Cliente> listarClientes();

    List<Cliente> listarClientesPorUsuario(int idUsuario);

    Cliente obtenerPorId(int idCliente);

    boolean insertar(Cliente cliente);

    boolean actualizar(Cliente cliente);

    boolean eliminar(int idCliente);
}
