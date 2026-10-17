package com.reservas.app.controller;

import com.reservas.app.model.Reserva;
import com.reservas.app.service.ReservaService;

import java.util.List;

public class ReservaController {

    private final ReservaService reservaService = new ReservaService();

    public List<Reserva> listarReservas() {
        return reservaService.listarTodos();
    }

    public Reserva buscarReserva(Long id) {
        return reservaService.buscarPorId(id);
    }

    public void guardarReserva(Reserva reserva) {
        reservaService.guardar(reserva);
    }

    public boolean actualizarReserva(Reserva reserva) {
        return reservaService.actualizar(reserva);
    }

    public boolean eliminarReserva(Long id) {
        return reservaService.eliminar(id);
    }
}