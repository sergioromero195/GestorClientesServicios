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

        validarId(id);


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

        validarDatosCliente(cliente);

        if (cliente.getId() != null) {
            throw new IllegalArgumentException(
                    "Un cliente nuevo no debe tener un ID asignado."
            );
        }



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

        validarDatosCliente(datosNuevos);

       validarId(datosNuevos.getId());

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

        validarId(id);

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

    private void validarDatosCliente(Cliente cliente){

        if(cliente == null){

            throw new IllegalArgumentException("El cliente no puede ser null");

        }

        if (cliente.getNombre() == null || cliente.getNombre().isBlank()){

            throw new IllegalArgumentException("El nombre es obligatorio.");

        }

        if(cliente.getEmail() == null || cliente.getEmail().isBlank()){


            throw  new IllegalArgumentException("El email es obligatorio.");
        }


    }

    private void validarId(Long id){

        if(id == null){

            throw new IllegalArgumentException("El ID del cliente no puede ser null.");

        }



    }


}