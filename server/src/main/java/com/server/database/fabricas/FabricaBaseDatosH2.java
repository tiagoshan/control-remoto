package com.server.database.fabricas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class FabricaBaseDatosH2 implements FabricaBaseDatos {

    @Override
    public Connection obtenerConexion(String url, String usuario, String contrasena) throws SQLException {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error cargando el driver de H2: " + e.getMessage());
        }
        return DriverManager.getConnection(url, usuario, contrasena);
    }
}
