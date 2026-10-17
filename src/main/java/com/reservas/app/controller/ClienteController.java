package com.reservas.app.controller;

import com.reservas.app.model.Cliente;
import com.reservas.app.service.ClienteService;

import java.util.List;

public class ClienteController {

    private final ClienteService clienteService = new ClienteService();

    public List<Cliente> listarClientes(){

        return clienteService.listarTodos();

    }

    public Cliente buscarCliente(Long id){

        return clienteService.buscarPorId(id);

    }

    public void guardarCliente(Cliente cliente) {
        clienteService.guardar(cliente);
    }

    public boolean actualizarCliente(Cliente cliente) {
        return clienteService.actualizar(cliente);
    }

    public boolean eliminarCliente(Long id) {
        return clienteService.eliminar(id);
    }

}
