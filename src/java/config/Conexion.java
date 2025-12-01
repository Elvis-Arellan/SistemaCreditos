package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String RDS_ENDPOINT = "database-aws.cp8wyg44obe8.us-east-2.rds.amazonaws.com";
    private static final String RDS_PORT = "3306";
    private static final String RDS_DATABASE = "sistema_creditos";
    private static final String RDS_USER = "admin";
    private static final String RDS_PASSWORD = "rootrootv1";
    private static final String URL = "jdbc:mysql://" + RDS_ENDPOINT + ":" + RDS_PORT + "/" + RDS_DATABASE
            + "?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    public static Connection conectar() {

        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(URL, RDS_USER, RDS_PASSWORD);

            System.out.println(" Conexión exitosa a AWS RDS");
            System.out.println(" Endpoint: " + RDS_ENDPOINT);
            System.out.println("  Base de datos: " + RDS_DATABASE);

        } catch (ClassNotFoundException e) {
            System.err.println(" Error: No se encontró el driver JDBC");
            System.err.println("Detalle: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println(" Error al conectar a AWS RDS");
            System.err.println("Detalle: " + e.getMessage());
            e.printStackTrace();
        }

        return con;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, RDS_USER, RDS_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver no encontrado", e);
        }
    }
}
