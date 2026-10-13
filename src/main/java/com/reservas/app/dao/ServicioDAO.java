package com.reservas.app.dao;

import com.reservas.app.config.JpaUtil;
import com.reservas.app.model.Servicio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class ServicioDAO {

    public List<Servicio> listarTodos() {
        EntityManagerFactory fabrica =
                JpaUtil.getEntityManagerFactory();
        EntityManager em = fabrica.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT s FROM Servicio s",
                    Servicio.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public Servicio buscarPorId(Long id) {
        EntityManagerFactory fabrica =
                JpaUtil.getEntityManagerFactory();
        EntityManager em = fabrica.createEntityManager();

        try {
            return em.find(Servicio.class, id);
        } finally {
            em.close();
        }
    }

    public void guardar(Servicio servicio) {
        EntityManagerFactory fabrica =
                JpaUtil.getEntityManagerFactory();
        EntityManager em = fabrica.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(servicio);
            tx.commit();
        } catch (RuntimeException error) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw error;
        } finally {
            em.close();
        }
    }

    public boolean actualizar(Servicio datosNuevos) {
        if (datosNuevos.getId() == null) {
            throw new IllegalArgumentException(
                    "El servicio debe tener un ID para actualizarse."
            );
        }

        EntityManagerFactory fabrica =
                JpaUtil.getEntityManagerFactory();
        EntityManager em = fabrica.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Servicio existente =
                    em.find(Servicio.class, datosNuevos.getId());

            if (existente == null) {
                tx.rollback();
                return false;
            }

            existente.setNombre(datosNuevos.getNombre());
            existente.setDescripcion(datosNuevos.getDescripcion());
            existente.setPrecio(datosNuevos.getPrecio());

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

            Servicio servicio = em.find(Servicio.class, id);

            if (servicio == null) {
                tx.rollback();
                return false;
            }

            em.remove(servicio);
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