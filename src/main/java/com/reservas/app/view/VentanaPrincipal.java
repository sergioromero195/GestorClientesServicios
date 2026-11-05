package com.reservas.app.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.reservas.app.controller.ClienteController;
import com.reservas.app.model.Cliente;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private final ClienteController clienteController = new ClienteController();
    private Long clienteSeleccionadoId;

    public VentanaPrincipal() {
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        configurarTabla();
        configurarEstilo();
        conectarEventos();
        cargarClientes();

        pack();
        setLocationRelativeTo(null);
    }

    private void configurarTabla() {
        tabla.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Email", "Teléfono"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void configurarEstilo() {
        Color fondo = new Color(245, 247, 250);
        Color azul = new Color(45, 104, 196);
        Color verde = new Color(46, 125, 50);
        Color rojo = new Color(198, 40, 40);
        Color gris = new Color(90, 99, 110);

        panelPrincipal.setBackground(fondo);

        Font fuenteBase = new Font("Segoe UI", Font.PLAIN, 14);
        Font fuenteBoton = new Font("Segoe UI", Font.BOLD, 13);

        nombreTextField.setFont(fuenteBase);
        emailTextField.setFont(fuenteBase);
        telefonoTextField.setFont(fuenteBase);

        configurarBoton(guardarButton, verde);
        configurarBoton(actualizarButton, azul);
        configurarBoton(eliminarButton, rojo);
        configurarBoton(limpiarButton, gris);
        configurarBoton(serviciosButton, azul);
        configurarBoton(reservasButton, azul);

        tabla.setFont(fuenteBase);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setFont(fuenteBoton);
        tabla.getTableHeader().setBackground(new Color(224, 232, 244));
        tabla.getTableHeader().setForeground(new Color(35, 45, 60));
        tabla.setGridColor(new Color(220, 225, 232));
        tabla.setSelectionBackground(new Color(207, 224, 247));
        tabla.setSelectionForeground(Color.BLACK);
    }

    private void configurarBoton(JButton boton, Color color) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void conectarEventos() {
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCliente();
            }
        });

        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarCliente();
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarCliente();
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });

        tabla.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            mostrarClienteSeleccionado();
                        }
                    }
                }


        );

        serviciosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                VentanaServicios ventanaServicios = new VentanaServicios();
                ventanaServicios.setVisible(true);

            }
        });

        reservasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                VentanaReservas ventanaReservas = new VentanaReservas();
                ventanaReservas.setVisible(true);
            }
        });

    }

    private void cargarClientes() {
        try {
            List<Cliente> clientes = clienteController.listarClientes();
            DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
            modelo.setRowCount(0);

            for (Cliente cliente : clientes) {
                modelo.addRow(new Object[]{
                        cliente.getId(),
                        cliente.getNombre(),
                        cliente.getEmail(),
                        cliente.getTelefono()
                });
            }
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    private void mostrarClienteSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(fila);
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        clienteSeleccionadoId = ((Number) modelo.getValueAt(filaModelo, 0)).longValue();
        nombreTextField.setText(textoCelda(modelo, filaModelo, 1));
        emailTextField.setText(textoCelda(modelo, filaModelo, 2));
        telefonoTextField.setText(textoCelda(modelo, filaModelo, 3));
    }

    private String textoCelda(DefaultTableModel modelo, int fila, int columna) {
        Object valor = modelo.getValueAt(fila, columna);
        return valor == null ? "" : valor.toString();
    }

    private void guardarCliente() {
        try {
            Cliente cliente = leerFormulario();
            clienteController.guardarCliente(cliente);
            JOptionPane.showMessageDialog(this, "Cliente guardado.");
            cargarClientes();
            limpiarFormulario();
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    private void actualizarCliente() {
        if (clienteSeleccionadoId == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un cliente de la tabla para actualizarlo.",
                    "Ningún cliente seleccionado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            Cliente cliente = leerFormulario();
            cliente.setId(clienteSeleccionadoId);

            if (clienteController.actualizarCliente(cliente)) {
                JOptionPane.showMessageDialog(this, "Cliente actualizado.");
                cargarClientes();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this,
                        "El cliente ya no existe en la base de datos.",
                        "Cliente no encontrado", JOptionPane.WARNING_MESSAGE);
                cargarClientes();
                limpiarFormulario();
            }
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    private void eliminarCliente() {
        if (clienteSeleccionadoId == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un cliente de la tabla para eliminarlo.",
                    "Ningún cliente seleccionado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Quieres eliminar el cliente seleccionado?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (clienteController.eliminarCliente(clienteSeleccionadoId)) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "El cliente ya no existe en la base de datos.",
                        "Cliente no encontrado", JOptionPane.WARNING_MESSAGE);
            }
            cargarClientes();
            limpiarFormulario();
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    private Cliente leerFormulario() {
        Cliente cliente = new Cliente();
        cliente.setNombre(nombreTextField.getText().trim());
        cliente.setEmail(emailTextField.getText().trim());
        cliente.setTelefono(telefonoTextField.getText().trim());
        return cliente;
    }

    private void limpiarFormulario() {
        nombreTextField.setText("");
        emailTextField.setText("");
        telefonoTextField.setText("");
        clienteSeleccionadoId = null;
        tabla.clearSelection();
        nombreTextField.requestFocusInWindow();
    }

    private void mostrarError(RuntimeException error) {
        String mensaje = error.getMessage();
        if (mensaje == null || mensaje.isBlank()) {
            mensaje = error.getClass().getSimpleName();
        }
        JOptionPane.showMessageDialog(this, mensaje,
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    private JTextField nombreTextField;
    private JTextField emailTextField;
    private JTextField telefonoTextField;
    private JButton guardarButton;
    private JButton actualizarButton;
    private JButton eliminarButton;
    private JTable tabla;
    private JButton limpiarButton;
    private JPanel panelPrincipal;
    private JButton serviciosButton;
    private JButton reservasButton;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayoutManager(5, 42, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Gestión de clientes");
        label1.setVerticalAlignment(1);
        panelPrincipal.add(label1, new GridConstraints(0, 0, 1, 42, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(124, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Nombre:");
        panelPrincipal.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nombreTextField = new JTextField();
        panelPrincipal.add(nombreTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(250, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Teléfono:");
        panelPrincipal.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        telefonoTextField = new JTextField();
        panelPrincipal.add(telefonoTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Email:");
        panelPrincipal.add(label4, new GridConstraints(1, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        emailTextField = new JTextField();
        panelPrincipal.add(emailTextField, new GridConstraints(1, 4, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panelPrincipal.add(scrollPane1, new GridConstraints(4, 0, 1, 42, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tabla = new JTable();
        scrollPane1.setViewportView(tabla);
        serviciosButton = new JButton();
        serviciosButton.setText("Servicios");
        panelPrincipal.add(serviciosButton, new GridConstraints(3, 37, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        guardarButton = new JButton();
        guardarButton.setText("Guardar");
        panelPrincipal.add(guardarButton, new GridConstraints(3, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        eliminarButton = new JButton();
        eliminarButton.setText("Eliminar");
        panelPrincipal.add(eliminarButton, new GridConstraints(3, 8, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        limpiarButton = new JButton();
        limpiarButton.setText("Limpiar");
        panelPrincipal.add(limpiarButton, new GridConstraints(3, 15, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        actualizarButton = new JButton();
        actualizarButton.setText("Actualizar");
        panelPrincipal.add(actualizarButton, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reservasButton = new JButton();
        reservasButton.setText("Reservas");
        panelPrincipal.add(reservasButton, new GridConstraints(3, 22, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelPrincipal;
    }

}
