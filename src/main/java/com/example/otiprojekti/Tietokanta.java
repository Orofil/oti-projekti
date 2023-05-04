package com.example.otiprojekti;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/*
Lähteet:
https://www.javatpoint.com/example-to-connect-to-the-mysql-database
https://www.tutorialspoint.com/jdbc/jdbc-create-database.htm
 */
public class Tietokanta {
    // API:tietokanta://palvelimen_nimi:portin_numero/tietokannan_nimi
    private final String DB_URL = "jdbc:mysql://localhost/vn";
    // Käyttäjänimi
    private final String USER = "kayttaja";
    // Salasana
    private final String PASS = "koira1";

    private final String POLKU = "src/main/resources/com/example/otiprojekti/";

    private Connection con;
    private PreparedStatement stm;

    /**
     * SQL:n käyttämä muotoilu DateTime-tietotyypeissä
     */
    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Tietokanta() {
        // Tarkistetaan löytyykö tarvittavaa luokkaa yhdistämiseen
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO ehkä joku parempi ilmoitus
            System.out.println("Tietokantaan yhdistämiseen tarvittavaa pakkausta ei löytynyt.\nVirhe: " + e);
        }

        try {
            con = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Tietokantaan yhdistäminen ei onnistunut.\nVirhe: " + e);
        }
    }



    ///// Tietojen syöttö tietokantaan

    /**
     * Syöttää tietokantaan alueen.
     * @param nimi Tyyppiä varchar(40).
     */
    public void insertAlue(String nimi) throws SQLException {
        stm = con.prepareStatement(
                "INSERT INTO alue(nimi) " +
                        "VALUES (?)");
        stm.setString(1, nimi);
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Syöttää tietokantaan asiakkaan.
     * @param postinro Tyyppiä char(5). Oltava taulussa posti.
     * @param etunimi Tyyppiä varchar(20).
     * @param sukunimi Tyyppiä varchar(40).
     * @param lahiosoite Tyyppiä varchar(40).
     * @param email Tyyppiä varchar(50).
     * @param puhelinnro Tyyppiä varchar(15).
     */
    public void insertAsiakas(String postinro, String etunimi, String sukunimi,
                                     String lahiosoite, String email, String puhelinnro) throws SQLException {
        stm = con.prepareStatement(
                "INSERT INTO asiakas(postinro,etunimi,sukunimi,lahiosoite,email,puhelinnro)" +
                "VALUES (?,?,?,?,?,?)");
        stm.setString(1, postinro);
        stm.setString(2, etunimi);
        stm.setString(3, sukunimi);
        stm.setString(4, lahiosoite);
        stm.setString(5, email);
        stm.setString(6, puhelinnro);
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Syöttää tietokantaan laskun.
     * @param varaus_id Tyyppiä int. Oltava taulussa varaus.
     * @param summa Tyyppiä double(8,2).
     * @param alv Tyyppiä int.
     */
    public void insertLasku(int varaus_id, BigDecimal summa, int alv) throws SQLException {
        stm = con.prepareStatement(
                "INSERT INTO lasku(varaus_id,summa,alv)" +
                "VALUES (?,?,?)");
        stm.setInt(1, varaus_id);
        stm.setBigDecimal(2, summa);
        stm.setInt(3, alv);
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Syöttää tietokantaan mökin.
     * @param alue_id Tyyppiä int. Oltava taulussa alue.
     * @param postinro Tyyppiä char(5). Oltava taulussa posti.
     * @param mokkinimi Tyyppiä varchar(45).
     * @param katuosoite Tyyppiä varchar(45).
     * @param hinta Tyyppiä double(8,2).
     * @param kuvaus Tyyppiä varchar(150).
     * @param henkilomaara Tyyppiä int.
     * @param varustelu Tyyppiä varchar(100).
     */
    public void insertMokki(int alue_id, String postinro, String mokkinimi, String katuosoite,
                                   BigDecimal hinta, String kuvaus, int henkilomaara, String varustelu) throws SQLException {
        stm = con.prepareStatement(
                "INSERT INTO mokki(alue_id,postinro,mokkinimi,katuosoite,hinta,kuvaus,henkilomaara,varustelu)" +
                "VALUES (?,?,?,?,?,?,?,?)");
        stm.setInt(1, alue_id);
        stm.setString(2, postinro);
        stm.setString(3, mokkinimi);
        stm.setString(4, katuosoite);
        stm.setBigDecimal(5, hinta);
        stm.setString(6, kuvaus);
        stm.setInt(7, henkilomaara);
        stm.setString(8, varustelu);
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Syöttää tietokantaan palvelun.
     * @param alue_id Tyyppiä int. Oltava taulussa alue.
     * @param nimi Tyyppiä varchar(40).
     * @param tyyppi Tyyppiä int.
     * @param kuvaus Tyyppiä varchar(255).
     * @param hinta Tyyppiä double(8,2).
     * @param alv Tyyppiä int.
     */
    public void insertPalvelu(int alue_id, String nimi, int tyyppi, String kuvaus,
                              BigDecimal hinta, BigDecimal alv) throws SQLException {
        stm = con.prepareStatement(
                "INSERT INTO palvelu(palvelu_id,alue_id,nimi,tyyppi,kuvaus,hinta,alv)" +
                        "VALUES (?,?,?,?,?,?,?)");
        stm.setInt(1, alue_id);
        stm.setString(2, nimi);
        stm.setInt(3, tyyppi);
        stm.setString(4, kuvaus);
        stm.setBigDecimal(5, hinta);
        stm.setBigDecimal(6, alv);
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Syöttää tietokantaan postinumeron.
     * @param postinro Tyyppiä char(5).
     * @param toimipaikka Tyyppiä varchar(45).
     */
    public void insertPosti(String postinro, String toimipaikka) throws SQLException {
        stm = con.prepareStatement(
                "INSERT INTO posti(postinro, toimipaikka) " +
                        "VALUES (?,?)");
        stm.setString(1, postinro);
        stm.setString(2, toimipaikka);
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Syöttää tietokantaan varaukseen liittyvän palvelun.
     * @param varaus_id Tyyppiä int. OLtava taulussa varaus.
     * @param palvelu_id Tyyppiä int. Oltava taulussa palvelu.
     * @param lkm Tyyppiä int.
     */
    // TODO tehdäänkö tämä näin vähän oudosti erillään
    public void insertVarauksenPalvelut(int varaus_id, int palvelu_id, int lkm) throws SQLException {
        stm = con.prepareStatement(
                "INSERT INTO varauksen_palvelut(varaus_id,palvelu_id,lkm)" +
                "(?,?,?)");
        stm.setInt(1, varaus_id);
        stm.setInt(2, palvelu_id);
        stm.setInt(3, lkm);
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Syöttää tietokantaan varauksen.
     * @param asiakas_id Tyyppiä int. Oltava taulussa asiakas.
     * @param mokki_id Tyyppiä int. Oltava taulussa mokki.
     * @param varattu_pvm Tyyppiä datetime (muotoa YYYY-MM-DD hh:mm:ss).
     * @param vahvistus_pvm Tyyppiä datetime (muotoa YYYY-MM-DD hh:mm:ss).
     * @param varattu_alkupvm Tyyppiä datetime (muotoa YYYY-MM-DD hh:mm:ss).
     * @param varattu_loppupvm Tyyppiä datetime (muotoa YYYY-MM-DD hh:mm:ss).
     */
    public void insertVaraus(int asiakas_id, int mokki_id, String varattu_pvm,
                                    String vahvistus_pvm, String varattu_alkupvm, String varattu_loppupvm) throws SQLException {
        stm = con.prepareStatement(
                "INSERT INTO varaus(asiakas_id,mokki_id,varattu_pvm,vahvistus_pvm,varattu_alkupvm,varattu_loppupvm) " +
                "VALUES (?,?,?,?,?,?)");
        stm.setInt(1, asiakas_id);
        stm.setInt(2, mokki_id);
        stm.setString(3, varattu_pvm);
        stm.setString(4, vahvistus_pvm);
        stm.setString(5, varattu_alkupvm);
        stm.setString(6, varattu_loppupvm);
        stm.executeUpdate();
        stm.close();
    }



    ///// Tietokannasta hakemiset
    // TODO vaikka mitä tarvittavia hakuja

    /**
     * Hakee tietokannasta kaikki mökit.
     * @return Lista {@link Asiakas Asiakkaista}
     */
    public ArrayList<Asiakas> haeAsiakas() throws SQLException {
        stm = con.prepareStatement("SELECT * FROM asiakas");
        ResultSet rs = stm.executeQuery();
        ArrayList<Asiakas> tulokset = asiakasLuokaksi(rs);
        stm.close();
        return tulokset;
    }

    /**
     * Hakee tietokannasta kaikki mökit.
     * @return Lista {@link Mokki Mokeista}
     */
    public ArrayList<Mokki> haeMokki() throws SQLException {
        stm = con.prepareStatement("SELECT * FROM mokki");
        ResultSet rs = stm.executeQuery();
        ArrayList<Mokki> tulokset = mokkiLuokaksi(rs);
        stm.close();
        return tulokset;
    }

    /**
     * Hakee tietokannasta kaikki palvelut
     * @return Lista {@link Palvelu Palveluista}
     */
    public ArrayList<Palvelu> haePalvelu() throws SQLException {
        stm = con.prepareStatement("SELECT * FROM palvelu");
        ResultSet rs = stm.executeQuery();
        ArrayList<Palvelu> tulokset = palveluLuokaksi(rs);
        stm.close();
        return tulokset;
    }

    /**
     * Hakee tietokannasta kaikki varaukset.
     * @return Lista {@link Varaus Varauksista}
     */
    public ArrayList<Varaus> haeVaraus() throws SQLException {
        stm = con.prepareStatement("SELECT * FROM varaus");
        ResultSet rs = stm.executeQuery();
        ArrayList<Varaus> tulokset = varausLuokaksi(rs);
        stm.close();
        return tulokset;
    }



    ///// Muuttamiset tietokannan tiedoista olioihin
    // TODO alue, lasku (postia ei ilmeisesti tehdä erillisenä olioluokkana, mutta en tiedä miksi sitten esim. alue tehdään)

    /**
     * Muuttaa tietokannasta saadun ResultSetin {@link Asiakas Asiakas}-olioiksi.
     * @param rs Tietokannasta saatu ResultSet asiakkaita
     * @return Lista asiakkaista
     */
    private ArrayList<Asiakas> asiakasLuokaksi(ResultSet rs) throws SQLException {
        ArrayList<Asiakas> asiakkaat = new ArrayList<>();
        while (rs.next()) {
            asiakkaat.add(new Asiakas(
                    rs.getInt("asiakas_id"),
                    rs.getString("postinro"),
                    rs.getString("etunimi"),
                    rs.getString("sukunimi"),
                    rs.getString("lahiosoite"),
                    rs.getString("email"),
                    rs.getString("puhelinnro")));
        }
        return asiakkaat;
    }

    private ArrayList<Mokki> mokkiLuokaksi(ResultSet rs) throws SQLException {
        ArrayList<Mokki> mokit = new ArrayList<>();
        while (rs.next()) {
            mokit.add(new Mokki(
                    rs.getInt("mokki_id"),
                    rs.getInt("alue_id"),
                    rs.getString("postinro"), // TODO toimiiko getInt vai pitääkö käyttää Integer.valueOf koska tietokannassa postinro on tyyppiä char
                    rs.getString("mokkinimi"),
                    rs.getString("katuosoite"),
                    rs.getBigDecimal("hinta"),
                    rs.getString("kuvaus"),
                    rs.getInt("henkilomaara"),
                    rs.getString("varustelu")));
        }
        return mokit;
    }

    /**
     * Muuttaa tietokannasta saadun ResultSetin {@link Palvelu Palvelu}-olioiksi.
     * @param rs Tietokannasta saatu ResultSet palveluja
     * @return Lista palveluista
     */
    private ArrayList<Palvelu> palveluLuokaksi(ResultSet rs) throws SQLException {
        ArrayList<Palvelu> palvelut = new ArrayList<>();
        while (rs.next()) {
            palvelut.add(new Palvelu(
                    rs.getInt("palvelu_id"),
                    rs.getInt("alue_id"),
                    rs.getString("nimi"),
                    rs.getString("tyyppi"),
                    rs.getString("kuvaus"),
                    rs.getBigDecimal("hinta"),
                    rs.getInt("alv")
            ));
        }
        return palvelut;
    }

    /**
     * Muuttaa tietokannasta saadun ResultSetin {@link Varaus Varaus}-olioiksi.
     * @param rs Tietokannasta saatu ResultSet varauksia
     * @return Lista varauksista
     */
    private ArrayList<Varaus> varausLuokaksi(ResultSet rs) throws SQLException {
        ArrayList<Varaus> varaukset = new ArrayList<>();
        while (rs.next()) {
            LocalDateTime vahvistusPvm = null; // TODO miten käsitellään tällaiset jotka voi olla null, tässä vähän monimutkainen ratkaisu
            try {
                vahvistusPvm = LocalDateTime.parse(rs.getString("vahvistus_pvm"), dateTimeFormat);
            } catch (NullPointerException ignored) {}

            varaukset.add(new Varaus(
                    rs.getInt("varaus_id"),
                    rs.getInt("asiakas_id"),
                    rs.getInt("mokki_id"),
                    LocalDateTime.parse(rs.getString("varattu_pvm"), dateTimeFormat),
                    vahvistusPvm,
                    LocalDateTime.parse(rs.getString("varattu_alkupvm"), dateTimeFormat),
                    LocalDateTime.parse(rs.getString("varattu_loppupvm"), dateTimeFormat)));
        }
        return varaukset;
    }
}
