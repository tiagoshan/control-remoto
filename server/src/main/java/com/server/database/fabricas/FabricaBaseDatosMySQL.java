package com.server.database.fabricas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class FabricaBaseDatosMySQL implements FabricaBaseDatos {

    @Override
    public Connection obtenerConexion(String url, String usuario, String contrasena) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error cargando el driver de MySQL: " + e.getMessage());
        }
        return DriverManager.getConnection(url, usuario, contrasena);
    }
}
