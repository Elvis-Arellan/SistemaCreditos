package config;

import dao.impl.UsuarioDaoImpl;
import modelo.Usuario;
import java.util.List;

public class TestDB {

    public static void main(String[] args) {
        System.out.println("TEST DE BASE DE DATOS - USUARIOS");

        testConexion();

        testListarUsuarios();

        System.out.println("FIN DEL TEST");

    }

    private static void testConexion() {
        System.out.println("\n TEST 1: PROBANDO CONEXIÓN...");
        System.out.println("─────────────────────────────────────────────────");
        Conexion.conectar();
    }

    private static void testListarUsuarios() {
        System.out.println("\n TEST 2: LISTANDO TODOS LOS USUARIOS...");
        System.out.println("─────────────────────────────────────────────────");

        UsuarioDaoImpl dao = new UsuarioDaoImpl();
        List<Usuario> lista = dao.listarUsuarios();

        if (lista.isEmpty()) {
            System.out.println(" No se encontraron usuarios en la base de datos.");
        } else {
            System.out.println("\n USUARIOS ENCONTRADOS: " + lista.size());
            System.out.println("─────────────────────────────────────────────────");
            for (Usuario u : lista) {
                System.out.println(u);
            }
        }
    }

}
