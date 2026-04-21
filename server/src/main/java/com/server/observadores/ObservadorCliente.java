package com.server.observadores;

import java.net.Socket;

public interface ObservadorCliente {
    void onClienteAnadido(String nombreCliente, Socket socketCliente);
    void onClienteRemovido(Socket socketCliente);
}
