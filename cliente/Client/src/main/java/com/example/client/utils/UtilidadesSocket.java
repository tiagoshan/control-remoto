package com.example.client.utils;

import java.net.Socket;

/**
 * Métodos utilitarios para manejo de sockets.
 */
public class UtilidadesSocket {

    private UtilidadesSocket() {}

    public static void cerrarSocket(Socket socket) {
        if (socket == null || socket.isClosed()) {
            return;
        }
        try {
            socket.close();
            System.out.println("Conexión cerrada.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
