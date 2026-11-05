package com.reservas.app.view;

import com.reservas.app.controller.ServicioController;
import com.reservas.app.model.Servicio;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

public class VentanaServicios extends JFrame {

    private final ServicioController servicioController =
            new ServicioController();

    private final JTextField nombreField = new JTextField(20);
    private final JTextField descripcionField = new JTextField(20);
    private final JTextField precioField = new JTextField(10);

    private final JButton guardarButton = new JButton("Guardar");
    private final JButton actualizarButton = new JButton("Actualizar");
    private final JButton eliminarButton = new JButton("Eliminar");
    private final JButton limpiarButton = new JButton("Limpiar");

    private final JTable serviciosTabla;
    private Long servicioSeleccionadoId;

    public VentanaServicios() {
        setTitle("Gestión de servicios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        serviciosTabla = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Descripción", "Precio"}
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        });

        construirInterfaz();
        configurarEstilo();
        conectarEventos();
        cargarServicios();

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
        nombreField.setFont(fuente);
        descripcionField.setFont(fuente);
        precioField.setFont(fuente);

        configurarBoton(guardarButton, verde);
        configurarBoton(actualizarButton, azul);
        configurarBoton(eliminarButton, rojo);
        configurarBoton(limpiarButton, gris);

        serviciosTabla.setFont(fuente);
        serviciosTabla.setRowHeight(28);
        serviciosTabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        serviciosTabla.getTableHeader().setBackground(new Color(224, 232, 244));
        serviciosTabla.getTableHeader().setForeground(new Color(35, 45, 60));
        serviciosTabla.setGridColor(new Color(220, 225, 232));
        serviciosTabla.setSelectionBackground(new Color(207, 224, 247));
        serviciosTabla.setSelectionForeground(Color.BLACK);
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
                "Gestión de servicios",
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
                formulario, restricciones, 0, "Nombre:", nombreField
        );
        agregarCampo(
                formulario, restricciones, 1, "Descripción:", descripcionField
        );
        agregarCampo(
                formulario, restricciones, 2, "Precio:", precioField
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

        serviciosTabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollTabla = new JScrollPane(serviciosTabla);
        panelPrincipal.add(scrollTabla, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }

    private void agregarCampo(
            JPanel panel,
            GridBagConstraints restricciones,
            int fila,
            String textoEtiqueta,
            JTextField campo
    ) {
        restricciones.gridy = fila;

        restricciones.gridx = 0;
        restricciones.weightx = 0;
        panel.add(new JLabel(textoEtiqueta), restricciones);

        restricciones.gridx = 1;
        restricciones.weightx = 1;
        panel.add(campo, restricciones);
    }

    private void conectarEventos() {
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                guardarServicio();
            }
        });

        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                actualizarServicio();
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                eliminarServicio();
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                limpiarFormulario();
            }
        });

        serviciosTabla.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent evento) {
                        if (!evento.getValueIsAdjusting()) {
                            mostrarServicioSeleccionado();
                        }
                    }
                }
        );
    }

    private void cargarServicios() {
        try {
            List<Servicio> servicios =
                    servicioController.listarServicios();

            DefaultTableModel modelo =
                    (DefaultTableModel) serviciosTabla.getModel();

            modelo.setRowCount(0);

            for (Servicio servicio : servicios) {
                modelo.addRow(new Object[]{
                        servicio.getId(),
                        servicio.getNombre(),
                        servicio.getDescripcion(),
                        servicio.getPrecio()
                });
            }
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    private void mostrarServicioSeleccionado() {
        int fila = serviciosTabla.getSelectedRow();

        if (fila < 0) {
            return;
        }

        servicioSeleccionadoId =
                ((Number) serviciosTabla.getValueAt(fila, 0)).longValue();

        nombreField.setText(textoCelda(fila, 1));
        descripcionField.setText(textoCelda(fila, 2));
        precioField.setText(textoCelda(fila, 3));
    }

    private String textoCelda(int fila, int columna) {
        Object valor = serviciosTabla.getValueAt(fila, columna);

        if (valor == null) {
            return "";
        }

        return valor.toString();
    }

    private void guardarServicio() {
        try {
            Servicio servicio = leerFormulario();
            servicioController.guardarServicio(servicio);

            JOptionPane.showMessageDialog(this, "Servicio guardado.");

            cargarServicios();
            limpiarFormulario();
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    private void actualizarServicio() {
        if (servicioSeleccionadoId == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un servicio de la tabla para actualizarlo."
            );
            return;
        }

        try {
            Servicio servicio = leerFormulario();
            servicio.setId(servicioSeleccionadoId);

            if (servicioController.actualizarServicio(servicio)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Servicio actualizado."
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "El servicio ya no existe en la base de datos."
                );
            }

            cargarServicios();
            limpiarFormulario();
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    private void eliminarServicio() {
        if (servicioSeleccionadoId == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un servicio de la tabla para eliminarlo."
            );
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Quieres eliminar el servicio seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (servicioController.eliminarServicio(servicioSeleccionadoId)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Servicio eliminado."
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "El servicio ya no existe en la base de datos."
                );
            }

            cargarServicios();
            limpiarFormulario();
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    private Servicio leerFormulario() {
        String nombre = nombreField.getText().trim();
        String descripcion = descripcionField.getText().trim();
        String textoPrecio = precioField.getText().trim().replace(',', '.');

        if (nombre.isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre es obligatorio."
            );
        }

        if (textoPrecio.isEmpty()) {
            throw new IllegalArgumentException(
                    "El precio es obligatorio."
            );
        }

        BigDecimal precio;

        try {
            precio = new BigDecimal(textoPrecio);
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException(
                    "El precio debe ser un número, por ejemplo: 19.99."
            );
        }

        if (precio.signum() < 0) {
            throw new IllegalArgumentException(
                    "El precio no puede ser negativo."
            );
        }

        Servicio servicio = new Servicio();
        servicio.setNombre(nombre);
        servicio.setDescripcion(descripcion);
        servicio.setPrecio(precio);

        return servicio;
    }

    private void limpiarFormulario() {
        nombreField.setText("");
        descripcionField.setText("");
        precioField.setText("");

        servicioSeleccionadoId = null;
        serviciosTabla.clearSelection();
        nombreField.requestFocusInWindow();
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



