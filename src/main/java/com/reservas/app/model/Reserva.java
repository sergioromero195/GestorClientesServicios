package com.reservas.app.model;

import jakarta.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDate fechaReserva;

    @Column(nullable = false)
    private String estado;


    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;


    @ManyToOne
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;



    public Reserva() {}


    public Reserva(LocalDate fechaReserva, String estado, Cliente cliente, Servicio servicio) {
        this.fechaReserva = fechaReserva;
        this.estado = estado;
        this.cliente = cliente;
        this.servicio = servicio;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDate fechaReserva) { this.fechaReserva = fechaReserva; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
}