package com.reservas.app.view;

import com.reservas.app.controller.ClienteController;
import com.reservas.app.model.Cliente;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
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

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
