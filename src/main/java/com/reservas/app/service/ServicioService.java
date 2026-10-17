package com.reservas.app.service;

import com.reservas.app.dao.ServicioDAO;
import com.reservas.app.model.Servicio;

import java.util.List;

public class ServicioService {

    private final ServicioDAO servicioDAO = new ServicioDAO();

    public List<Servicio> listarTodos() {
        return servicioDAO.listarTodos();
    }

    public Servicio buscarPorId(Long id) {
        return servicioDAO.buscarPorId(id);
    }

    public void guardar(Servicio servicio) {
        servicioDAO.guardar(servicio);
    }

    public boolean actualizar(Servicio servicio) {
        return servicioDAO.actualizar(servicio);
    }

    public boolean eliminar(Long id) {
        return servicioDAO.eliminar(id);
    }
}