package com.example.client.utils;

import javax.swing.*;
import java.net.Socket;

/**
 * Métodos utilitarios para manejo de sockets.
 */
public class UtilidadesSocket {

    public static void cerrarSocket(Socket socket) {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Conexión cerrada.");
                JOptionPane.showMessageDialog(null, "La conexión ha sido cerrada.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
