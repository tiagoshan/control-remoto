package com.server.actualizadores;

import com.server.dao.ConexionClienteDAO;
import java.net.Socket;

public class ActualizadorBaseDatos {
    private ConexionClienteDAO clientConnectionDAO;
    private int contadorClicks;

    public ActualizadorBaseDatos() {
        this.clientConnectionDAO = new ConexionClienteDAO();
    }

    public void actualizarContadorClicks(Socket cliente) {
        try {
            clientConnectionDAO.actualizarContadorClicksCliente(cliente, contadorClicks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarDesconexion(Socket cliente) {
        try {
            clientConnectionDAO.actualizarDesconexionCliente(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarVideo(int adminId, Socket cliente, String rutaArchivo) {
        try {
            clientConnectionDAO.guardarVideo(adminId, cliente, rutaArchivo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
