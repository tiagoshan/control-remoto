package com.server.manejadores;

import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

public class EnviadorComandosCliente extends Thread implements KeyListener, MouseMotionListener, MouseListener {
    private Socket s;
    private JPanel p;
    private PrintWriter writer;
    private Rectangle r;

    public EnviadorComandosCliente(Socket s, JPanel p, Rectangle r) {
        this.s = s;
        this.p = p;
        this.r = r;
        // Asociar oyentes de eventos al panel
        p.addKeyListener(this);
        p.addMouseListener(this);
        p.addMouseMotionListener(this);
        try {
            // Preparar PrintWriter que se utilizará para enviar comandos
            // al cliente
            writer = new PrintWriter(s.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mouseDragged(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {
        // Encontrar la relación entre la pantalla del cliente y el tamaño de la pantalla del servidor dividiéndolas
        double xScale = r.getWidth() / p.getWidth();
        System.out.println("xScale: " + xScale);
        double yScale = r.getHeight() / p.getHeight();
        System.out.println("yScale: " + yScale);
        System.out.println("Mouse Moved");
        writer.println(-5);
        writer.println((int)(e.getX() * xScale));
        writer.println((int)(e.getY() * yScale));
        writer.flush();
    }

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse Pressed");
        writer.println(-1);
        int button = e.getButton();
        // Primero asumimos que se hace clic en el botón izquierdo
        int xButton = 16;
        if (button == MouseEvent.BUTTON3) { // Si se hace clic en el botón derecho
            xButton = 4;
        }
        // xButton es el valor utilizado para indicar a la clase Robot qué botón del ratón se presionó
        writer.println(xButton);
        writer.flush();
    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse Released");
        writer.println(-2);
        int button = e.getButton();
        System.out.println(button);
        int xButton = 16;
        if (button == MouseEvent.BUTTON3) {
            xButton = 4;
        }
        writer.println(xButton);
        writer.flush();
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        System.out.println("Key Pressed");
        writer.println(-3);
        writer.println(e.getKeyCode());
        writer.flush();
    }

    public void keyReleased(KeyEvent e) {
        System.out.println("Key Released");
        writer.println(-4);
        writer.println(e.getKeyCode());
        writer.flush();
    }
}
