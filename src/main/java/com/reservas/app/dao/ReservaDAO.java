package com.reservas.app.dao;

import com.reservas.app.config.JpaUtil;
import com.reservas.app.model.Cliente;
import com.reservas.app.model.Reserva;
import com.reservas.app.model.Servicio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class ReservaDAO {

    public List<Reserva> listarTodos() {
        EntityManagerFactory fabrica =
                JpaUtil.getEntityManagerFactory();
        EntityManager em = fabrica.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT r FROM Reserva r",
                    Reserva.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public Reserva buscarPorId(Long id) {
        EntityManagerFactory fabrica =
                JpaUtil.getEntityManagerFactory();
        EntityManager em = fabrica.createEntityManager();

        try {
            return em.find(Reserva.class, id);
        } finally {
            em.close();
        }
    }

    public void guardar(Reserva reserva) {
        validarDatosReserva(reserva);

        if (reserva.getId() != null) {
            throw new IllegalArgumentException("Una reserva nueva no debe tener un ID asignado.");
        }
        EntityManagerFactory fabrica =
                JpaUtil.getEntityManagerFactory();
        EntityManager em = fabrica.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Sustituimos las referencias por entidades gestionadas por este EntityManager.
            Cliente cliente = em.find(Cliente.class, reserva.getCliente().getId());
            Servicio servicio = em.find(Servicio.class, reserva.getServicio().getId());

            if (cliente == null || servicio == null) {
                throw new IllegalArgumentException(
                        "El cliente y el servicio deben existir en la base de datos."
                );
            }

            reserva.setCliente(cliente);
            reserva.setServicio(servicio);

            em.persist(reserva);
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

    public boolean actualizar(Reserva datosNuevos) {
        if (datosNuevos == null) {
            throw new IllegalArgumentException("La reserva no puede ser null.");
        }

        if (datosNuevos.getId() == null) {
            throw new IllegalArgumentException(
                    "La reserva debe tener un ID para actualizarse."
            );
        }
        validarDatosReserva(datosNuevos);

        EntityManagerFactory fabrica =
                JpaUtil.getEntityManagerFactory();
        EntityManager em = fabrica.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Reserva existente = em.find(Reserva.class, datosNuevos.getId());

            if (existente == null) {
                tx.rollback();
                return false;
            }

            Cliente cliente = em.find(
                    Cliente.class,
                    datosNuevos.getCliente().getId()
            );
            Servicio servicio = em.find(
                    Servicio.class,
                    datosNuevos.getServicio().getId()
            );

            if (cliente == null || servicio == null) {
                throw new IllegalArgumentException(
                        "El cliente y el servicio deben existir en la base de datos."
                );
            }

            existente.setFechaReserva(datosNuevos.getFechaReserva());
            existente.setEstado(datosNuevos.getEstado());
            existente.setCliente(cliente);
            existente.setServicio(servicio);

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

            Reserva reserva = em.find(Reserva.class, id);

            if (reserva == null) {
                tx.rollback();
                return false;
            }

            em.remove(reserva);
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

    private void validarDatosReserva(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no puede ser null.");
        }
        if (reserva.getFechaReserva() == null) {
            throw new IllegalArgumentException("La fecha de la reserva es obligatoria.");
        }
        if (reserva.getEstado() == null || reserva.getEstado().isBlank()) {
            throw new IllegalArgumentException("El estado de la reserva es obligatorio.");
        }
        if (reserva.getCliente() == null || reserva.getCliente().getId() == null) {
            throw new IllegalArgumentException("La reserva debe tener un cliente con ID.");
        }
        if (reserva.getServicio() == null || reserva.getServicio().getId() == null) {
            throw new IllegalArgumentException("La reserva debe tener un servicio con ID.");
        }
    }
}

