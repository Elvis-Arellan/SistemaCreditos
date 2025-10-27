package servicios.impl;

import dao.impl.UsuarioDaoImpl;
import dao.interfaces.IUsuarioDao;
import modelo.Usuario;
import servicios.interfaces.IUsuarioServicio;
import java.security.MessageDigest;
import java.util.List;

public class UsuarioServiciosImpl implements IUsuarioServicio {

    private final IUsuarioDao usuarioDao = new UsuarioDaoImpl();

    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioDao.listarUsuarios();
    }

    @Override
    public Usuario obtenerUsuarioPorId(int id) {
        return usuarioDao.obtenerPorId(id);
    }

    @Override
    public Usuario obtenerUsuarioPorUsername(String username) {
        return usuarioDao.obtenerPorUsername(username);
    }

    @Override
    public boolean registrarUsuario(Usuario usuario) {
        try {
            if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                System.err.println("El username es obligatorio");
                return false;
            }

            if (usuario.getClave() == null || usuario.getClave().trim().isEmpty()) {
                System.err.println("La contraseña es obligatoria");
                return false;
            }

            if (usuario.getClave().length() < 4) {
                System.err.println("La contraseña debe tener al menos 4 caracteres");
                return false;
            }

            Usuario existente = usuarioDao.obtenerPorUsername(usuario.getUsername());
            if (existente != null) {
                System.err.println("El username ya está en uso");
                return false;
            }

            if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
                usuario.setRol("USUARIO");
            }

            String claveEncriptada = encriptarClave(usuario.getClave());
            usuario.setClave(claveEncriptada);

            return usuarioDao.insertar(usuario);

        } catch (Exception e) {
            System.err.println("Error en el servicio al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        try {
            if (usuario.getId() <= 0) {
                System.err.println("ID de usuario inválido");
                return false;
            }

            if (usuario.getClave() != null && !usuario.getClave().isEmpty()) {
                String claveEncriptada = encriptarClave(usuario.getClave());
                usuario.setClave(claveEncriptada);
            }

            return usuarioDao.actualizar(usuario);

        } catch (Exception e) {
            System.err.println("Error en el servicio al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarUsuario(int id) {
        if (id <= 0) {
            System.err.println("ID de usuario inválido");
            return false;
        }

        return usuarioDao.eliminar(id);
    }

    @Override
    public Usuario login(String username, String clave) {
        try {
            if (username == null || username.trim().isEmpty()) {
                System.err.println("El username es obligatorio");
                return null;
            }

            if (clave == null || clave.trim().isEmpty()) {
                System.err.println("La contraseña es obligatoria");
                return null;
            }

            String claveEncriptada = encriptarClave(clave);

            return usuarioDao.validarLogin(username, claveEncriptada);

        } catch (Exception e) {
            System.err.println("Error en el servicio de login: " + e.getMessage());
            return null;
        }
    }

    private String encriptarClave(String clave) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(clave.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (Exception e) {
            System.err.println("Error al encriptar contraseña: " + e.getMessage());
            return clave;
        }
    }
}
