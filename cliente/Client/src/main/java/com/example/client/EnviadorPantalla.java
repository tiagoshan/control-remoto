package com.example.client;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ObjectOutputStream;
import javax.swing.ImageIcon;
import java.net.Socket;

/**
 * Envía capturas de pantalla al servidor.
 */
public class EnviadorPantalla extends Thread {

    private final Socket socket;
    private final Robot robot;
    private final Rectangle limitesPantalla;
    private volatile boolean ejecutando = true;

    public EnviadorPantalla(Socket socket, Robot robot, Rectangle limitesPantalla) {
        this.socket = socket;
        this.robot = robot;
        this.limitesPantalla = limitesPantalla;
        setDaemon(true);
    }

    @Override
    public void run() {
        try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
            oos.writeObject(limitesPantalla);

            while (ejecutando) {
                BufferedImage captura = robot.createScreenCapture(limitesPantalla);
                oos.writeObject(new ImageIcon(captura));
                oos.reset();
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void detenerEnvio() {
        ejecutando = false;
    }
}
