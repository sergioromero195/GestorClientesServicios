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


    public boolean actualizar(Cliente datosNuevos) {
        if (datosNuevos.getId() == null) {
            throw new IllegalArgumentException(
                    "El cliente debe tener un ID para actualizarse."
            );
        }

        EntityManagerFactory fabrica =
                JpaUtil.getEntityManagerFactory();

        EntityManager em =
                fabrica.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Cliente existente =
                    em.find(Cliente.class, datosNuevos.getId());

            if (existente == null) {
                tx.rollback();
                return false;
            }

            existente.setNombre(datosNuevos.getNombre());
            existente.setEmail(datosNuevos.getEmail());
            existente.setTelefono(datosNuevos.getTelefono());

            tx.commit();
            return true;
        } catch (RuntimeException error) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw error;
        } finally {
            em.close();
        }
    }


    public boolean eliminar(Long id) {
        EntityManagerFactory fabrica =
                JpaUtil.getEntityManagerFactory();
        EntityManager em = fabrica.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Cliente cliente = em.find(Cliente.class, id);

            if (cliente == null) {
                tx.rollback();
                return false;
            }

            em.remove(cliente);
            tx.commit();
            return true;
        } catch (RuntimeException error) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw error;
        } finally {
            em.close();
        }
    }
}