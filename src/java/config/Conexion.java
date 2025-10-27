package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/sistema_creditos";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection conectar() {

        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println(" Conexión exitosa a la base de datos");
        } catch (ClassNotFoundException e) {
            System.out.println(" Error: No se encontró el driver JDBC " + e.getMessage());
        } catch (SQLException e) {
            System.out.println(" Error al conectar: " + e.getMessage());
        }
        return con;
    }
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver no encontrado", e);
        }
    }
}
