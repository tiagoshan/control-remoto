package com.example.client;

import com.example.client.ui.VentanaConexion;
import com.example.client.utils.UtilidadesSocket;

import java.awt.*;
import java.net.Socket;

/**
 * Punto de entrada para la aplicación cliente.
 */
public class AplicacionCliente {

    private Socket socket;

    public static void main(String[] args) {
        new AplicacionCliente().iniciar();
    }

    public void iniciar() {
        VentanaConexion ventanaConexion = new VentanaConexion();
        ventanaConexion.mostrar((ip, puerto) -> inicializarConexion(ip, puerto));
    }

    private void inicializarConexion(String ip, int puerto) {
        try {
            System.out.println("Conectando al servidor...");
            socket = new Socket(ip, puerto);
            System.out.println("Conexión establecida.");

            Robot robot = new Robot();
            Dimension tamañoPantalla = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle limitesPantalla = new Rectangle(tamañoPantalla);

            new EnviadorPantalla(socket, robot, limitesPantalla).start();
            new EjecutorComandosServidor(socket, robot).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cerrarConexion() {
        UtilidadesSocket.cerrarSocket(socket);
    }
}
