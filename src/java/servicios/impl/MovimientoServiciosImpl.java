package servicios.impl;

import dao.impl.MovimientoDaoImpl;
import dao.impl.ClienteDaoImpl;
import dao.interfaces.IMovimientoDao;
import dao.interfaces.IClienteDao;
import modelo.Movimiento;
import modelo.Cliente;
import servicios.interfaces.IMovimientoServicio;
import java.math.BigDecimal;
import java.util.List;

public class MovimientoServiciosImpl implements IMovimientoServicio {

    private final IMovimientoDao movimientoDao = new MovimientoDaoImpl();
    private final IClienteDao clienteDao = new ClienteDaoImpl();

    @Override
    public List<Movimiento> obtenerMovimientosPorCliente(int idCliente) {
        return movimientoDao.listarPorCliente(idCliente);
    }

    @Override
    public List<Movimiento> obtenerTodosLosMovimientos() {
        return movimientoDao.listarTodos();
    }

    @Override
    public Movimiento obtenerMovimientoPorId(int idMovimiento) {
        return movimientoDao.obtenerPorId(idMovimiento);
    }

    @Override
    public boolean registrarMovimiento(Movimiento movimiento) {
        try {
            // Validaciones
            if (movimiento.getId_cliente() <= 0) {
                System.err.println("ID de cliente inválido");
                return false;
            }

            if (movimiento.getMonto() == null || movimiento.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
                System.err.println("El monto debe ser mayor a cero");
                return false;
            }

            if (movimiento.getTipo_movimiento() == null || movimiento.getTipo_movimiento().trim().isEmpty()) {
                System.err.println("El tipo de movimiento es obligatorio");
                return false;
            }

            // Validar que el tipo sea FIADO o PAGO
            String tipo = movimiento.getTipo_movimiento().toUpperCase();
            if (!tipo.equals("FIADO") && !tipo.equals("PAGO")) {
                System.err.println("El tipo de movimiento debe ser FIADO o PAGO");
                return false;
            }

            movimiento.setTipo_movimiento(tipo);

            // Obtener cliente actual
            Cliente cliente = clienteDao.obtenerPorId(movimiento.getId_cliente());
            if (cliente == null) {
                System.err.println("Cliente no encontrado");
                return false;
            }

            System.out.println("💰 Saldo actual del cliente: S/. " + cliente.getSaldo());
            System.out.println("📝 Registrando movimiento tipo: " + tipo + " - Monto: S/. " + movimiento.getMonto());

            // Calcular nuevo saldo
            BigDecimal saldoActual = cliente.getSaldo() != null ? cliente.getSaldo() : BigDecimal.ZERO;
            BigDecimal nuevoSaldo;

            if (tipo.equals("FIADO")) {
                // FIADO aumenta la deuda (suma al saldo)
                nuevoSaldo = saldoActual.add(movimiento.getMonto());
                System.out.println("➕ FIADO: Aumentando deuda");
            } else {
                // PAGO reduce la deuda (resta al saldo)
                nuevoSaldo = saldoActual.subtract(movimiento.getMonto());
                System.out.println("➖ PAGO: Reduciendo deuda");
                
                // Validar que el pago no genere saldo negativo (el cliente estaría pagando de más)
                if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
                    System.err.println("⚠️ Advertencia: El pago genera un saldo negativo. El cliente estaría pagando S/. " 
                            + nuevoSaldo.abs() + " de más");
                    // Puedes decidir si permitir esto o no
                    // Para este caso, lo permitiremos pero con advertencia
                }
            }

            System.out.println("💵 Nuevo saldo calculado: S/. " + nuevoSaldo);

            // Insertar el movimiento
            boolean movimientoInsertado = movimientoDao.insertar(movimiento);

            if (!movimientoInsertado) {
                System.err.println("❌ Error al insertar el movimiento");
                return false;
            }

            // Actualizar saldo del cliente
            cliente.setSaldo(nuevoSaldo);
            boolean saldoActualizado = clienteDao.actualizar(cliente);

            if (!saldoActualizado) {
                System.err.println("❌ Error al actualizar el saldo del cliente");
                // Aquí idealmente deberías hacer rollback del movimiento
                // Para simplificar, solo retornamos false
                return false;
            }

            System.out.println("✅ Movimiento registrado y saldo actualizado correctamente");
            System.out.println("📊 Saldo final: S/. " + nuevoSaldo);

            return true;

        } catch (Exception e) {
            System.err.println("❌ Error en el servicio al registrar movimiento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizarMovimiento(Movimiento movimiento) {
        try {
            if (movimiento.getId_movimiento() <= 0) {
                System.err.println("ID de movimiento inválido");
                return false;
            }

            if (movimiento.getMonto() == null || movimiento.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
                System.err.println("El monto debe ser mayor a cero");
                return false;
            }

            // Obtener movimiento anterior para recalcular saldo
            Movimiento movimientoAnterior = movimientoDao.obtenerPorId(movimiento.getId_movimiento());
            if (movimientoAnterior == null) {
                System.err.println("Movimiento no encontrado");
                return false;
            }

            // Obtener cliente
            Cliente cliente = clienteDao.obtenerPorId(movimiento.getId_cliente());
            if (cliente == null) {
                System.err.println("Cliente no encontrado");
                return false;
            }

            BigDecimal saldoActual = cliente.getSaldo() != null ? cliente.getSaldo() : BigDecimal.ZERO;
            
            // Revertir el movimiento anterior
            if (movimientoAnterior.getTipo_movimiento().equals("FIADO")) {
                saldoActual = saldoActual.subtract(movimientoAnterior.getMonto());
            } else {
                saldoActual = saldoActual.add(movimientoAnterior.getMonto());
            }

            // Aplicar el nuevo movimiento
            if (movimiento.getTipo_movimiento().equals("FIADO")) {
                saldoActual = saldoActual.add(movimiento.getMonto());
            } else {
                saldoActual = saldoActual.subtract(movimiento.getMonto());
            }

            // Actualizar movimiento
            boolean movimientoActualizado = movimientoDao.actualizar(movimiento);
            if (!movimientoActualizado) {
                return false;
            }

            // Actualizar saldo del cliente
            cliente.setSaldo(saldoActual);
            boolean saldoActualizado = clienteDao.actualizar(cliente);

            if (saldoActualizado) {
                System.out.println("✅ Movimiento y saldo actualizados correctamente");
                return true;
            }

            return false;

        } catch (Exception e) {
            System.err.println("❌ Error en el servicio al actualizar movimiento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarMovimiento(int idMovimiento) {
        try {
            if (idMovimiento <= 0) {
                System.err.println("ID de movimiento inválido");
                return false;
            }

            // Obtener movimiento para revertir el saldo
            Movimiento movimiento = movimientoDao.obtenerPorId(idMovimiento);
            if (movimiento == null) {
                System.err.println("Movimiento no encontrado");
                return false;
            }

            // Obtener cliente
            Cliente cliente = clienteDao.obtenerPorId(movimiento.getId_cliente());
            if (cliente == null) {
                System.err.println("Cliente no encontrado");
                return false;
            }

            BigDecimal saldoActual = cliente.getSaldo() != null ? cliente.getSaldo() : BigDecimal.ZERO;

            // Revertir el movimiento
            if (movimiento.getTipo_movimiento().equals("FIADO")) {
                saldoActual = saldoActual.subtract(movimiento.getMonto());
                System.out.println("🔄 Revirtiendo FIADO: restando S/. " + movimiento.getMonto());
            } else {
                saldoActual = saldoActual.add(movimiento.getMonto());
                System.out.println("🔄 Revirtiendo PAGO: sumando S/. " + movimiento.getMonto());
            }

            // Eliminar movimiento
            boolean movimientoEliminado = movimientoDao.eliminar(idMovimiento);
            if (!movimientoEliminado) {
                return false;
            }

            // Actualizar saldo del cliente
            cliente.setSaldo(saldoActual);
            boolean saldoActualizado = clienteDao.actualizar(cliente);

            if (saldoActualizado) {
                System.out.println("✅ Movimiento eliminado y saldo revertido correctamente");
                System.out.println("💵 Nuevo saldo: S/. " + saldoActual);
                return true;
            }

            return false;

        } catch (Exception e) {
            System.err.println("❌ Error al eliminar movimiento: " + e.getMessage());
            return false;
        }
    }
}