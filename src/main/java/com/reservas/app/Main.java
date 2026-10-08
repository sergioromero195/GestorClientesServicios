package com.reservas.app;

import com.reservas.app.config.JpaUtil;

public class Main {
    public static void main(String[] args) {

        try {
            JpaUtil.getEntityManagerFactory();
            System.out.println("La fábrica de JPA se ha inicializado.");
        } catch (RuntimeException error) {
            System.err.println("No se pudo iniciar JPA.");
            error.printStackTrace();
        } finally {
            JpaUtil.close();
        }


    }


}