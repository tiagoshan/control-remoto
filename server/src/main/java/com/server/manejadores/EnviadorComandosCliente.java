package com.server.manejadores;

import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

public class EnviadorComandosCliente extends Thread implements KeyListener, MouseMotionListener, MouseListener {
    private final JPanel panel;
    private final PrintWriter writer;
    private final Rectangle dimensionCliente;

    public EnviadorComandosCliente(Socket socket, JPanel panel, Rectangle dimensionCliente) {
        this.panel = panel;
        this.dimensionCliente = dimensionCliente;
        panel.addKeyListener(this);
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.writer = pw;
    }

    public void mouseDragged(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {
        if (writer == null) return;
        double xScale = dimensionCliente.getWidth() / panel.getWidth();
        double yScale = dimensionCliente.getHeight() / panel.getHeight();
        writer.println(-5);
        writer.println((int) (e.getX() * xScale));
        writer.println((int) (e.getY() * yScale));
        writer.flush();
    }

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        if (writer == null) return;
        writer.println(-1);
        writer.println(resolverBotonRaton(e.getButton()));
        writer.flush();
    }

    public void mouseReleased(MouseEvent e) {
        if (writer == null) return;
        writer.println(-2);
        writer.println(resolverBotonRaton(e.getButton()));
        writer.flush();
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        if (writer == null) return;
        writer.println(-3);
        writer.println(e.getKeyCode());
        writer.flush();
    }

    public void keyReleased(KeyEvent e) {
        if (writer == null) return;
        writer.println(-4);
        writer.println(e.getKeyCode());
        writer.flush();
    }

    private int resolverBotonRaton(int button) {
        return button == MouseEvent.BUTTON3 ? 4 : 16;
    }
}
