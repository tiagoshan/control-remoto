package com.server.dao;

import com.server.database.ConexionBaseDatos;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO para manejar las conexiones de los clientes en la base de datos.
 */
public class ConexionClienteDAO {

    public void guardarConexionCliente(Socket socketCliente, int idAdmin) {
        String ip = socketCliente.getInetAddress().getHostAddress();
        try (Connection conexion = ConexionBaseDatos.obtenerConexion()) {
            int idCliente = obtenerOCrearCliente(conexion, ip);
            if (idCliente != -1) {
                String sql = "INSERT INTO Conexiones (cliente_id, administrador_id, fecha_conexion) VALUES (?, ?, NOW())";
                try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                    ps.setInt(1, idCliente);
                    ps.setInt(2, idAdmin);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarDesconexionCliente(Socket socketCliente) {
        String ip = socketCliente.getInetAddress().getHostAddress();
        try (Connection conexion = ConexionBaseDatos.obtenerConexion()) {
            int idCliente = obtenerIdCliente(conexion, ip);
            if (idCliente == -1) return;

            String sqlActualizar = "UPDATE Conexiones SET fecha_desconexion = NOW(), "
                + "duracion = TIMESTAMPDIFF(SECOND, fecha_conexion, NOW()) "
                + "WHERE cliente_id = ? AND fecha_desconexion IS NULL";
            try (PreparedStatement ps = conexion.prepareStatement(sqlActualizar)) {
                ps.setInt(1, idCliente);
                ps.executeUpdate();
            }

            String sqlUltimaConexion = "SELECT id FROM Conexiones WHERE cliente_id = ? ORDER BY fecha_conexion DESC LIMIT 1";
            try (PreparedStatement ps = conexion.prepareStatement(sqlUltimaConexion);
                 ResultSet rs = ejecutarConsultaConId(ps, idCliente)) {
                if (rs.next()) {
                    insertarReporte(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarContadorClicksCliente(Socket socketCliente, int cantidadClicks) {
        String ip = socketCliente.getInetAddress().getHostAddress();
        try (Connection conexion = ConexionBaseDatos.obtenerConexion()) {
            int idCliente = obtenerIdCliente(conexion, ip);
            if (idCliente == -1) return;

            String sql = "UPDATE Conexiones SET cantidad_clicks = ? WHERE cliente_id = ? AND fecha_desconexion IS NULL";
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, cantidadClicks);
                ps.setInt(2, idCliente);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void guardarVideo(int idAdmin, Socket socketCliente, String rutaVideo) {
        String ip = socketCliente.getInetAddress().getHostAddress();
        try (Connection conexion = ConexionBaseDatos.obtenerConexion()) {
            int idCliente = obtenerIdCliente(conexion, ip);
            if (idCliente == -1) return;

            String sql = "INSERT INTO Videos (administrador_id, cliente_id, ruta_video) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, idAdmin);
                ps.setInt(2, idCliente);
                ps.setString(3, rutaVideo);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertarReporte(int idConexion) {
        try (Connection conexion = ConexionBaseDatos.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement("INSERT INTO Reportes (conexiones_id) VALUES (?)")) {
            ps.setInt(1, idConexion);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int obtenerIdCliente(Connection conexion, String ip) throws SQLException {
        String sql = "SELECT id FROM Clientes WHERE ip = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ejecutarConsultaConIp(ps, ip)) {
            return rs.next() ? rs.getInt("id") : -1;
        }
    }

    private int obtenerOCrearCliente(Connection conexion, String ip) throws SQLException {
        int idCliente = obtenerIdCliente(conexion, ip);
        if (idCliente != -1) return idCliente;

        String sqlInsertar = "INSERT INTO Clientes (ip) VALUES (?)";
        try (PreparedStatement ps = conexion.prepareStatement(sqlInsertar, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ip);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    private ResultSet ejecutarConsultaConIp(PreparedStatement ps, String ip) throws SQLException {
        ps.setString(1, ip);
        return ps.executeQuery();
    }

    private ResultSet ejecutarConsultaConId(PreparedStatement ps, int id) throws SQLException {
        ps.setInt(1, id);
        return ps.executeQuery();
    }
}
