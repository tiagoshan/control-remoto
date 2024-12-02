package com.example.client;

import java.awt.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Ejecuta comandos enviados desde el servidor.
 */
public class EjecutorComandosServidor extends Thread {

    private final Socket socket;
    private final Robot robot;
    private boolean ejecutando = true;

    public EjecutorComandosServidor(Socket socket, Robot robot) {
        this.socket = socket;
        this.robot = robot;
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(socket.getInputStream())) {
            while (ejecutando) {
                int comando = scanner.nextInt();
                procesarComando(comando, scanner);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void procesarComando(int comando, Scanner scanner) {
        switch (comando) {
            case -1 -> robot.mousePress(scanner.nextInt());
            case -2 -> robot.mouseRelease(scanner.nextInt());
            case -3 -> robot.keyPress(scanner.nextInt());
            case -4 -> robot.keyRelease(scanner.nextInt());
            case -5 -> robot.mouseMove(scanner.nextInt(), scanner.nextInt());
            default -> System.out.println("Comando desconocido: " + comando);
        }
    }

    public void detenerEjecucion() {
        ejecutando = false;
    }
}
