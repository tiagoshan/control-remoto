    package com.server.gui;
    import javax.swing.*;
    import javax.swing.event.InternalFrameAdapter;
    import javax.swing.event.InternalFrameEvent;
    import java.awt.*;
    import java.awt.event.ActionListener;
    import java.awt.event.MouseAdapter;

    public class ClienteGUI {
        private JInternalFrame marcoInterno;
        private JPanel panelCliente;

        public ClienteGUI(ActionListener volverListener, MouseAdapter mouseAdapter, Runnable onCerrarListener) {
            marcoInterno = new JInternalFrame("Pantalla del Cliente", true, true, true);
            panelCliente = new JPanel(new BorderLayout());
            

            marcoInterno.setLayout(new BorderLayout());
            marcoInterno.getContentPane().add(panelCliente, BorderLayout.CENTER);
            marcoInterno.setSize(100, 100);
            marcoInterno.setVisible(true);

            // Escuchador para eventos de cierre
            marcoInterno.addInternalFrameListener(new InternalFrameAdapter() {
                @Override
                public void internalFrameClosing(InternalFrameEvent e) {
                    // Ejecutar acción personalizada al cerrar
                    if (onCerrarListener != null) {
                        onCerrarListener.run();
                    }
                }
            });

            panelCliente.addMouseListener(mouseAdapter);
        }

        public void agregarEscritorio(JDesktopPane escritorio) {
            escritorio.add(marcoInterno);
            try {
                marcoInterno.setMaximum(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            panelCliente.setFocusable(true);
        }

        public JInternalFrame obtenerMarcoInterno() {
            return marcoInterno;
        }

        public JPanel obtenerPanelCliente() {
            return panelCliente;
        }
    }
