package com.server.fabricas;

import com.server.InterfazServidor;
import com.server.manejadores.AdministradorCliente;

import javax.swing.JDesktopPane;
import java.net.Socket;


public class FabricaAdministradorCliente implements FabricaManejadorCliente {
    private final InterfazServidor interfazServidor;

    public FabricaAdministradorCliente(InterfazServidor interfazServidor) {
        this.interfazServidor = interfazServidor;
    }


    @Override
    public AdministradorCliente crearManejador(Socket cliente, JDesktopPane escritorio) {
        return new AdministradorCliente(cliente, escritorio, interfazServidor);
    }
}
