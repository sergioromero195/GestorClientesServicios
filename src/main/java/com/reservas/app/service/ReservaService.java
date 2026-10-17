package com.reservas.app.service;

import com.reservas.app.dao.ReservaDAO;
import com.reservas.app.model.Reserva;

import java.util.List;

public class ReservaService {

    private final ReservaDAO reservaDAO = new ReservaDAO();

    public List<Reserva> listarTodos() {
        return reservaDAO.listarTodos();
    }

    public Reserva buscarPorId(Long id) {
        return reservaDAO.buscarPorId(id);
    }

    public void guardar(Reserva reserva) {
        reservaDAO.guardar(reserva);
    }

    public boolean actualizar(Reserva reserva) {
        return reservaDAO.actualizar(reserva);
    }

    public boolean eliminar(Long id) {
        return reservaDAO.eliminar(id);
    }
}