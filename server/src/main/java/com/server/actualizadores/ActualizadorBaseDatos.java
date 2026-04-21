package com.server.actualizadores;

import com.server.dao.ConexionClienteDAO;
import java.net.Socket;

public class ActualizadorBaseDatos {
    private final ConexionClienteDAO daoConexionCliente;

    public ActualizadorBaseDatos() {
        this.daoConexionCliente = new ConexionClienteDAO();
    }

    public void actualizarContadorClicks(Socket cliente, int contadorClicks) {
        try {
            daoConexionCliente.actualizarContadorClicksCliente(cliente, contadorClicks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarDesconexion(Socket cliente) {
        try {
            daoConexionCliente.actualizarDesconexionCliente(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarVideo(int adminId, Socket cliente, String rutaArchivo) {
        try {
            daoConexionCliente.guardarVideo(adminId, cliente, rutaArchivo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
