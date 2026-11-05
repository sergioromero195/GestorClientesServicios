package com.reservas.app.view;

import com.reservas.app.controller.ClienteController;
import com.reservas.app.controller.ReservaController;
import com.reservas.app.controller.ServicioController;
import com.reservas.app.model.Cliente;
import com.reservas.app.model.Reserva;
import com.reservas.app.model.Servicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class VentanaReservas extends JFrame {

    private final ClienteController clienteController =
            new ClienteController();
    private final ServicioController servicioController =
            new ServicioController();
    private final ReservaController reservaController =
            new ReservaController();

    private final JTextField fechaField = new JTextField(12);
    private final JComboBox<String> estadoComboBox =
            new JComboBox<>(new String[]{"Pendiente", "Confirmada", "Cancelada"});

    private final JComboBox<Cliente> clienteComboBox =
            new JComboBox<>();
    private final JComboBox<Servicio> servicioComboBox =
            new JComboBox<>();

    private final JButton guardarButton = new JButton("Guardar");
    private final JButton actualizarButton = new JButton("Actualizar");
    private final JButton eliminarButton = new JButton("Eliminar");
    private final JButton limpiarButton = new JButton("Limpiar");

    private final JTable reservasTabla;
    private Long reservaSeleccionadaId;

    public VentanaReservas() {
        setTitle("Gestión de reservas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        reservasTabla = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Fecha", "Estado", "Cliente", "Servicio"}
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        });

        estadoComboBox.setSelectedIndex(-1);
        construirInterfaz();
        configurarEstilo();
        configurarListasDesplegables();
        cargarClientesYServicios();
        conectarEventos();
        cargarReservas();

        pack();
        setLocationRelativeTo(null);
    }

    private void configurarEstilo() {
        Color fondo = new Color(245, 247, 250);
        Color azul = new Color(45, 104, 196);
        Color verde = new Color(46, 125, 50);
        Color rojo = new Color(198, 40, 40);
        Color gris = new Color(90, 99, 110);
        Font fuente = new Font("Segoe UI", Font.PLAIN, 14);

        getContentPane().setBackground(fondo);
        fechaField.setFont(fuente);
        estadoComboBox.setFont(fuente);
        clienteComboBox.setFont(fuente);
        servicioComboBox.setFont(fuente);

        configurarBoton(guardarButton, verde);
        configurarBoton(actualizarButton, azul);
        configurarBoton(eliminarButton, rojo);
        configurarBoton(limpiarButton, gris);

        reservasTabla.setFont(fuente);
        reservasTabla.setRowHeight(28);
        reservasTabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        reservasTabla.getTableHeader().setBackground(new Color(224, 232, 244));
        reservasTabla.getTableHeader().setForeground(new Color(35, 45, 60));
        reservasTabla.setGridColor(new Color(220, 225, 232));
        reservasTabla.setSelectionBackground(new Color(207, 224, 247));
        reservasTabla.setSelectionForeground(Color.BLACK);
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

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );

        JLabel titulo = new JLabel(
                "Gestión de reservas",
                SwingConstants.CENTER
        );
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.insets = new Insets(5, 5, 5, 5);
        restricciones.anchor = GridBagConstraints.WEST;
        restricciones.fill = GridBagConstraints.HORIZONTAL;

        agregarCampo(
                formulario, restricciones, 0, "Fecha (AAAA-MM-DD):", fechaField
        );
        agregarCampo(
                formulario, restricciones, 1, "Estado:", estadoComboBox
        );
        agregarCampo(
                formulario, restricciones, 2, "Cliente:", clienteComboBox
        );
        agregarCampo(
                formulario, restricciones, 3, "Servicio:", servicioComboBox
        );

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botones.add(guardarButton);
        botones.add(actualizarButton);
        botones.add(eliminarButton);
        botones.add(limpiarButton);

        JPanel zonaSuperior = new JPanel(new BorderLayout());
        zonaSuperior.add(formulario, BorderLayout.CENTER);
        zonaSuperior.add(botones, BorderLayout.SOUTH);

        panelPrincipal.add(zonaSuperior, BorderLayout.CENTER);

        reservasTabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollTabla = new JScrollPane(reservasTabla);
        scrollTabla.setPreferredSize(new Dimension(700, 200));
        panelPrincipal.add(scrollTabla, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }

    private void agregarCampo(
            JPanel panel,
            GridBagConstraints restricciones,
            int fila,
            String textoEtiqueta,
            Component campo
    ) {
        restricciones.gridy = fila;

        restricciones.gridx = 0;
        restricciones.weightx = 0;
        panel.add(new JLabel(textoEtiqueta), restricciones);

        restricciones.gridx = 1;
        restricciones.weightx = 1;
        panel.add(campo, restricciones);
    }

    private void configurarListasDesplegables() {
        clienteComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> lista,
                    Object valor,
                    int indice,
                    boolean seleccionado,
                    boolean tieneFoco
            ) {
                super.getListCellRendererComponent(
                        lista, valor, indice, seleccionado, tieneFoco
                );

                if (valor instanceof Cliente) {
                    Cliente cliente = (Cliente) valor;
                    setText(cliente.getNombre() + " (ID "
                            + cliente.getId() + ")");
                } else {
                    setText("Selecciona un cliente");
                }

                return this;
            }
        });

        servicioComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> lista,
                    Object valor,
                    int indice,
                    boolean seleccionado,
                    boolean tieneFoco
            ) {
                super.getListCellRendererComponent(
                        lista, valor, indice, seleccionado, tieneFoco
                );

                if (valor instanceof Servicio) {
                    Servicio servicio = (Servicio) valor;
                    setText(servicio.getNombre() + " (ID "
                            + servicio.getId() + ")");
                } else {
                    setText("Selecciona un servicio");
                }

                return this;
            }
        });
    }

    private void cargarClientesYServicios() {
        try {
            List<Cliente> clientes = clienteController.listarClientes();
            for (Cliente cliente : clientes) {
                clienteComboBox.addItem(cliente);
            }

            List<Servicio> servicios = servicioController.listarServicios();
            for (Servicio servicio : servicios) {
                servicioComboBox.addItem(servicio);
            }

            clienteComboBox.setSelectedIndex(-1);
            servicioComboBox.setSelectedIndex(-1);
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    private void conectarEventos() {
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                guardarReserva();
            }
        });

        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                actualizarReserva();
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                eliminarReserva();
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                limpiarFormulario();
            }
        });

        reservasTabla.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent evento) {
                        if (!evento.getValueIsAdjusting()) {
                            mostrarReservaSeleccionada();
                        }
                    }
                }
        );
    }

    private void cargarReservas() {
        try {
            List<Reserva> reservas = reservaController.listarReservas();

            DefaultTableModel modelo =
                    (DefaultTableModel) reservasTabla.getModel();
            modelo.setRowCount(0);

            for (Reserva reserva : reservas) {
                modelo.addRow(new Object[]{
                        reserva.getId(),
                        reserva.getFechaReserva(),
                        reserva.getEstado(),
                        reserva.getCliente().getNombre(),
                        reserva.getServicio().getNombre()
                });
            }
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    private void mostrarReservaSeleccionada() {
        int fila = reservasTabla.getSelectedRow();

        if (fila < 0) {
            return;
        }

        Long id = ((Number) reservasTabla.getValueAt(fila, 0)).longValue();
        Reserva reserva = reservaController.buscarReserva(id);

        if (reserva == null) {
            return;
        }

        reservaSeleccionadaId = reserva.getId();
        fechaField.setText(reserva.getFechaReserva().toString());
        estadoComboBox.setSelectedItem(reserva.getEstado());

        seleccionarCliente(reserva.getCliente().getId());
        seleccionarServicio(reserva.getServicio().getId());
    }

    private void seleccionarCliente(Long id) {
        for (int indice = 0; indice < clienteComboBox.getItemCount(); indice++) {
            Cliente cliente = clienteComboBox.getItemAt(indice);

            if (cliente.getId().equals(id)) {
                clienteComboBox.setSelectedIndex(indice);
                return;
            }
        }
    }

    private void seleccionarServicio(Long id) {
        for (int indice = 0;
             indice < servicioComboBox.getItemCount();
             indice++) {
            Servicio servicio = servicioComboBox.getItemAt(indice);

            if (servicio.getId().equals(id)) {
                servicioComboBox.setSelectedIndex(indice);
                return;
            }
        }
    }

    private void guardarReserva() {
        try {
            Reserva reserva = leerFormulario();
            reservaController.guardarReserva(reserva);

            JOptionPane.showMessageDialog(this, "Reserva guardada.");
            cargarReservas();
            limpiarFormulario();
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    private void actualizarReserva() {
        if (reservaSeleccionadaId == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una reserva de la tabla para actualizarla."
            );
            return;
        }

        try {
            Reserva reserva = leerFormulario();
            reserva.setId(reservaSeleccionadaId);

            if (reservaController.actualizarReserva(reserva)) {
                JOptionPane.showMessageDialog(this, "Reserva actualizada.");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "La reserva ya no existe en la base de datos."
                );
            }

            cargarReservas();
            limpiarFormulario();
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    private void eliminarReserva() {
        if (reservaSeleccionadaId == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una reserva de la tabla para eliminarla."
            );
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Quieres eliminar la reserva seleccionada?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (reservaController.eliminarReserva(reservaSeleccionadaId)) {
                JOptionPane.showMessageDialog(this, "Reserva eliminada.");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "La reserva ya no existe en la base de datos."
                );
            }

            cargarReservas();
            limpiarFormulario();
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    private Reserva leerFormulario() {
        LocalDate fecha;

        try {
            fecha = LocalDate.parse(fechaField.getText().trim());
        } catch (DateTimeParseException error) {
            throw new IllegalArgumentException(
                    "Escribe la fecha con el formato AAAA-MM-DD, "
                            + "por ejemplo 2026-10-28."
            );
        }

        String estado = (String) estadoComboBox.getSelectedItem();

        if (estado == null) {
            throw new IllegalArgumentException("Selecciona un estado.");
        }
        Cliente cliente = (Cliente) clienteComboBox.getSelectedItem();
        Servicio servicio = (Servicio) servicioComboBox.getSelectedItem();

        if (cliente == null) {
            throw new IllegalArgumentException(
                    "Selecciona un cliente."
            );
        }

        if (servicio == null) {
            throw new IllegalArgumentException(
                    "Selecciona un servicio."
            );
        }

        return new Reserva(fecha, estado, cliente, servicio);
    }

    private void limpiarFormulario() {
        fechaField.setText("");
        estadoComboBox.setSelectedIndex(-1);
        clienteComboBox.setSelectedIndex(-1);
        servicioComboBox.setSelectedIndex(-1);

        reservaSeleccionadaId = null;
        reservasTabla.clearSelection();
        fechaField.requestFocusInWindow();
    }

    private void mostrarError(RuntimeException error) {
        String mensaje = error.getMessage();

        if (mensaje == null || mensaje.isBlank()) {
            mensaje = error.getClass().getSimpleName();
        }

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
