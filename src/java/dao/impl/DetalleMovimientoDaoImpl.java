package dao.impl;

import config.Conexion;
import modelo.DetalleMovimiento;
import dao.interfaces.IDetalleMovimientoDao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleMovimientoDaoImpl implements IDetalleMovimientoDao {

    @Override
    public List<DetalleMovimiento> listarPorMovimiento(int idMovimiento) {
        List<DetalleMovimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_movimientos WHERE id_movimiento = ?";

        try (Connection conn = Conexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMovimiento);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DetalleMovimiento detalle = new DetalleMovimiento();
                detalle.setId_detalle(rs.getInt("id_detalle"));
                detalle.setId_movimiento(rs.getInt("id_movimiento"));
                detalle.setDescripcion_producto(rs.getString("descripcion_producto"));
                detalle.setCantidad(rs.getBigDecimal("cantidad"));
                lista.add(detalle);
            }

            System.out.println("DetalleMovimientoDAO: Se encontraron " + lista.size() + " detalles para movimiento " + idMovimiento);

        } catch (SQLException e) {
            System.err.println("Error al listar detalles por movimiento: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public DetalleMovimiento obtenerPorId(int idDetalle) {
        DetalleMovimiento detalle = null;
        String sql = "SELECT * FROM detalle_movimientos WHERE id_detalle = ?";

        try (Connection conn = Conexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDetalle);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                detalle = new DetalleMovimiento();
                detalle.setId_detalle(rs.getInt("id_detalle"));
                detalle.setId_movimiento(rs.getInt("id_movimiento"));
                detalle.setDescripcion_producto(rs.getString("descripcion_producto"));
                detalle.setCantidad(rs.getBigDecimal("cantidad"));

                System.out.println("Detalle encontrado: ID " + idDetalle);
            } else {
                System.out.println("No se encontró detalle con ID: " + idDetalle);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener detalle: " + e.getMessage());
        }

        return detalle;
    }

    @Override
    public boolean insertar(DetalleMovimiento detalle) {
        String sql = "INSERT INTO detalle_movimientos (id_movimiento, descripcion_producto, cantidad) " +
                     "VALUES (?, ?, ?)";

        try (Connection conn = Conexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, detalle.getId_movimiento());
            ps.setString(2, detalle.getDescripcion_producto());
            ps.setBigDecimal(3, detalle.getCantidad());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Detalle de movimiento insertado correctamente");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar detalle: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean actualizar(DetalleMovimiento detalle) {
        String sql = "UPDATE detalle_movimientos SET id_movimiento = ?, descripcion_producto = ?, cantidad = ? " +
                     "WHERE id_detalle = ?";

        try (Connection conn = Conexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, detalle.getId_movimiento());
            ps.setString(2, detalle.getDescripcion_producto());
            ps.setBigDecimal(3, detalle.getCantidad());
            ps.setInt(4, detalle.getId_detalle());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Detalle actualizado correctamente");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar detalle: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean eliminar(int idDetalle) {
        String sql = "DELETE FROM detalle_movimientos WHERE id_detalle = ?";

        try (Connection conn = Conexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDetalle);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Detalle eliminado correctamente");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar detalle: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean eliminarPorMovimiento(int idMovimiento) {
        String sql = "DELETE FROM detalle_movimientos WHERE id_movimiento = ?";

        try (Connection conn = Conexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMovimiento);
            int filasAfectadas = ps.executeUpdate();

            System.out.println("Detalles eliminados para movimiento " + idMovimiento + ": " + filasAfectadas);
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar detalles por movimiento: " + e.getMessage());
        }

        return false;
    }
}