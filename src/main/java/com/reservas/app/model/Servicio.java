package com.reservas.app.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;


    @OneToMany(mappedBy = "servicio")
    private List<Reserva> reservas = new ArrayList<>();


    public Servicio() {}


    public Servicio(String nombre, String descripcion, BigDecimal  precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal  getPrecio() { return precio; }
    public void setPrecio(BigDecimal  precio) { this.precio = precio; }

    public List<Reserva> getReservas() { return reservas; }
    public void setReservas(List<Reserva> reservas) { this.reservas = reservas; }
}