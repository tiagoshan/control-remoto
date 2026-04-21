package com.server;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase principal del servidor
 */
public class ServidorPrincipal {
    private InterfazServidor interfazServidor;
    private int idAdmin;

    public static void main(String[] args) {
        ServidorPrincipal servidorPrincipal = new ServidorPrincipal();
        GestorLogin gestorLogin = new GestorLogin();
        if (gestorLogin.iniciarSesion()) {
            servidorPrincipal.idAdmin = gestorLogin.obtenerIdAdmin();
            String puertoStr = JOptionPane.showInputDialog("Por favor, ingrese el puerto de escucha");
            try {
                int puerto = Integer.parseInt(puertoStr);
                servidorPrincipal.inicializar(puerto);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Puerto inválido. Saliendo del programa.");
                System.exit(1);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Login fallido. Saliendo del programa.");
            System.exit(0);
        }
    }

    public void inicializar(int puerto) {
        try (ServerSocket servidorSocket = new ServerSocket(puerto)) {
            interfazServidor = InterfazServidor.obtenerInstancia();
            interfazServidor.establecerIdAdmin(idAdmin);
            while (true) {
                Socket cliente = servidorSocket.accept();
                String idCliente = cliente.getInetAddress().toString() + ":" + cliente.getPort();
                System.out.println("Nuevo cliente conectado al servidor " + idCliente);
                interfazServidor.anadirCliente("Cliente " + idCliente, cliente);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
