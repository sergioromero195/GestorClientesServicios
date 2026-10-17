package com.reservas.app.controller;

import com.reservas.app.model.Servicio;
import com.reservas.app.service.ServicioService;

import java.util.List;

public class ServicioController {

    private final ServicioService servicioService = new ServicioService();

    public List<Servicio> listarServicios() {
        return servicioService.listarTodos();
    }

    public Servicio buscarServicio(Long id) {
        return servicioService.buscarPorId(id);
    }

    public void guardarServicio(Servicio servicio) {
        servicioService.guardar(servicio);
    }

    public boolean actualizarServicio(Servicio servicio) {
        return servicioService.actualizar(servicio);
    }

    public boolean eliminarServicio(Long id) {
        return servicioService.eliminar(id);
    }
}