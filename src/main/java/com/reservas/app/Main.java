package com.reservas.app;

import com.reservas.app.config.JpaUtil;
import com.reservas.app.dao.ClienteDAO;
import com.reservas.app.model.Cliente;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        try {
            JpaUtil.getEntityManagerFactory();
            System.out.println("La fábrica de JPA se ha inicializado.");

            ClienteDAO clienteDAO = new ClienteDAO();
            List<Cliente> clientes = clienteDAO.listarTodos();

            System.out.println("Clientes encontrados: " + clientes.size());

            for (Cliente cliente : clientes) {
                System.out.println(cliente);
            }

            Cliente clienteBuscado = clienteDAO.buscarPorId(1L);

            if (clienteBuscado != null) {
                System.out.println("Cliente encontrado: " + clienteBuscado);
            } else {
                System.out.println("No existe un cliente con ese ID.");
            }

        } catch (RuntimeException error) {
            System.err.println("No se pudo iniciar JPA.");
            error.printStackTrace();
        } finally {
            JpaUtil.close();
        }


    }


}