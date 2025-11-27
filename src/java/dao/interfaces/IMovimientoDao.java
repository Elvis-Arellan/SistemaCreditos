package dao.interfaces;

import modelo.Movimiento;
import java.util.List;

public interface IMovimientoDao {
    List<Movimiento> listarPorCliente(int idCliente);
    List<Movimiento> listarTodos();
    Movimiento obtenerPorId(int idMovimiento);
    boolean insertar(Movimiento movimiento);
    boolean actualizar(Movimiento movimiento);
    boolean eliminar(int idMovimiento);
}