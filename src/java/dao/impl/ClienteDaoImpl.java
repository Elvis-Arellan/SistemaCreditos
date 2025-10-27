package dao.impl;

import config.Conexion;
import modelo.Cliente;
import dao.interfaces.IClienteDao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDaoImpl implements IClienteDao {

    @Override
    public List<Cliente> listarClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE estado = 'ACTIVO' ORDER BY fecha_registro DESC";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId_cliente(rs.getInt("id_cliente"));
                c.setId_usuario(rs.getInt("id_usuario"));
                c.setNombre(rs.getString("nombre"));
                c.setApellido(rs.getString("apellido"));
                c.setEmail(rs.getString("email"));
                c.setTelefono(rs.getString("telefono"));
                c.setFecha_registro(rs.getDate("fecha_registro"));
                c.setEstado(rs.getString("estado"));
                c.setSaldo(rs.getBigDecimal("saldo"));
                lista.add(c);
            }

            System.out.println("ClienteDAO: Se encontraron " + lista.size() + " clientes");

        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<Cliente> listarClientesPorUsuario(int idUsuario) {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE id_usuario = ? AND estado = 'ACTIVO' ORDER BY fecha_registro DESC";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId_cliente(rs.getInt("id_cliente"));
                c.setId_usuario(rs.getInt("id_usuario"));
                c.setNombre(rs.getString("nombre"));
                c.setApellido(rs.getString("apellido"));
                c.setEmail(rs.getString("email"));
                c.setTelefono(rs.getString("telefono"));
                c.setFecha_registro(rs.getDate("fecha_registro"));
                c.setEstado(rs.getString("estado"));
                c.setSaldo(rs.getBigDecimal("saldo"));
                lista.add(c);
            }

            System.out.println("ClienteDAO: Usuario " + idUsuario + " tiene " + lista.size() + " clientes");

        } catch (SQLException e) {
            System.err.println("Error al listar clientes por usuario: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public Cliente obtenerPorId(int idCliente) {
        Cliente cliente = null;
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cliente = new Cliente();
                cliente.setId_cliente(rs.getInt("id_cliente"));
                cliente.setId_usuario(rs.getInt("id_usuario"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setEmail(rs.getString("email"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setFecha_registro(rs.getDate("fecha_registro"));
                cliente.setEstado(rs.getString("estado"));
                cliente.setSaldo(rs.getBigDecimal("saldo"));

                System.out.println("Cliente encontrado: " + cliente.getNombre());
            } else {
                System.out.println("No se encontró cliente con ID: " + idCliente);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener cliente: " + e.getMessage());
        }

        return cliente;
    }

    @Override
    public boolean insertar(Cliente cliente) {
        String sql = "INSERT INTO clientes (id_usuario, nombre, apellido, email, telefono, saldo, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cliente.getId_usuario());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getApellido());
            ps.setString(4, cliente.getEmail());
            ps.setString(5, cliente.getTelefono());
            ps.setBigDecimal(6, cliente.getSaldo());
            ps.setString(7, cliente.getEstado());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Cliente insertado correctamente: " + cliente.getNombre());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre = ?, apellido = ?, email = ?, telefono = ?, "
                + "saldo = ?, estado = ? WHERE id_cliente = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getTelefono());
            ps.setBigDecimal(5, cliente.getSaldo());
            ps.setString(6, cliente.getEstado());
            ps.setInt(7, cliente.getId_cliente());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Cliente actualizado correctamente");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean eliminar(int idCliente) {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Cliente eliminado correctamente");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            if (e.getMessage().contains("foreign key constraint")) {
                System.err.println("No se puede eliminar: el cliente tiene movimientos asociados");
            }
        }

        return false;
    }
}
