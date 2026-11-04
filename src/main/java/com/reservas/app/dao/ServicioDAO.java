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

        validarId(id);

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

        validarDatosServicio(servicio);

        if(servicio.getId() != null){

            throw  new IllegalArgumentException("Un servicio nuevo no debe tener un ID asignado.");

        }


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

        validarDatosServicio(datosNuevos);

        validarId(datosNuevos.getId());

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

        validarId(id);

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

            Long cantidadReservas = em.createQuery(
                            "SELECT COUNT(r) FROM Reserva r WHERE r.servicio.id = :id",
                            Long.class
                    )
                    .setParameter("id", id)
                    .getSingleResult();

            if (cantidadReservas > 0) {
                throw new IllegalStateException(
                        "No se puede eliminar el servicio porque tiene reservas asociadas."
                );
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

    private void validarDatosServicio(Servicio servicio) {

        if (servicio == null) {
            throw new IllegalArgumentException(
                    "El servicio no puede ser null."
            );
        }

        if (servicio.getNombre() == null
                || servicio.getNombre().isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del servicio es obligatorio."
            );
        }

        // Limpiar espacios
        servicio.setNombre(servicio.getNombre().trim());

        if (servicio.getDescripcion() != null) {
            servicio.setDescripcion(
                    servicio.getDescripcion().trim()
            );
        }

        if (servicio.getNombre().length() > 255) {
            throw new IllegalArgumentException(
                    "El nombre del servicio no puede superar los 255 caracteres."
            );
        }

        if (servicio.getPrecio() == null) {
            throw new IllegalArgumentException(
                    "El precio del servicio es obligatorio."
            );
        }

        if (servicio.getPrecio().signum() < 0) {
            throw new IllegalArgumentException(
                    "El precio no puede ser negativo."
            );
        }

        if (servicio.getPrecio()
                .stripTrailingZeros()
                .scale() > 2) {
            throw new IllegalArgumentException(
                    "El precio solo puede tener dos decimales."
            );
        }
    }

    private void validarId(Long id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "El ID del servicio no puede ser null."
            );
        }
    }
}