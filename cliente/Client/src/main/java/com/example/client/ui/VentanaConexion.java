package com.example.client.ui;

import javax.swing.*;
import java.util.function.BiConsumer;

/**
 * Ventana para obtener la IP y el puerto del servidor.
 */
public class VentanaConexion {

    public void mostrar(BiConsumer<String, Integer> alConectar) {
        String ip = JOptionPane.showInputDialog("Por favor, ingrese la IP del servidor:");
        String puertoStr = JOptionPane.showInputDialog("Por favor, ingrese el puerto:");

        try {
            int puerto = Integer.parseInt(puertoStr);
            alConectar.accept(ip, puerto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Puerto inválido. Inténtelo de nuevo.");
        }
    }
}
