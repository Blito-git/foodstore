package com.foodstore.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionDB {

    public static Connection obtenerConexion() {
        Properties props = new Properties();
        try (InputStream is = ConexionDB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) throw new RuntimeException("No se encontró db.properties en el classpath de recursos.");
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Error fatal al procesar db.properties: " + e.getMessage(), e);
        }

        String url      = props.getProperty("db.url");
        String usuario  = props.getProperty("db.usuario");
        String password = props.getProperty("db.password");

        try {
            return DriverManager.getConnection(url, usuario, password);
        } catch (SQLException e) {
            throw new RuntimeException("Error en DriverManager. No se pudo establecer enlace: " + e.getMessage(), e);
        }
    }
}