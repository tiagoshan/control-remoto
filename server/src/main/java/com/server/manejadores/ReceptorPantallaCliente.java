package com.server.manejadores;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;

public class ReceptorPantallaCliente extends Thread {
    private ObjectInputStream objetoEntrada;
    private JPanel panel;


    public ReceptorPantallaCliente(ObjectInputStream objetoEntrada, JPanel panel) {
        this.objetoEntrada = objetoEntrada;
        this.panel = panel;
    }

    @Override
    public void run() {
        try {

            while (true) {
                ImageIcon iconoImagen = (ImageIcon) objetoEntrada.readObject();
                System.out.println("Nueva imagen recibida");
                Image imagen = iconoImagen.getImage();
                imagen = imagen.getScaledInstance(panel.getWidth(), panel.getHeight(), Image.SCALE_SMOOTH);
                Graphics graficos = panel.getGraphics();
                graficos.drawImage(imagen, 0, 0, panel.getWidth(), panel.getHeight(), panel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
