package dao.interfaces;

import modelo.Usuario;
import java.util.List;

public interface IUsuarioDao {

    List<Usuario> listarUsuarios();

    Usuario obtenerPorId(int id);

    Usuario obtenerPorUsername(String username);

    boolean insertar(Usuario usuario);

    boolean actualizar(Usuario usuario);

    boolean eliminar(int id);

    Usuario validarLogin(String username, String clave);

}
