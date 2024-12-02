package com.server;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Clase principal del servidor
 */
public class ServidorPrincipal {
    private InterfazServidor interfazServidor;
    private static final int MAX_CLIENTES = 10; // Máximo número de clientes
    private ExecutorService servicioEjecutor;
    private int idAdmin;

    public static void main(String[] args) {
        ServidorPrincipal servidorPrincipal = new ServidorPrincipal();
        GestorLogin gestorLogin = new GestorLogin();
        if (gestorLogin.iniciarSesion()) {
            servidorPrincipal.idAdmin = gestorLogin.obtenerIdAdmin();
            String puerto = JOptionPane.showInputDialog("Por favor, ingrese el puerto de escucha");
            servidorPrincipal.inicializar(Integer.parseInt(puerto));
        } else {
            JOptionPane.showMessageDialog(null, "Login fallido. Saliendo del programa.");
            System.exit(0);
        }
    }

    public void inicializar(int puerto) {
        servicioEjecutor = Executors.newFixedThreadPool(MAX_CLIENTES);
        try (ServerSocket servidorSocket = new ServerSocket(puerto)) {
            interfazServidor = InterfazServidor.obtenerInstancia();
            interfazServidor.establecerIdAdmin(idAdmin);
            while (true) {
                Socket cliente = servidorSocket.accept();
                String idCliente = cliente.getInetAddress().toString() + ":" + cliente.getPort();
                
                System.out.println("Nuevo cliente conectado al servidor " + idCliente);
                String nombreCliente = "Cliente " + idCliente;
                interfazServidor.anadirCliente(nombreCliente, cliente);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
