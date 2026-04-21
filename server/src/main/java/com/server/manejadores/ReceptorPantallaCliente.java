package com.server.manejadores;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;

public class ReceptorPantallaCliente extends Thread {
    private final ObjectInputStream objetoEntrada;
    private final JPanel panel;
    private volatile boolean ejecutando = true;

    public ReceptorPantallaCliente(ObjectInputStream objetoEntrada, JPanel panel) {
        this.objetoEntrada = objetoEntrada;
        this.panel = panel;
        setDaemon(true);
    }

    @Override
    public void run() {
        try {
            while (ejecutando) {
                ImageIcon iconoImagen = (ImageIcon) objetoEntrada.readObject();
                Image imagen = iconoImagen.getImage()
                    .getScaledInstance(panel.getWidth(), panel.getHeight(), Image.SCALE_SMOOTH);
                Graphics graficos = panel.getGraphics();
                if (graficos != null) {
                    graficos.drawImage(imagen, 0, 0, panel.getWidth(), panel.getHeight(), panel);
                    graficos.dispose();
                }
            }
        } catch (Exception e) {
            if (ejecutando) {
                e.printStackTrace();
            }
        }
    }

    public void detener() {
        ejecutando = false;
    }
}
