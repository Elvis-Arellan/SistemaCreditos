package dao.impl;

import config.Conexion;
import dao.interfaces.IUsuarioDao;
import modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDaoImpl implements IUsuarioDao {

    @Override
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id_usuario";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setUsername(rs.getString("username"));
                u.setClave(rs.getString("clave"));
                u.setNombre_completo(rs.getString("nombre_completo"));
                u.setRol(rs.getString("rol"));
                lista.add(u);
            }

            System.out.println("Se encontraron " + lista.size() + " usuarios");

        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public Usuario obtenerPorId(int id) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setUsername(rs.getString("username"));
                usuario.setClave(rs.getString("clave"));
                usuario.setNombre_completo(rs.getString("nombre_completo"));
                usuario.setRol(rs.getString("rol"));

                System.out.println("Usuario encontrado: " + usuario.getUsername());
            } else {
                System.out.println("No se encontró usuario con ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por ID: " + e.getMessage());
        }

        return usuario;
    }

    @Override
    public Usuario obtenerPorUsername(String username) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE username = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setUsername(rs.getString("username"));
                usuario.setClave(rs.getString("clave"));
                usuario.setNombre_completo(rs.getString("nombre_completo"));
                usuario.setRol(rs.getString("rol"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por username: " + e.getMessage());
        }

        return usuario;
    }

    @Override
    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, clave, nombre_completo, rol) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getClave());
            ps.setString(3, usuario.getNombre_completo());
            ps.setString(4, usuario.getRol() != null ? usuario.getRol() : "USUARIO");

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Usuario insertado correctamente: " + usuario.getUsername());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            if (e.getMessage().contains("Duplicate entry")) {
                System.err.println("El username ya existe en la base de datos");
            }
        }

        return false;
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET username = ?, clave = ?, nombre_completo = ?, rol = ? WHERE id_usuario = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getClave());
            ps.setString(3, usuario.getNombre_completo());
            ps.setString(4, usuario.getRol());
            ps.setInt(5, usuario.getId());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Usuario actualizado correctamente");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Usuario eliminado correctamente");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            if (e.getMessage().contains("foreign key constraint")) {
                System.err.println("No se puede eliminar: el usuario tiene clientes asociados");
            }
        }

        return false;
    }

    @Override
    public Usuario validarLogin(String username, String clave) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE username = ? AND clave = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, clave);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setUsername(rs.getString("username"));
                usuario.setClave(rs.getString("clave"));
                usuario.setNombre_completo(rs.getString("nombre_completo"));
                usuario.setRol(rs.getString("rol"));

                System.out.println("Login exitoso para: " + usuario.getUsername() + " (Rol: " + usuario.getRol() + ")");
            } else {
                System.out.println("Credenciales inválidas");
            }

        } catch (SQLException e) {
            System.err.println("Error en validación de login: " + e.getMessage());
        }

        return usuario;
    }
}
