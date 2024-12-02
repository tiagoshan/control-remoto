package com.server.manejadores;

import com.server.InterfazServidor;
import com.server.actualizadores.ActualizadorBaseDatos;
import com.server.gui.ClienteGUI;
import com.server.util.GrabadorPantalla;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;


public class AdministradorCliente extends Thread {
    private Socket cliente;
    private JDesktopPane escritorio;
    private InterfazServidor interfazServidor;
    private ActualizadorBaseDatos actualizadorBaseDatos;
    private GrabadorPantalla grabadorPantalla;
    private ClienteGUI clienteGUI;
    private JPanel cPanel = new JPanel();
    private int contadorClicks = 0;

    public AdministradorCliente(Socket cliente, JDesktopPane escritorio, InterfazServidor interfazServidor) {
        this.cliente = cliente;
        this.escritorio = escritorio;
        this.interfazServidor = interfazServidor;
        this.actualizadorBaseDatos = new ActualizadorBaseDatos();
    
        this.clienteGUI = new ClienteGUI(
            e -> {
                try {
                    detenerGrabacion();
                    clienteGUI.obtenerMarcoInterno().dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }, 
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    contadorClicks++;
                    actualizadorBaseDatos.actualizarContadorClicks(cliente);
                }
            },
            () -> {
                // Lógica al cerrar la ventana del cliente
                System.out.println("Cerrando la ventana del cliente: " + cliente);
                interfazServidor.removerCliente(cliente); // Remover cliente del servidor
                try {
                    detenerGrabacion();
                } catch (Exception e1) {
                    e1.printStackTrace();
                } 
            }
        );
    }
    

    @Override
public void run() {
    // Agregar el cliente al escritorio
    clienteGUI.agregarEscritorio(escritorio);
    
    try (ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream())) {
        // Obtener dimensiones de la pantalla del cliente
        Rectangle clientScreenDim = (Rectangle) ois.readObject();
        
        // Iniciar receptor de pantalla
        new ReceptorPantallaCliente(ois, clienteGUI.obtenerPanelCliente()).start();

        // Crear y asociar el enviador de comandos
        EnviadorComandosCliente enviadorComandos = new EnviadorComandosCliente(
            cliente, 
            clienteGUI.obtenerPanelCliente(), 
            clientScreenDim
        );
        enviadorComandos.start();

        // Iniciar grabación de pantalla
        iniciarGrabacion();

        // Ciclo de monitoreo de conexión
        while (!cliente.isClosed() && cliente.isConnected()) {
            Thread.sleep(5000); // Verificar cada 5 segundos
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        // Limpieza final
        interfazServidor.removerCliente(cliente);
        actualizadorBaseDatos.actualizarDesconexion(cliente);
        try {
            detenerGrabacion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


    private void iniciarGrabacion() throws Exception {
        String nombreVideo = "admin_" + System.currentTimeMillis() + ".mp4";
        grabadorPantalla = new GrabadorPantalla(nombreVideo, 30, 30);
        grabadorPantalla.iniciarGrabacion();
    }

    private void detenerGrabacion() throws Exception {
        if (grabadorPantalla != null) {
            grabadorPantalla.detenerGrabacion();
            actualizadorBaseDatos.guardarVideo(interfazServidor.obtenerIdAdmin(), cliente, grabadorPantalla.getRutaArchivo());
            grabadorPantalla = null;
        }
    }
}
