package com.example.otiprojekti;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.otiprojekti.Utils.*;

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

    private Connection con;
    private PreparedStatement stm;

    /**
     * SQL:n käyttämä muotoilu DateTime-tietotyypeissä
     */
    public static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Tietokanta() {
        // Tarkistetaan löytyykö tarvittavaa luokkaa yhdistämiseen
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Tietokantaan yhdistämiseen tarvittavaa pakkausta ei löytynyt.\nVirhe: " + e);
        }

        // Yhdistetään tietokantaan
        try {
            con = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Tietokantaan yhdistäminen ei onnistunut.\nVirhe: " + e);
        }
    }



    ///// Tietojen syöttö tietokantaan
    // TODO kaikki palauttaa uusimman syötetyn rivin

    /**
     * Syöttää tietokantaan alueen.
     * @param nimi Tyyppiä varchar(40)
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
     * Syöttää tietokantaan asiakkaan ja palauttaa sen oliona.
     * @param postinro Tyyppiä char(5). Oltava taulussa posti.
     * @param etunimi Tyyppiä varchar(20)
     * @param sukunimi Tyyppiä varchar(40)
     * @param lahiosoite Tyyppiä varchar(40)
     * @param email Tyyppiä varchar(50)
     * @param puhelinnro Tyyppiä varchar(15)
     * @param postit Lista {@link Posti Posteista}
     * @return {@link Asiakas}
     */
    public Asiakas insertAsiakas(String postinro, String etunimi, String sukunimi,
                                     String lahiosoite, String email, String puhelinnro,
                                     ArrayList<Posti> postit) throws SQLException {
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
        return haeAsiakasUusi(postit);
    }

    /**
     * Syöttää tietokantaan laskun.
     * @param varaus_id Tyyppiä int. Oltava taulussa varaus.
     * @param summa Tyyppiä double(8,2)
     * @param alv Tyyppiä int
     * @param status Tyyppiä int
     */
    public void insertLasku(int varaus_id, BigDecimal summa, int alv, int status) throws SQLException {
        stm = con.prepareStatement(
                "INSERT INTO lasku(varaus_id,summa,alv,status)" +
                "VALUES (?,?,?,?)");
        stm.setInt(1, varaus_id);
        stm.setBigDecimal(2, summa);
        stm.setInt(3, alv);
        stm.setInt(4, status);
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Syöttää tietokantaan mökin.
     * @param alue_id Tyyppiä int. Oltava taulussa alue.
     * @param postinro Tyyppiä char(5). Oltava taulussa posti.
     * @param mokkinimi Tyyppiä varchar(45)
     * @param katuosoite Tyyppiä varchar(45)
     * @param hinta Tyyppiä double(8,2)
     * @param kuvaus Tyyppiä varchar(150)
     * @param henkilomaara Tyyppiä int
     * @param varustelu Tyyppiä varchar(100)
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
     * @param nimi Tyyppiä varchar(40)
     * @param tyyppi Tyyppiä int
     * @param kuvaus Tyyppiä varchar(255)
     * @param hinta Tyyppiä double(8,2)
     * @param alv Tyyppiä int
     */
    public void insertPalvelu(int alue_id, String nimi, int tyyppi, String kuvaus,
                              BigDecimal hinta, int alv) throws SQLException {
        stm = con.prepareStatement(
                "INSERT INTO palvelu(alue_id,nimi,tyyppi,kuvaus,hinta,alv)" +
                        "VALUES (?,?,?,?,?,?)");
        stm.setInt(1, alue_id);
        stm.setString(2, nimi);
        stm.setInt(3, tyyppi);
        stm.setString(4, kuvaus);
        stm.setBigDecimal(5, hinta);
        stm.setInt(6, alv);
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Syöttää tietokantaan postinumeron.
     * @param postinro Tyyppiä char(5)
     * @param toimipaikka Tyyppiä varchar(45)
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
     * @param lkm Tyyppiä int
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
     * Syöttää tietokantaan varauksen ja palauttaa sen oliona.
     * @param asiakas_id Tyyppiä int. Oltava taulussa asiakas.
     * @param mokki_id Tyyppiä int. Oltava taulussa mokki.
     * @param varattu_pvm Tyyppiä datetime (muotoa YYYY-MM-DD hh:mm:ss)
     * @param vahvistus_pvm Tyyppiä datetime (muotoa YYYY-MM-DD hh:mm:ss)
     * @param varattu_alkupvm Tyyppiä datetime (muotoa YYYY-MM-DD hh:mm:ss)
     * @param varattu_loppupvm Tyyppiä datetime (muotoa YYYY-MM-DD hh:mm:ss)
     * @param asiakkaat Lista {@link Asiakas Asiakkaista}
     * @param mokit Lista {@link Mokki Mökeistä}
     * @param palvelut Lista {@link Palvelu Palveluista}
     * @return {@link Varaus}
     */
    public Varaus insertVaraus(int asiakas_id, int mokki_id, HashMap<Palvelu, Integer> varauksenPalvelut, String varattu_pvm,
                                    String vahvistus_pvm, String varattu_alkupvm, String varattu_loppupvm,
                                    ArrayList<Asiakas> asiakkaat, ArrayList<Mokki> mokit, ArrayList<Palvelu> palvelut) throws SQLException {
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

        Varaus uusiVaraus = haeVarausUusi(asiakkaat, mokit, palvelut);

        for (Map.Entry<Palvelu, Integer> vp : varauksenPalvelut.entrySet()) {
            stm = con.prepareStatement(
                    "INSERT INTO varauksen_palvelut(varaus_id,palvelu_id,lkm) " +
                    "VALUES (?,?,?)");
            stm.setInt(1, uusiVaraus.getID());
            stm.setInt(2, vp.getKey().getID());
            stm.setInt(3, vp.getValue());
        }
        return uusiVaraus;
    }



    ///// Tietokannan tietojen muokkaamiset

    /**
     * Muokkaa aluetta tietokannassa.
     * @param alue_id Tyyppiä int. Oltava taulussa alue.
     * @param nimi Tyyppiä varchar(40)
     */
    public void muokkaaAlue(int alue_id, String nimi) throws SQLException {
        stm = con.prepareStatement(
                "UPDATE alue " +
                        "SET nimi = ? " +
                        "WHERE alue_id = ?");
        stm.setString(1, nimi);
        stm.setInt(2, alue_id);
        stm.executeUpdate();
        stm.close();
    }

    public void muokkaaAlue(Alue alue) throws SQLException {
        stm = con.prepareStatement(
                "UPDATE alue " +
                        "SET nimi = ? " +
                        "WHERE alue_id = ?");
        stm.setString(1, alue.getNimi());
        stm.setInt(2, alue.getID());
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Muokkaa asiakasta tietokannassa.
     * @param asiakas_id Tyyppiä int. Oltava taulussa asiakas.
     * @param postinro Tyyppiä char(5). Oltava taulussa posti.
     * @param sukunimi Tyyppiä varchar(40)
     * @param etunimi Tyyppiä varchar(20)
     * @param email Tyyppiä varchar(50)
     * @param lahiosoite Tyyppiä varchar(40)
     * @param puhelinnro Tyyppiä varchar(15)
     */
    public void muokkaaAsiakas(int asiakas_id, String postinro, String sukunimi, String etunimi,
                               String email, String lahiosoite, String puhelinnro) throws SQLException {
        stm = con.prepareStatement(
                "UPDATE asiakas " +
                        "SET postinro = ?," +
                        "sukunimi = ?," +
                        "etunimi = ?," +
                        "email = ?," +
                        "lahiosoite = ?," +
                        "puhelinnro = ? " +
                        "WHERE asiakas_id = ?");
        stm.setString(1, postinro);
        stm.setString(2, sukunimi);
        stm.setString(3, etunimi);
        stm.setString(4, email);
        stm.setString(5, lahiosoite);
        stm.setString(6, puhelinnro);
        stm.setInt(7, asiakas_id);
        stm.executeUpdate();
        stm.close();
    }

    public void muokkaaAsiakas(Asiakas asiakas) throws SQLException {
        stm = con.prepareStatement(
                "UPDATE asiakas " +
                        "SET postinro = ?," +
                        "sukunimi = ?," +
                        "etunimi = ?," +
                        "email = ?," +
                        "lahiosoite = ?," +
                        "puhelinnro = ? " +
                        "WHERE asiakas_id = ?");
        stm.setString(1, asiakas.getPostiNro().getPostiNro());
        stm.setString(2, asiakas.getSukunimi());
        stm.setString(3, asiakas.getEtunimi());
        stm.setString(4, asiakas.getEmail());
        stm.setString(5, asiakas.getLahiosoite());
        stm.setString(6, asiakas.getPuhelinNro());
        stm.setInt(7, asiakas.getID());
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Muokkaa laskua tietokannassa.
     * @param lasku_id Tyyppiä int. Oltava taulussa lasku.
     * @param varaus_id Tyyppiä int. Oltava taulussa varaus.
     * @param summa Tyyppiä double(8,2).
     * @param alv Tyyppiä int.
     * @param status Tyyppiä int.
     */
    public void muokkaaLasku(int lasku_id, int varaus_id, BigDecimal summa, int alv, int status) throws SQLException {
        stm = con.prepareStatement(
                "UPDATE lasku " +
                        "SET varaus_id = ?," +
                        "summa = ?," +
                        "alv = ?," +
                        "status = ? " +
                        "WHERE lasku_id = ?");
        stm.setInt(1, varaus_id);
        stm.setBigDecimal(2, summa);
        stm.setInt(3, alv);
        stm.setInt(4, status);
        stm.setInt(5, lasku_id);
        stm.executeUpdate();
        stm.close();
    }

    public void muokkaaLasku(Lasku lasku) throws SQLException {
        stm = con.prepareStatement(
                "UPDATE lasku " +
                        "SET varaus_id = ?" +
                        "summa = ?," +
                        "alv = ?," +
                        "status = ? " +
                        "WHERE lasku_id = ?");
        stm.setInt(1, lasku.getVaraus().getID());
        stm.setBigDecimal(2, lasku.getSumma());
        stm.setInt(3, lasku.getAlv());
        stm.setInt(4, lasku.getStatus().id);
        stm.setInt(5, lasku.getID());
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Muokkaa mökkiä tietokannassa.
     * @param mokki_id Tyyppiä int. Oltava taulussa mokki.
     * @param alue_id Tyyppiä int. Oltava taulussa alue.
     * @param postinro Tyyppiä char(5). Oltava taulussa posti.
     * @param mokkinimi Tyyppiä varchar(45)
     * @param katuosoite Tyyppiä varchar(45)
     * @param hinta Tyyppiä double(8,2)
     * @param kuvaus Tyyppiä varchar(150)
     * @param henkilomaara Tyyppiä int
     * @param varustelu Tyyppiä varchar(100)
     */
    public void muokkaaMokki(int mokki_id, int alue_id, String postinro, String mokkinimi, String katuosoite,
                             BigDecimal hinta, String kuvaus, int henkilomaara, String varustelu) throws SQLException {
        stm = con.prepareStatement(
                "UPDATE mokki " +
                        "SET alue_id = ?," +
                        "postinro = ?," +
                        "mokkinimi = ?," +
                        "katuosoite = ?," +
                        "hinta = ?," +
                        "kuvaus = ?," +
                        "henkilomaara = ?," +
                        "varustelu = ? " +
                        "WHERE mokki_id = ?");
        stm.setInt(1, alue_id);
        stm.setString(2, postinro);
        stm.setString(3, mokkinimi);
        stm.setString(4, katuosoite);
        stm.setBigDecimal(5, hinta);
        stm.setString(6, kuvaus);
        stm.setInt(7, henkilomaara);
        stm.setString(8, varustelu);
        stm.setInt(9, mokki_id);
        stm.executeUpdate();
        stm.close();
    }

    public void muokkaaMokki(Mokki mokki) throws SQLException {
        stm = con.prepareStatement(
                "UPDATE mokki " +
                        "SET alue_id = ?," +
                        "postinro = ?," +
                        "mokkinimi = ?," +
                        "katuosoite = ?," +
                        "hinta = ?," +
                        "kuvaus = ?," +
                        "henkilomaara = ?," +
                        "varustelu = ? " +
                        "WHERE mokki_id = ?");
        stm.setInt(1, mokki.getAlue().getID());
        stm.setString(2, mokki.getPostiNro().getPostiNro());
        stm.setString(3, mokki.getNimi());
        stm.setString(4, mokki.getKatuosoite());
        stm.setBigDecimal(5, mokki.getHinta());
        stm.setString(6, mokki.getKuvaus());
        stm.setInt(7, mokki.getHloMaara());
        stm.setString(8, mokki.getVarustelu());
        stm.setInt(9, mokki.getID());
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Muokkaa palvelua tietokannassa.
     * @param palvelu_id Tyyppiä int. Oltava taulussa palvelu.
     * @param alue_id Tyyppiä int. Oltava taulussa alue.
     * @param nimi Tyyppiä varchar(40)
     * @param tyyppi Tyyppiä int
     * @param kuvaus Tyyppiä varchar(255)
     * @param hinta Tyyppiä double(8,2)
     * @param alv Tyyppiä int
     */
    public void muokkaaPalvelu(int palvelu_id, int alue_id, String nimi, int tyyppi, String kuvaus,
                               BigDecimal hinta, int alv) throws SQLException {
        stm = con.prepareStatement(
                "UPDATE palvelu " +
                    "SET alue_id = ?," +
                        "nimi = ?," +
                        "tyyppi = ?," +
                        "kuvaus = ?," +
                        "hinta = ?," +
                        "alv = ? " +
                    "WHERE palvelu_id = ?");
        stm.setInt(1, alue_id);
        stm.setString(2, nimi);
        stm.setInt(3, tyyppi);
        stm.setString(4, kuvaus);
        stm.setBigDecimal(5, hinta);
        stm.setInt(6,alv);
        stm.setInt(7, palvelu_id);
        stm.executeUpdate();
        stm.close();
    }

    public void muokkaaPalvelu(Palvelu palvelu) throws SQLException {
        stm = con.prepareStatement(
                "UPDATE palvelu " +
                        "SET alue_id = ?," +
                        "nimi = ?," +
                        "tyyppi = ?," +
                        "kuvaus = ?," +
                        "hinta = ?," +
                        "alv = ? " +
                        "WHERE palvelu_id = ?");
        stm.setInt(1, palvelu.getAlue().getID());
        stm.setString(2, palvelu.getNimi());
        stm.setInt(3, palvelu.getTyyppi());
        stm.setString(4, palvelu.getKuvaus());
        stm.setBigDecimal(5, palvelu.getHinta());
        stm.setInt(6, palvelu.getAlv());
        stm.setInt(7, palvelu.getID());
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Muokkaa varausta tietokannassa.
     * @param varaus_id Tyyppiä int. Oltava taulussa varaus.
     * @param asiakas_id Tyyppiä int. Oltava taulussa asiakas.
     * @param mokki_id Tyyppiä int. Oltava taulussa mokki.
     * @param palvelut HashMap {@link Palvelu Palveluista} ja kokonaisluvuista.
     * @param varattu_pvm Tyyppiä datetime (muotoa YYYY-MM-DD hh:mm:ss)
     * @param vahvistus_pvm Tyyppiä datetime (muotoa YYYY-MM-DD hh:mm:ss)
     * @param varattu_alkupvm Tyyppiä datetime (muotoa YYYY-MM-DD hh:mm:ss)
     * @param varattu_loppupvm Tyyppiä datetime (muotoa YYYY-MM-DD hh:mm:ss)
     */
    public void muokkaaVaraus(int varaus_id, int asiakas_id, int mokki_id, HashMap<Palvelu, Integer> palvelut, String varattu_pvm,
                              String vahvistus_pvm, String varattu_alkupvm, String varattu_loppupvm) throws SQLException {
        stm = con.prepareStatement(
                "UPDATE varaus " +
                        "SET asiakas_id = ?," +
                        "mokki_id = ?," +
                        "varattu_pvm = ?," +
                        "vahvistus_pvm = ?," +
                        "varattu_alkupvm = ?," +
                        "varattu_loppupvm = ? " +
                        "WHERE varaus_id = ?");
        stm.setInt(1, asiakas_id);
        stm.setInt(2, mokki_id);
        stm.setString(3, varattu_pvm);
        stm.setString(4, vahvistus_pvm);
        stm.setString(5, varattu_alkupvm);
        stm.setString(6, varattu_loppupvm);
        stm.setInt(7, varaus_id);
        stm.executeUpdate();
        stm.close();

        // Muokataan varaukseen liittyvät palvelut
        for (Map.Entry<Palvelu, Integer> vp : palvelut.entrySet()) {
            stm = con.prepareStatement(
                    "UPDATE varauksen_palvelut " +
                            "SET lkm = ? " +
                            "WHERE varaus_id = ? " +
                            "AND palvelu_id = ?");
            stm.setInt(1, vp.getValue());
            stm.setInt(2, varaus_id);
            stm.setInt(3, vp.getKey().getID());
            stm.executeUpdate();
            stm.close();
        }
    }

    public void muokkaaVaraus(Varaus varaus) throws SQLException {
        stm = con.prepareStatement(
                "UPDATE varaus " +
                        "SET asiakas_id = ?," +
                        "mokki_id = ?," +
                        "varattu_pvm = ?," +
                        "vahvistus_pvm = ?," +
                        "varattu_alkupvm = ?," +
                        "varattu_loppupvm = ? " +
                        "WHERE varaus_id = ?");
        stm.setInt(1, varaus.getAsiakas().getID());
        stm.setInt(2, varaus.getMokki().getID());
        stm.setString(3, dateTimeFormat.format(varaus.getVarattuPvm()));
        stm.setString(4, dateTimeFormat.format(varaus.getVahvistusPvm()));
        stm.setString(5, dateTimeFormat.format(varaus.getVarausAlkuPvm()));
        stm.setString(6, dateTimeFormat.format(varaus.getVarausLoppuPvm()));
        stm.setInt(7, varaus.getID());
        stm.executeUpdate();
        stm.close();

        // Muokataan varaukseen liittyvät palvelut
        for (Map.Entry<Palvelu, Integer> vp : varaus.getPalvelut().entrySet()) {
            stm = con.prepareStatement(
                    "UPDATE varauksen_palvelut " +
                            "SET lkm = ? " +
                            "WHERE varaus_id = ? " +
                            "AND palvelu_id = ?");
            stm.setInt(1, vp.getValue());
            stm.setInt(2, varaus.getID());
            stm.setInt(3, vp.getKey().getID());
            stm.executeUpdate();
            stm.close();
        }
    }




    ///// Tietokannan tietojen poistamiset

    /**
     * Poistaa tietokannasta alueen.
     * @param alue_id Tyyppiä int. Oltava taulussa alue.
     */
    public void poistaAlue(int alue_id) throws SQLException {
        stm = con.prepareStatement(
                "DELETE FROM alue WHERE alue_id = ?");
        stm.setInt(1, alue_id);
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Poistaa tietokannasta asiakkaan.
     * @param asiakas_id Tyyppiä int. Oltava taulussa asiakas.
     */
    public void poistaAsiakas(int asiakas_id) throws SQLException {
        stm = con.prepareStatement(
                "DELETE FROM asiakas WHERE asiakas_id = ?");
        stm.setInt(1, asiakas_id);
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Poistaa tietokannasta laskun.
     * @param lasku_id Tyyppiä int. Oltava taulussa lasku.
     */
    public void poistaLasku(int lasku_id) throws SQLException {
        stm = con.prepareStatement(
                "DELETE FROM lasku WHERE lasku_id = ?");
        stm.setInt(1, lasku_id);
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Poistaa tietokannasta mökin.
     * @param mokki_id Tyyppiä int. Oltava taulussa mokki.
     */
    public void poistaMokki(int mokki_id) throws SQLException {
        stm = con.prepareStatement(
                "DELETE FROM mokki WHERE mokki_id = ?");
        stm.setInt(1, mokki_id);
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Poistaa tietokannasta palvelun.
     * @param palvelu_id Tyyppiä int. Oltava taulussa palvelu.
     */
    public void poistaPalvelu(int palvelu_id) throws SQLException {
        stm = con.prepareStatement(
                "DELETE FROM palvelu WHERE palvelu_id = ?");
        stm.setInt(1, palvelu_id);
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Poistaa tietokannasta postinumeron.
     * @param postinro Tyyppiä int. Oltava taulussa posti.
     */
    public void poistaPosti(int postinro) throws SQLException {
        stm = con.prepareStatement(
                "DELETE FROM posti WHERE postinro = ?");
        stm.setInt(1, postinro);
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Poistaa tietokannasta varaukseen liittyvän palvelun.
     * @param varaus_id Tyyppiä int. Oltava taulussa varaus.
     * @param palvelu_id Tyyppiä int. Oltava taulussa palvelu.
     */
    // TODO tehdäänkö tämä näin vähän oudosti erillään
    public void poistaVarauksenPalvelut(int varaus_id, int palvelu_id) throws SQLException {
        stm = con.prepareStatement(
                "DELETE FROM varauksen_palvelut WHERE varaus_id = ? AND palvelu_id = ?");
        stm.setInt(1, varaus_id);
        stm.setInt(2, palvelu_id);
        stm.executeUpdate();
        stm.close();
    }

    /**
     * Poistaa tietokannasta varauksen.
     * @param varaus_id Tyyppiä int. Oltava taulussa varaus.
     */
    public void poistaVaraus(int varaus_id) throws SQLException {
        stm = con.prepareStatement(
                "DELETE FROM varaus WHERE varaus_id = ?");
        stm.setInt(1, varaus_id);
        stm.executeUpdate();
        stm.close();
    }



    ///// Tietokannasta hakemiset
    // TODO uusimpien hakeminen

    /**
     * Hakee tietokannasta kaikki alueet.
     * @return Lista {@link Alue Alueista}
     */
    public ArrayList<Alue> haeAlue() throws SQLException {
        stm = con.prepareStatement("SELECT * FROM alue");
        ResultSet rs = stm.executeQuery();
        ArrayList<Alue> tulokset = alueLuokaksi(rs);
        stm.close();
        return tulokset;
    }

    /**
     * Hakee tietokannasta uusimman asiakkaan.
     * @return {@link Asiakas}
     */
    public Asiakas haeAsiakasUusi(ArrayList<Posti> postit) throws SQLException {
        stm = con.prepareStatement(
                "SELECT * FROM asiakas WHERE asiakas_id = (SELECT MAX(asiakas_id) FROM asiakas)");
        ResultSet rs = stm.executeQuery();
        ArrayList<Asiakas> tulokset = asiakasLuokaksi(rs, postit);
        stm.close();
        return tulokset.get(0);
    }

    /**
     * Hakee tietokannasta kaikki asiakkaat.
     * @param postit Lista {@link Posti Posteista}
     * @return Lista {@link Asiakas Asiakkaista}
     */
    public ArrayList<Asiakas> haeAsiakas(ArrayList<Posti> postit) throws SQLException {
        stm = con.prepareStatement("SELECT * FROM asiakas");
        ResultSet rs = stm.executeQuery();
        ArrayList<Asiakas> tulokset = asiakasLuokaksi(rs, postit);
        stm.close();
        return tulokset;
    }

    /**
     * Hakee tietokannasta kaikki laskut.
     * @param varaukset Lista {@link Varaus Varauksista}
     * @return Lista {@link Lasku Laskuista}
     */
    public ArrayList<Lasku> haeLasku(ArrayList<Varaus> varaukset) throws SQLException {
        stm = con.prepareStatement("SELECT * FROM lasku");
        ResultSet rs = stm.executeQuery();
        ArrayList<Lasku> tulokset = laskuLuokaksi(rs, varaukset);
        stm.close();
        return tulokset;
    }

    /**
     * Hakee tietokannasta kaikki mökit.
     * @param alueet Lista {@link Alue Alueista}
     * @param postit Lista {@link Posti Posteista}
     * @return Lista {@link Mokki Mokeista}
     */
    public ArrayList<Mokki> haeMokki(ArrayList<Alue> alueet, ArrayList<Posti> postit) throws SQLException {
        stm = con.prepareStatement("SELECT * FROM mokki");
        ResultSet rs = stm.executeQuery();
        ArrayList<Mokki> tulokset = mokkiLuokaksi(rs, alueet, postit);
        stm.close();
        return tulokset;
    }

    /**
     * Hakee tietokannasta kaikki palvelut.
     * @param alueet Lista {@link Alue Alueista}
     * @return Lista {@link Palvelu Palveluista}
     */
    public ArrayList<Palvelu> haePalvelu(ArrayList<Alue> alueet) throws SQLException {
        stm = con.prepareStatement("SELECT * FROM palvelu");
        ResultSet rs = stm.executeQuery();
        ArrayList<Palvelu> tulokset = palveluLuokaksi(rs, alueet);
        stm.close();
        return tulokset;
    }

    /**
     * Hakee tietokannasta kaikki postinumerot.
     * @return Lista {@link Posti Posteista}
     */
    public ArrayList<Posti> haePosti() throws SQLException {
        stm = con.prepareStatement("SELECT * FROM posti");
        ResultSet rs = stm.executeQuery();
        ArrayList<Posti> tulokset = postiLuokaksi(rs);
        stm.close();
        return tulokset;
    }

    public HashMap<Integer, HashMap<Palvelu, Integer>> haeVarauksenPalvelut(ArrayList<Palvelu> palvelut) throws SQLException {
        stm = con.prepareStatement("SELECT * FROM varauksen_palvelut");
        ResultSet rs = stm.executeQuery();
        HashMap<Integer, HashMap<Palvelu, Integer>> tulokset = varauksenPalvelutLuokaksi(rs, palvelut);
        stm.close();
        return tulokset;
    }

    public HashMap<Integer, HashMap<Palvelu, Integer>> haeTietynVarauksenPalvelut(ArrayList<Palvelu> palvelut, int varausID) throws SQLException {
        stm = con.prepareStatement("SELECT * FROM varauksen_palvelut " +
                "WHERE varaus_id = ?");
        stm.setInt(1, varausID);
        ResultSet rs = stm.executeQuery();
        HashMap<Integer, HashMap<Palvelu, Integer>> tulokset = varauksenPalvelutLuokaksi(rs, palvelut);
        stm.close();
        return tulokset;
    }

    /**
     * Hakee tietokannasta uusimman varauksen.
     * @param asiakkaat Lista {@link Asiakas Asiakkaista}
     * @param mokit Lista {@link Mokki Mökeistä}
     * @return {@link Varaus}
     */
    public Varaus haeVarausUusi(ArrayList<Asiakas> asiakkaat, ArrayList<Mokki> mokit, ArrayList<Palvelu> palvelut) throws SQLException {
        // Haetaan ensin varaukseen liittyvät palvelut
        HashMap<Integer, HashMap<Palvelu, Integer>> varauksenPalvelut = haeVarauksenPalvelut(palvelut);

        stm = con.prepareStatement(
                "SELECT * FROM varaus WHERE varaus_id = (SELECT MAX(varaus_id) FROM varaus)");
        ResultSet rs = stm.executeQuery();
        ArrayList<Varaus> tulokset = varausLuokaksi(rs, asiakkaat, mokit, varauksenPalvelut);
        stm.close();
        return tulokset.get(0);
    }

    /**
     * Hakee tietokannasta kaikki varaukset.
     * @param asiakkaat Lista {@link Asiakas Asiakkaista}
     * @param mokit Lista {@link Mokki Mökeistä}
     * @return Lista {@link Varaus Varauksista}
     */
    public ArrayList<Varaus> haeVaraus(ArrayList<Asiakas> asiakkaat, ArrayList<Mokki> mokit, ArrayList<Palvelu> palvelut) throws SQLException {
        // Haetaan ensin varaukseen liittyvät palvelut
        HashMap<Integer, HashMap<Palvelu, Integer>> varauksenPalvelut = haeVarauksenPalvelut(palvelut);

        stm = con.prepareStatement("SELECT * FROM varaus");
        ResultSet rs = stm.executeQuery();
        ArrayList<Varaus> tulokset = varausLuokaksi(rs, asiakkaat, mokit, varauksenPalvelut);
        stm.close();
        return tulokset;
    }



    ///// Muuttamiset tietokannan tiedoista olioihin

    private ArrayList<Alue> alueLuokaksi(ResultSet rs) throws SQLException {
        ArrayList<Alue> alueet = new ArrayList<>();
        while (rs.next()) {
            alueet.add(new Alue(
                    rs.getInt("alue_id"),
                    rs.getString("nimi")));
        }
        return alueet;
    }

    private ArrayList<Asiakas> asiakasLuokaksi(ResultSet rs, ArrayList<Posti> postit) throws SQLException {
        ArrayList<Asiakas> asiakkaat = new ArrayList<>();
        while (rs.next()) {
            asiakkaat.add(new Asiakas(
                    rs.getInt("asiakas_id"),
                    etsiPostiNro(postit, rs.getString("postinro")),
                    rs.getString("sukunimi"),
                    rs.getString("etunimi"),
                    rs.getString("email"),
                    rs.getString("lahiosoite"),
                    rs.getString("puhelinnro")));
        }
        return asiakkaat;
    }

    private ArrayList<Lasku> laskuLuokaksi(ResultSet rs, ArrayList<Varaus> varaukset) throws SQLException {
        ArrayList<Lasku> laskut = new ArrayList<>();
        while (rs.next()) {
            laskut.add(new Lasku(
                    rs.getInt("lasku_id"),
                    etsiVarausID(varaukset, rs.getInt("varaus_id")),
                    rs.getBigDecimal("summa"),
                    rs.getInt("alv"),
                    LaskuStatus.valueOf(rs.getInt("status"))));
        }
        return laskut;
    }

    private ArrayList<Mokki> mokkiLuokaksi(ResultSet rs, ArrayList<Alue> alueet, ArrayList<Posti> postit) throws SQLException {
        ArrayList<Mokki> mokit = new ArrayList<>();
        while (rs.next()) {
            mokit.add(new Mokki(
                    rs.getInt("mokki_id"),
                    etsiAlueID(alueet, rs.getInt("alue_id")),
                    etsiPostiNro(postit, rs.getString("postinro")),
                    rs.getString("mokkinimi"),
                    rs.getString("katuosoite"),
                    rs.getBigDecimal("hinta"),
                    rs.getString("kuvaus"),
                    rs.getInt("henkilomaara"),
                    rs.getString("varustelu")));
        }
        return mokit;
    }

    private ArrayList<Palvelu> palveluLuokaksi(ResultSet rs, ArrayList<Alue> alueet) throws SQLException {
        ArrayList<Palvelu> palvelut = new ArrayList<>();
        while (rs.next()) {
            palvelut.add(new Palvelu(
                    rs.getInt("palvelu_id"),
                    etsiAlueID(alueet, rs.getInt("alue_id")),
                    rs.getString("nimi"),
                    rs.getInt("tyyppi"),
                    rs.getString("kuvaus"),
                    rs.getBigDecimal("hinta"),
                    rs.getInt("alv")
            ));
        }
        return palvelut;
    }

    private ArrayList<Posti> postiLuokaksi(ResultSet rs) throws SQLException {
        ArrayList<Posti> postit = new ArrayList<>();
        while (rs.next()) {
            postit.add(new Posti(
                    rs.getString("postinro"),
                    rs.getString("toimipaikka")));
        }
        return postit;
    }

    private HashMap<Integer, HashMap<Palvelu, Integer>> varauksenPalvelutLuokaksi(ResultSet rs, ArrayList<Palvelu> palvelut) throws SQLException {
        HashMap<Integer, HashMap<Palvelu, Integer>> varauksenPalvelut = new HashMap<>();
        while (rs.next()) {
            varauksenPalvelut.putIfAbsent(rs.getInt("varaus_id"), new HashMap<>());
            varauksenPalvelut.get(rs.getInt("varaus_id")).put(
                    etsiPalveluID(palvelut, rs.getInt("palvelu_id")),
                    rs.getInt("lkm"));
        }
        return varauksenPalvelut;
    }

    private ArrayList<Varaus> varausLuokaksi(ResultSet rs, ArrayList<Asiakas> asiakkaat, ArrayList<Mokki> mokit,
                                             HashMap<Integer, HashMap<Palvelu, Integer>> varauksenPalvelut) throws SQLException {
        ArrayList<Varaus> varaukset = new ArrayList<>();
        while (rs.next()) {
            LocalDateTime vahvistusPvm = null; // TODO miten käsitellään tällaiset jotka voi olla null, tässä vähän monimutkainen ratkaisu, joku vastaava pitää lisätä muillekin jotka voi olla tyhjiä
            try {
                vahvistusPvm = LocalDateTime.parse(rs.getString("vahvistus_pvm"), dateTimeFormat);
            } catch (NullPointerException ignored) {}

            varaukset.add(new Varaus(
                    rs.getInt("varaus_id"),
                    etsiAsiakasID(asiakkaat, rs.getInt("asiakas_id")),
                    etsiMokkiID(mokit, rs.getInt("mokki_id")),
                    varauksenPalvelut.get(rs.getInt("varaus_id")),
                    LocalDateTime.parse(rs.getString("varattu_pvm"), dateTimeFormat),
                    vahvistusPvm,
                    LocalDateTime.parse(rs.getString("varattu_alkupvm"), dateTimeFormat),
                    LocalDateTime.parse(rs.getString("varattu_loppupvm"), dateTimeFormat)));
        }
        return varaukset;
    }
}
