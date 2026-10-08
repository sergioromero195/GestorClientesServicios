package com.reservas.app.config;


import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class JpaUtil {

    private static final String UNIDAD_PERSISTENCIA = "GestorPU";
    private static EntityManagerFactory fabrica;


    private JpaUtil(){
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (fabrica == null || !fabrica.isOpen()) {
            String usuario = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");

            if (usuario == null || usuario.isBlank()
                    || password == null || password.isBlank()) {
                throw new IllegalStateException(
                        "Faltan las variables de entorno DB_USER o DB_PASSWORD."
                );
            }

            Map<String, Object> propiedades = new HashMap<>();
            propiedades.put("jakarta.persistence.jdbc.user", usuario);
            propiedades.put("jakarta.persistence.jdbc.password", password);

            fabrica = Persistence.createEntityManagerFactory(
                    UNIDAD_PERSISTENCIA, propiedades
            );
        }

        return fabrica;
    }

    public static void close() {
        if (fabrica != null && fabrica.isOpen()) {
            fabrica.close();
        }
    }
}
