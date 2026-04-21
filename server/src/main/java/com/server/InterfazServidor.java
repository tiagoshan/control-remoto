package com.server;

import com.server.dao.ConexionClienteDAO;
import com.server.fabricas.FabricaAdministradorCliente;
import com.server.observadores.ObservadorCliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

public class InterfazServidor implements ObservadorCliente {
    private static InterfazServidor instancia;
    private final JFrame marco;
    private final JPanel panelPrincipal;
    private final FabricaAdministradorCliente fabricaCliente;
    private final ConexionClienteDAO conexionClienteDAO;
    private int idAdmin;
    private final Map<Socket, JButton> botonesClientes = new LinkedHashMap<>();

    private InterfazServidor() {
        marco = new JFrame("SERVIDOR PRINCIPAL");
        panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int ancho = getWidth();
                int alto = getHeight();
                Color color1 = new Color(52, 152, 219);
                Color color2 = new Color(41, 128, 185);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, alto, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, ancho, alto);
            }
        };
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        fabricaCliente = new FabricaAdministradorCliente(this);
        conexionClienteDAO = new ConexionClienteDAO();
        configurarInterfaz();
    }

    public static synchronized InterfazServidor obtenerInstancia() {
        if (instancia == null) {
            instancia = new InterfazServidor();
        }
        return instancia;
    }

    private void configurarInterfaz() {
        marco.setLayout(new BorderLayout());

        JLabel etiquetaCabecera = new JLabel("Clientes Conectados", JLabel.CENTER);
        etiquetaCabecera.setFont(new Font("SansSerif", Font.BOLD, 28));
        etiquetaCabecera.setForeground(Color.WHITE);
        etiquetaCabecera.setOpaque(true);
        etiquetaCabecera.setBackground(new Color(44, 62, 80));
        etiquetaCabecera.setBorder(new EmptyBorder(10, 10, 10, 10));
        marco.add(etiquetaCabecera, BorderLayout.NORTH);

        JScrollPane scrollPanel = new JScrollPane(panelPrincipal);
        scrollPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        marco.add(scrollPanel, BorderLayout.CENTER);

        JLabel pie = new JLabel("Servidor en ejecución", JLabel.CENTER);
        pie.setFont(new Font("SansSerif", Font.ITALIC, 14));
        pie.setForeground(Color.WHITE);
        pie.setOpaque(true);
        pie.setBackground(new Color(34, 49, 63));
        pie.setBorder(new EmptyBorder(5, 5, 5, 5));
        marco.add(pie, BorderLayout.SOUTH);

        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setSize(900, 600);
        marco.setLocationRelativeTo(null);
        marco.setVisible(true);
    }

    public void anadirCliente(String nombreCliente, Socket socketCliente) {
        SwingUtilities.invokeLater(() -> {
            JButton botonCliente = crearBotonCliente(nombreCliente, socketCliente);
            panelPrincipal.add(Box.createVerticalStrut(10));
            panelPrincipal.add(botonCliente);
            panelPrincipal.revalidate();
            panelPrincipal.repaint();
            botonesClientes.put(socketCliente, botonCliente);
        });
        System.out.println("Cliente añadido a la interfaz: " + nombreCliente);
        conexionClienteDAO.guardarConexionCliente(socketCliente, idAdmin);
    }

    private JButton crearBotonCliente(String nombreCliente, Socket socketCliente) {
        JButton botonCliente = new JButton(nombreCliente);
        botonCliente.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonCliente.setMaximumSize(new Dimension(Integer.MAX_VALUE, botonCliente.getMinimumSize().height));
        botonCliente.setBackground(new Color(39, 174, 96));
        botonCliente.setForeground(Color.WHITE);
        botonCliente.setFont(new Font("SansSerif", Font.BOLD, 16));
        botonCliente.setFocusPainted(false);
        botonCliente.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        botonCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonCliente.setBackground(new Color(30, 130, 76));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                botonCliente.setBackground(new Color(39, 174, 96));
            }
        });

        botonCliente.addActionListener(e -> abrirPantallaCliente(socketCliente, nombreCliente));
        return botonCliente;
    }

    public void removerCliente(Socket socketCliente) {
        SwingUtilities.invokeLater(() -> {
            JButton boton = botonesClientes.remove(socketCliente);
            if (boton != null) {
                panelPrincipal.remove(boton);
                panelPrincipal.revalidate();
                panelPrincipal.repaint();
            }
        });
        System.out.println("Cliente removido de la interfaz");
        conexionClienteDAO.actualizarDesconexionCliente(socketCliente);
    }

    private void abrirPantallaCliente(Socket socketCliente, String nombreCliente) {
        JFrame marcoCliente = new JFrame("Controlando: " + nombreCliente);
        JDesktopPane escritorioCliente = new JDesktopPane();
        marcoCliente.add(escritorioCliente, BorderLayout.CENTER);
        marcoCliente.setSize(800, 600);
        marcoCliente.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        marcoCliente.setVisible(true);

        fabricaCliente.crearManejador(socketCliente, escritorioCliente).start();
    }

    public void establecerIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    public int obtenerIdAdmin() {
        return idAdmin;
    }

    @Override
    public void onClienteAnadido(String nombreCliente, Socket socketCliente) {
        anadirCliente(nombreCliente, socketCliente);
    }

    @Override
    public void onClienteRemovido(Socket socketCliente) {
        removerCliente(socketCliente);
    }
}
