package com.example.otiprojekti;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

/*
Lähteet:
https://www.javatpoint.com/example-to-connect-to-the-mysql-database
https://www.tutorialspoint.com/jdbc/jdbc-create-database.htm
 */
public class SQL {
    // API:tietokanta://palvelimen_nimi:portin_numero/tietokannan_nimi
    private static final String DB_URL = "jdbc:mysql://localhost/";
    // Käyttäjänimi
    private static final String USER = "root";
    // Salasana
    private static final String PASS = ""; // TODO salasana pitää lisätä

    private static final String POLKU = "src/main/resources/com/example/otiprojekti/";

    public static void main(String[] args) {
        /*
        En tiedä tarvitaanko tätä, tämä kai vain tarkistaa löytyykö
        yhdistämiseen tarvittavaa luokkaa.
         */
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Yhdistetään MySQL:ään
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stm = con.createStatement()) {
            // Tietokannan luonti
            String vnLuonti = Files.readString(Path.of(POLKU, "vn.sql"));
            stm.executeUpdate(vnLuonti); // TODO ei toimi, tulee syntaksivirheitä
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
