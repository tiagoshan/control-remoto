package com.server.fabricas;

import javax.swing.JDesktopPane;
import com.server.manejadores.AdministradorCliente;
import java.net.Socket;


public interface FabricaManejadorCliente {


    AdministradorCliente crearManejador(Socket cliente, JDesktopPane escritorio);
}
