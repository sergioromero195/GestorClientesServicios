package com.reservas.app.dao;

import com.reservas.app.config.JpaUtil;
import com.reservas.app.model.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

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

    public void guardar(Cliente cliente){

        EntityManagerFactory fabrica = JpaUtil.getEntityManagerFactory();

       EntityManager em = fabrica.createEntityManager();

      EntityTransaction tx = em.getTransaction();

      try{

          tx.begin();

          em.persist(cliente);

          tx.commit();


      }catch(RuntimeException error){

          if(tx.isActive()){
              tx.rollback();

          }
          throw error;

      }finally {
          em.close();
      }


    }
}