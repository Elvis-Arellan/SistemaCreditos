package servicios.interfaces;

import modelo.Movimiento;
import java.util.List;

public interface IMovimientoServicio {
    List<Movimiento> obtenerMovimientosPorCliente(int idCliente);
    List<Movimiento> obtenerTodosLosMovimientos();
    Movimiento obtenerMovimientoPorId(int idMovimiento);
    boolean registrarMovimiento(Movimiento movimiento);
    boolean actualizarMovimiento(Movimiento movimiento);
    boolean eliminarMovimiento(int idMovimiento);
}