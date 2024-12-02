package com.server;

import com.server.dao.ConexionClienteDAO;
import com.server.fabricas.FabricaAdministradorCliente;
import com.server.manejadores.AdministradorCliente;
import com.server.observadores.ObservadorCliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterfazServidor implements ObservadorCliente {
    private static InterfazServidor instancia;
    private final JFrame marco;
    private final JPanel panelPrincipal;
    private final DefaultListModel<String> modeloListaClientes;
    private final List<Socket> socketsClientes;
    private final List<JButton> botonesClientes;
    private final FabricaAdministradorCliente fabricaCliente;
    private final ConexionClienteDAO conexionClienteDAO;
    private final List<ObservadorCliente> observadores;
    private int idAdmin;
    private Map<String, AdministradorCliente> clientes = new HashMap<>();

    public Map<String, AdministradorCliente> obtenerClientes() {
        return clientes;
    }

    private InterfazServidor() {
        marco = new JFrame("SERVIDOR PRINCIPAL");
        panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int ancho = getWidth();
                int alto = getHeight();
                Color color1 = new Color(52, 152, 219); // Azul suave
                Color color2 = new Color(41, 128, 185); // Azul más oscuro
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, alto, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, ancho, alto);
            }
        };
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        modeloListaClientes = new DefaultListModel<>();
        socketsClientes = new ArrayList<>();
        botonesClientes = new ArrayList<>();
        fabricaCliente = new FabricaAdministradorCliente(this);
        conexionClienteDAO = new ConexionClienteDAO();
        observadores = new ArrayList<>();
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

        // Cabecera
        JLabel etiquetaCabecera = new JLabel("Clientes Conectados", JLabel.CENTER);
        etiquetaCabecera.setFont(new Font("SansSerif", Font.BOLD, 28));
        etiquetaCabecera.setForeground(Color.WHITE);
        etiquetaCabecera.setOpaque(true);
        etiquetaCabecera.setBackground(new Color(44, 62, 80));
        etiquetaCabecera.setBorder(new EmptyBorder(10, 10, 10, 10));
        marco.add(etiquetaCabecera, BorderLayout.NORTH);

        // Scroll para lista de clientes
        JScrollPane scrollPanel = new JScrollPane(panelPrincipal);
        scrollPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        marco.add(scrollPanel, BorderLayout.CENTER);

        // Pie de página
        JLabel pie = new JLabel("Servidor en ejecución", JLabel.CENTER);
        pie.setFont(new Font("SansSerif", Font.ITALIC, 14));
        pie.setForeground(Color.WHITE);
        pie.setOpaque(true);
        pie.setBackground(new Color(34, 49, 63));
        pie.setBorder(new EmptyBorder(5, 5, 5, 5));
        marco.add(pie, BorderLayout.SOUTH);

        // Configuración final
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setSize(900, 600);
        marco.setLocationRelativeTo(null);
        marco.setVisible(true);
    }

    public void anadirCliente(String nombreCliente, Socket socketCliente) {
        JButton botonCliente = crearBotonCliente(nombreCliente, socketCliente);
        panelPrincipal.add(Box.createVerticalStrut(10));
        panelPrincipal.add(botonCliente);
        panelPrincipal.revalidate();
        panelPrincipal.repaint();

        modeloListaClientes.addElement(nombreCliente);
        socketsClientes.add(socketCliente);
        botonesClientes.add(botonCliente);

        System.out.println("Cliente añadido a la interfaz: " + nombreCliente);
        conexionClienteDAO.guardarConexionCliente(socketCliente, idAdmin);
    }

    private JButton crearBotonCliente(String nombreCliente, Socket socketCliente) {
        JButton botonCliente = new JButton(nombreCliente);
        botonCliente.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonCliente.setMaximumSize(new Dimension(Integer.MAX_VALUE, botonCliente.getMinimumSize().height));
        botonCliente.setBackground(new Color(39, 174, 96)); // Verde suave
        botonCliente.setForeground(Color.WHITE);
        botonCliente.setFont(new Font("SansSerif", Font.BOLD, 16));
        botonCliente.setFocusPainted(false);
        botonCliente.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        botonCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonCliente.setBackground(new Color(30, 130, 76)); // Verde más oscuro
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                botonCliente.setBackground(new Color(39, 174, 96)); // Volver al verde original
            }
        });

        botonCliente.addActionListener(e -> abrirPantallaCliente(socketCliente, nombreCliente));
        return botonCliente;
    }

    public void removerCliente(Socket socketCliente) {
        int indice = socketsClientes.indexOf(socketCliente);    
        if (indice != -1) {
            modeloListaClientes.remove(indice);
            panelPrincipal.remove(botonesClientes.get(indice));
            socketsClientes.remove(indice);
            botonesClientes.remove(indice);
            panelPrincipal.revalidate();
            panelPrincipal.repaint();

            System.out.println("Cliente removido de la interfaz");
            conexionClienteDAO.actualizarDesconexionCliente(socketCliente);
        }
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

    // Métodos para los observadores
    public void anadirObservador(ObservadorCliente observador) {
        observadores.add(observador);
    }

    public void eliminarObservador(ObservadorCliente observador) {
        observadores.remove(observador);
    }

    private void notificarClienteAnadido(String nombreCliente, Socket socketCliente) {
        for (ObservadorCliente observador : observadores) {
            observador.onClienteAnadido(nombreCliente, socketCliente);
        }
    }

    private void notificarClienteRemovido(Socket socketCliente) {
        for (ObservadorCliente observador : observadores) {
            observador.onClienteRemovido(socketCliente);
        }
    }

    public List<Socket> obtenerSocketsClientes() {
        return socketsClientes;
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
        notificarClienteAnadido(nombreCliente, socketCliente); // Notificar a los observadores
    }

    @Override
    public void onClienteRemovido(Socket socketCliente) {
        removerCliente(socketCliente);
        notificarClienteRemovido(socketCliente); // Notificar a los observadores
    }
}
