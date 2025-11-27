package dao.impl;

import config.Conexion;
import modelo.Movimiento;
import dao.interfaces.IMovimientoDao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDaoImpl implements IMovimientoDao {

    @Override
    public List<Movimiento> listarPorCliente(int idCliente) {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT m.*, c.nombre, c.apellido " +
                     "FROM movimientos m " +
                     "INNER JOIN clientes c ON m.id_cliente = c.id_cliente " +
                     "WHERE m.id_cliente = ? " +
                     "ORDER BY m.fecha_movimiento DESC";

        try (Connection conn = Conexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Movimiento m = new Movimiento();
                m.setId_movimiento(rs.getInt("id_movimiento"));
                m.setId_cliente(rs.getInt("id_cliente"));
                m.setTipo_movimiento(rs.getString("tipo_movimiento"));
                m.setMonto(rs.getBigDecimal("monto"));
                m.setDescripcion(rs.getString("descripcion"));
                m.setFecha_movimiento(rs.getTimestamp("fecha_movimiento"));
                m.setNombreCliente(rs.getString("nombre"));
                m.setApellidoCliente(rs.getString("apellido"));
                lista.add(m);
            }

            System.out.println("MovimientoDAO: Se encontraron " + lista.size() + " movimientos para cliente " + idCliente);

        } catch (SQLException e) {
            System.err.println("Error al listar movimientos por cliente: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<Movimiento> listarTodos() {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT m.*, c.nombre, c.apellido " +
                     "FROM movimientos m " +
                     "INNER JOIN clientes c ON m.id_cliente = c.id_cliente " +
                     "ORDER BY m.fecha_movimiento DESC";

        try (Connection conn = Conexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Movimiento m = new Movimiento();
                m.setId_movimiento(rs.getInt("id_movimiento"));
                m.setId_cliente(rs.getInt("id_cliente"));
                m.setTipo_movimiento(rs.getString("tipo_movimiento"));
                m.setMonto(rs.getBigDecimal("monto"));
                m.setDescripcion(rs.getString("descripcion"));
                m.setFecha_movimiento(rs.getTimestamp("fecha_movimiento"));
                m.setNombreCliente(rs.getString("nombre"));
                m.setApellidoCliente(rs.getString("apellido"));
                lista.add(m);
            }

            System.out.println("MovimientoDAO: Se encontraron " + lista.size() + " movimientos");

        } catch (SQLException e) {
            System.err.println("Error al listar todos los movimientos: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public Movimiento obtenerPorId(int idMovimiento) {
        Movimiento movimiento = null;
        String sql = "SELECT m.*, c.nombre, c.apellido " +
                     "FROM movimientos m " +
                     "INNER JOIN clientes c ON m.id_cliente = c.id_cliente " +
                     "WHERE m.id_movimiento = ?";

        try (Connection conn = Conexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMovimiento);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                movimiento = new Movimiento();
                movimiento.setId_movimiento(rs.getInt("id_movimiento"));
                movimiento.setId_cliente(rs.getInt("id_cliente"));
                movimiento.setTipo_movimiento(rs.getString("tipo_movimiento"));
                movimiento.setMonto(rs.getBigDecimal("monto"));
                movimiento.setDescripcion(rs.getString("descripcion"));
                movimiento.setFecha_movimiento(rs.getTimestamp("fecha_movimiento"));
                movimiento.setNombreCliente(rs.getString("nombre"));
                movimiento.setApellidoCliente(rs.getString("apellido"));

                System.out.println("Movimiento encontrado: ID " + idMovimiento);
            } else {
                System.out.println("No se encontró movimiento con ID: " + idMovimiento);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener movimiento: " + e.getMessage());
        }

        return movimiento;
    }

    @Override
    public boolean insertar(Movimiento movimiento) {
        String sql = "INSERT INTO movimientos (id_cliente, tipo_movimiento, monto, descripcion) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movimiento.getId_cliente());
            ps.setString(2, movimiento.getTipo_movimiento());
            ps.setBigDecimal(3, movimiento.getMonto());
            ps.setString(4, movimiento.getDescripcion());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Movimiento insertado correctamente: " + movimiento.getTipo_movimiento());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar movimiento: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean actualizar(Movimiento movimiento) {
        String sql = "UPDATE movimientos SET id_cliente = ?, tipo_movimiento = ?, monto = ?, descripcion = ? " +
                     "WHERE id_movimiento = ?";

        try (Connection conn = Conexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movimiento.getId_cliente());
            ps.setString(2, movimiento.getTipo_movimiento());
            ps.setBigDecimal(3, movimiento.getMonto());
            ps.setString(4, movimiento.getDescripcion());
            ps.setInt(5, movimiento.getId_movimiento());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Movimiento actualizado correctamente");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar movimiento: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean eliminar(int idMovimiento) {
        String sql = "DELETE FROM movimientos WHERE id_movimiento = ?";

        try (Connection conn = Conexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMovimiento);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Movimiento eliminado correctamente");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar movimiento: " + e.getMessage());
            if (e.getMessage().contains("foreign key constraint")) {
                System.err.println("No se puede eliminar: el movimiento tiene detalles asociados");
            }
        }

        return false;
    }
}