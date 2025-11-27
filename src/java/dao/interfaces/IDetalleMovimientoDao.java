package dao.interfaces;

import modelo.DetalleMovimiento;
import java.util.List;

public interface IDetalleMovimientoDao {
    List<DetalleMovimiento> listarPorMovimiento(int idMovimiento);
    DetalleMovimiento obtenerPorId(int idDetalle);
    boolean insertar(DetalleMovimiento detalle);
    boolean actualizar(DetalleMovimiento detalle);
    boolean eliminar(int idDetalle);
    boolean eliminarPorMovimiento(int idMovimiento);
}