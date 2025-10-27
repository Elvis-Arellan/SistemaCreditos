package servicios.interfaces;

import modelo.Usuario;
import java.util.List;

public interface IUsuarioServicio {

    List<Usuario> obtenerTodosLosUsuarios();

    Usuario obtenerUsuarioPorId(int id);

    Usuario obtenerUsuarioPorUsername(String username);

    boolean registrarUsuario(Usuario usuario);

    boolean actualizarUsuario(Usuario usuario);

    boolean eliminarUsuario(int id);

    Usuario login(String username, String clave);
}
