package com.reservas.app.dao;

import com.reservas.app.config.JpaUtil;
import com.reservas.app.model.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ClienteDAO {

    public List<Cliente> listarTodos() {
        EntityManagerFactory fabrica =
                JpaUtil.getEntityManagerFactory();

        EntityManager em =
                fabrica.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT c FROM Cliente c",
                    Cliente.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public Cliente buscarPorId(Long id) {
        EntityManagerFactory fabrica =
                JpaUtil.getEntityManagerFactory();

        EntityManager em =
                fabrica.createEntityManager();

        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }
}