package com.server;

import com.server.database.ConexionBaseDatos;
import org.mindrot.jbcrypt.BCrypt;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestorLogin {
    private int idAdmin;

    public boolean iniciarSesion() {
        JTextField campoUsuario = new JTextField();
        JPasswordField campoContraseña = new JPasswordField();
        Object[] mensaje = {
            "Usuario:", campoUsuario,
            "Contraseña:", campoContraseña
        };
        int opcion = JOptionPane.showConfirmDialog(null, mensaje, "Iniciar Sesión", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            String usuario = campoUsuario.getText();
            String contraseña = new String(campoContraseña.getPassword());
            return autenticar(usuario, contraseña);
        } else {
            return false;
        }
    }

    private boolean autenticar(String usuario, String contraseña) {
        String consulta = "SELECT id, contraseña FROM Administradores WHERE usuario = ?";
        try (Connection conexion = ConexionBaseDatos.obtenerConexion();
             PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
            declaracion.setString(1, usuario);
            try (ResultSet conjuntoResultados = declaracion.executeQuery()) {
                if (conjuntoResultados.next()) {
                    String contraseñaHasheada = conjuntoResultados.getString("contraseña");
                    if (BCrypt.checkpw(contraseña, contraseñaHasheada)) {
                        idAdmin = conjuntoResultados.getInt("id");
                        actualizarUltimaSesion(usuario);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void actualizarUltimaSesion(String usuario) {
        String consulta = "UPDATE Administradores SET ultima_sesion = NOW() WHERE usuario = ?";
        try (Connection conexion = ConexionBaseDatos.obtenerConexion();
             PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
            declaracion.setString(1, usuario);
            declaracion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int obtenerIdAdmin() {
        return idAdmin;
    }
}
