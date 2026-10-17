package com.reservas.app.service;

import com.reservas.app.dao.ClienteDAO;
import com.reservas.app.model.Cliente;

import java.util.List;

public class ClienteService {


    private final ClienteDAO clienteDAO = new ClienteDAO();

    public List<Cliente> listarTodos(){

        return clienteDAO.listarTodos();

    }

    public Cliente buscarPorId(Long id){

        return clienteDAO.buscarPorId(id);

    }


    public void guardar(Cliente cliente) {
        clienteDAO.guardar(cliente);
    }

    public boolean actualizar(Cliente cliente) {
        return clienteDAO.actualizar(cliente);
    }

    public boolean eliminar(Long id) {
        return clienteDAO.eliminar(id);
    }

}
