package com.server.database;

import com.server.database.fabricas.*;
import com.server.database.productores.*;

import java.sql.Statement;
import java.sql.DriverManager;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConexionBaseDatos {

    private static Connection conexion;
    private static String TIPO_DB;
    private static String URL_JDBC;
    private static String USUARIO;
    private static String CONTRASEÑA;

    static {
        try {
            Properties propiedades = new Properties();
            try (InputStream entrada = ConexionBaseDatos.class.getClassLoader().getResourceAsStream("database.properties")) {
                if (entrada == null) {
                    throw new IOException("Archivo database.properties no encontrado");
                }
                propiedades.load(entrada);
            }

            TIPO_DB = propiedades.getProperty("db.type");
            URL_JDBC = propiedades.getProperty("jdbc.url");
            USUARIO = propiedades.getProperty("jdbc.user");
            CONTRASEÑA = propiedades.getProperty("jdbc.password");

        } catch (IOException e) {
            System.out.println("Error al cargar archivo .properties: " + e.getMessage());
        }
    }

    // Singleton que retorna la conexión usando el Factory Method
    public static Connection obtenerConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                if (TIPO_DB.equals("mysql")) {
                    // Conectar a MySQL sin especificar base de datos para poder crear la base de datos si no existe
                    String urlSinBaseDatos = URL_JDBC.substring(0, URL_JDBC.lastIndexOf("/") + 1);  // Quita el nombre de la base de datos
                    try (Connection tempConexion = DriverManager.getConnection(urlSinBaseDatos, USUARIO, CONTRASEÑA);
                         Statement declaracion = tempConexion.createStatement()) {

                        // Crear la base de datos "db" si no existe
                        declaracion.execute("CREATE DATABASE IF NOT EXISTS db");
                        System.out.println("Base de datos creada o ya existe.");
                    }

                    // Cargar el driver de MySQL
                    Class.forName("com.mysql.cj.jdbc.Driver");

                } else if (TIPO_DB.equals("h2")) {
                    // Cargar driver de H2
                    Class.forName("org.h2.Driver");
                }

                // Establecer la conexión a la base de datos específica
                FabricaBaseDatos fabrica = ProductorFabricaBaseDatos.obtenerFabrica(TIPO_DB);
                conexion = fabrica.obtenerConexion(URL_JDBC, USUARIO, CONTRASEÑA);

                // Ejecutar el script SQL para crear tablas
                //ejecutarScriptSQL(conexion, "database.sql");

            } catch (SQLException error) {
                System.out.println("Error al conectar con la base de datos: " + error.getMessage());
                throw error;  // Repropagar excepción para manejarla en el DAO o capas superiores
            } catch (ClassNotFoundException e) {
                System.out.println("Error al cargar el driver de la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return conexion;
    }

    private static void ejecutarScriptSQL(Connection conexion, String nombreArchivo) {
        try (InputStream entrada = ConexionBaseDatos.class.getClassLoader().getResourceAsStream(nombreArchivo)) {
            if (entrada == null) {
                throw new IOException("Archivo " + nombreArchivo + " no encontrado.");
            }

            try (BufferedReader lector = new BufferedReader(new InputStreamReader(entrada))) {
                StringBuilder sqlBuilder = new StringBuilder();
                String linea;
                while ((linea = lector.readLine()) != null) {
                    sqlBuilder.append(linea).append("\n");
                }

                String[] declaracionesSQL = sqlBuilder.toString().split(";");
                for (String declaracion : declaracionesSQL) {
                    if (!declaracion.trim().isEmpty()) {
                        try (Statement stmt = conexion.createStatement()) {
                            stmt.execute(declaracion);
                        }
                    }
                }
                System.out.println("Script SQL ejecutado correctamente.");
            }
        } catch (IOException | SQLException error) {
            System.out.println("Error al ejecutar el script SQL: " + error.getMessage());
        }
    }

    // Método para cerrar la conexión
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException error) {
            System.out.println("Error al cerrar la conexión: " + error.getMessage());
        }
    }
}
