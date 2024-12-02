package com.server.dao;

import com.server.database.ConexionBaseDatos;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Clase DAO para manejar las conexiones de los clientes en la base de datos.
 */
public class ConexionClienteDAO {

    /**
     * Guarda una nueva conexión de cliente en la base de datos.
     *
     * @param socketCliente El socket del cliente.
     * @param idAdmin El ID del administrador.
     */
    public void guardarConexionCliente(Socket socketCliente, int idAdmin) {
        try (Connection conexion = ConexionBaseDatos.obtenerConexion()) {
            String consultaVerificarCliente = "SELECT id FROM Clientes WHERE ip = ?";
            PreparedStatement consultaVerificarClientePstmt = conexion.prepareStatement(consultaVerificarCliente);
            consultaVerificarClientePstmt.setString(1, socketCliente.getInetAddress().getHostAddress());
            int idCliente = -1;
            try (ResultSet rs = consultaVerificarClientePstmt.executeQuery()) {
                if (rs.next()) {
                    idCliente = rs.getInt("id");
                }
            }

            if (idCliente == -1) {
                String consultaInsertarCliente = "INSERT INTO Clientes (ip) VALUES (?)";
                PreparedStatement consultaInsertarClientePstmt = conexion.prepareStatement(consultaInsertarCliente, PreparedStatement.RETURN_GENERATED_KEYS);
                consultaInsertarClientePstmt.setString(1, socketCliente.getInetAddress().getHostAddress());
                consultaInsertarClientePstmt.executeUpdate();

                try (ResultSet rs = consultaInsertarClientePstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idCliente = rs.getInt(1);
                    }
                }
            }

            if (idCliente != -1) {
                String consultaInsertarConexion = "INSERT INTO Conexiones (cliente_id, administrador_id, fecha_conexion) VALUES (?, ?, NOW())";
                PreparedStatement consultaInsertarConexionPstmt = conexion.prepareStatement(consultaInsertarConexion);
                consultaInsertarConexionPstmt.setInt(1, idCliente);
                consultaInsertarConexionPstmt.setInt(2, idAdmin);
                consultaInsertarConexionPstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza la base de datos cuando un cliente se desconecta.
     *
     * @param socketCliente El socket del cliente.
     */
    public void actualizarDesconexionCliente(Socket socketCliente) {
        try (Connection conexion = ConexionBaseDatos.obtenerConexion()) {
            String consultaIdCliente = "SELECT id FROM Clientes WHERE ip = ?";
            PreparedStatement consultaIdClientePstmt = conexion.prepareStatement(consultaIdCliente);
            consultaIdClientePstmt.setString(1, socketCliente.getInetAddress().getHostAddress());
            int idCliente = -1;
            try (ResultSet rs = consultaIdClientePstmt.executeQuery()) {
                if (rs.next()) {
                    idCliente = rs.getInt("id");
                }
            }

            if (idCliente != -1) {
                String consultaActualizar = "UPDATE Conexiones SET fecha_desconexion = NOW(), duracion = TIMESTAMPDIFF(SECOND, fecha_conexion, NOW()) WHERE cliente_id = ? AND fecha_desconexion IS NULL";
                PreparedStatement consultaActualizarPstmt = conexion.prepareStatement(consultaActualizar);
                consultaActualizarPstmt.setInt(1, idCliente);
                consultaActualizarPstmt.executeUpdate();

                // Obtener el ID de la última conexión
                String consultaIdUltimaConexion = "SELECT id FROM Conexiones WHERE cliente_id = ? ORDER BY fecha_conexion DESC LIMIT 1";
                PreparedStatement consultaIdUltimaConexionPstmt = conexion.prepareStatement(consultaIdUltimaConexion);
                consultaIdUltimaConexionPstmt.setInt(1, idCliente);
                try (ResultSet rs = consultaIdUltimaConexionPstmt.executeQuery()) {
                    if (rs.next()) {
                        int idConexion = rs.getInt("id");
                        insertarReporte(idConexion); // Insertar el reporte con el ID de la última conexión
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza el contador de clics del cliente en la base de datos.
     *
     * @param socketCliente El socket del cliente.
     * @param cantidadClicks El número de clics del cliente.
     */
    public void actualizarContadorClicksCliente(Socket socketCliente, int cantidadClicks) {
        try (Connection conexion = ConexionBaseDatos.obtenerConexion()) {
            String consultaIdCliente = "SELECT id FROM Clientes WHERE ip = ?";
            PreparedStatement consultaIdClientePstmt = conexion.prepareStatement(consultaIdCliente);
            consultaIdClientePstmt.setString(1, socketCliente.getInetAddress().getHostAddress());
            int idCliente = -1;
            try (ResultSet rs = consultaIdClientePstmt.executeQuery()) {
                if (rs.next()) {
                    idCliente = rs.getInt("id");
                }
            }

            if (idCliente != -1) {
                String consultaActualizar = "UPDATE Conexiones SET cantidad_clicks = ? WHERE cliente_id = ? AND fecha_desconexion IS NULL";
                PreparedStatement consultaActualizarPstmt = conexion.prepareStatement(consultaActualizar);
                consultaActualizarPstmt.setInt(1, cantidadClicks);
                consultaActualizarPstmt.setInt(2, idCliente);
                consultaActualizarPstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda el video de una sesión en la base de datos.
     *
     * @param idAdmin El ID del administrador.
     * @param socketCliente El socket del cliente.
     * @param rutaVideo La ruta del video grabado.
     */
    public void guardarVideo(int idAdmin, Socket socketCliente, String rutaVideo) {
        try (Connection conexion = ConexionBaseDatos.obtenerConexion()) {
            String consultaIdCliente = "SELECT id FROM Clientes WHERE ip = ?";
            PreparedStatement consultaIdClientePstmt = conexion.prepareStatement(consultaIdCliente);
            consultaIdClientePstmt.setString(1, socketCliente.getInetAddress().getHostAddress());
            int idCliente = -1;
            try (ResultSet rs = consultaIdClientePstmt.executeQuery()) {
                if (rs.next()) {
                    idCliente = rs.getInt("id");
                }
            }

            if (idCliente != -1) {
                String consultaInsertar = "INSERT INTO Videos (administrador_id, cliente_id, ruta_video) VALUES (?, ?, ?)";
                PreparedStatement consultaInsertarPstmt = conexion.prepareStatement(consultaInsertar);
                consultaInsertarPstmt.setInt(1, idAdmin);
                consultaInsertarPstmt.setInt(2, idCliente);
                consultaInsertarPstmt.setString(3, rutaVideo);
                consultaInsertarPstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserta un reporte en la base de datos.
     *
     * @param idConexion El ID de la conexión.
     */
    private void insertarReporte(int idConexion) {
        try (Connection conexion = ConexionBaseDatos.obtenerConexion()) {
            String consultaInsertarReporte = "INSERT INTO Reportes (conexiones_id) VALUES (?)";
            PreparedStatement consultaInsertarReportePstmt = conexion.prepareStatement(consultaInsertarReporte);
            consultaInsertarReportePstmt.setInt(1, idConexion);
            consultaInsertarReportePstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
