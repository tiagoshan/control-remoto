package com.server.manejadores;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.awt.Rectangle;
import com.server.gui.ClienteGUI;

public class ManejadorConexion extends Thread {
    private Socket cliente;
    private ObjectInputStream ois;
    private ClienteGUI clienteGUI;

    public ManejadorConexion(Socket cliente, ClienteGUI clienteGUI) {
        this.cliente = cliente;
        this.clienteGUI = clienteGUI;
    }

    @Override
    public void run() {
        try {
            ois = new ObjectInputStream(cliente.getInputStream());
            Rectangle dimensionPantallaCliente = (Rectangle) ois.readObject();
            new ReceptorPantallaCliente(ois, clienteGUI.obtenerPanelCliente()).start();
            new EnviadorComandosCliente(cliente, clienteGUI.obtenerPanelCliente(), dimensionPantallaCliente).start();
            while (!cliente.isClosed() && cliente.isConnected()) {
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
