package com.server.database.fabricas;
import java.sql.Connection;
import java.sql.SQLException;

public interface FabricaBaseDatos {
 
    Connection obtenerConexion(String url, String usuario, String contrasena) throws SQLException;
}
