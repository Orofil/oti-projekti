package com.example.otiprojekti;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
     * @return {@link Varaus}
     */
    public Varaus insertVaraus(int asiakas_id, int mokki_id, String varattu_pvm,
                                    String vahvistus_pvm, String varattu_alkupvm, String varattu_loppupvm,
                                    ArrayList<Asiakas> asiakkaat, ArrayList<Mokki> mokit) throws SQLException {
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
        return haeVarausUusi(asiakkaat, mokit);
    }



    ///// Tietokannan tietojen muokkaamiset
    // TODO muokkausmetodit
    // TODO tehdäänkö muokkausmetodit niin päin, että ensin muokataan ohjelmaan oliota ja sitten olio syötetään tänne metodille josta sen tiedot tietokantaan

    /**
     * Muokkaa varausta tietokannassa.
     * @param varaus_id Tyyppiä int. Oltava taulussa varaus.
     * @param asiakas_id Tyyppiä int. Oltava taulussa asiakas.
     * @param mokki_id Tyyppiä int. Oltava taulussa mokki.
     * @param varattu_pvm Tyyppiä datetime (muotoa YYYY-MM-DD hh:mm:ss)
     * @param vahvistus_pvm Tyyppiä datetime (muotoa YYYY-MM-DD hh:mm:ss)
     * @param varattu_alkupvm Tyyppiä datetime (muotoa YYYY-MM-DD hh:mm:ss)
     * @param varattu_loppupvm Tyyppiä datetime (muotoa YYYY-MM-DD hh:mm:ss)
     */
    public void muokkaaVaraus(int varaus_id, int asiakas_id, int mokki_id, String varattu_pvm,
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
                "UPDATE mökki " +
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

    /**
     * Hakee tietokannasta uusimman varauksen.
     * @param asiakkaat Lista {@link Asiakas Asiakkaista}
     * @param mokit Lista {@link Mokki Mökeistä}
     * @return {@link Varaus}
     */
    public Varaus haeVarausUusi(ArrayList<Asiakas> asiakkaat, ArrayList<Mokki> mokit) throws SQLException {
        stm = con.prepareStatement(
                "SELECT * FROM varaus WHERE varaus_id = (SELECT MAX(varaus_id) FROM varaus)");
        ResultSet rs = stm.executeQuery();
        ArrayList<Varaus> tulokset = varausLuokaksi(rs, asiakkaat, mokit);
        stm.close();
        return tulokset.get(0);
    }

    /**
     * Hakee tietokannasta kaikki varaukset.
     * @param asiakkaat Lista {@link Asiakas Asiakkaista}
     * @param mokit Lista {@link Mokki Mökeistä}
     * @return Lista {@link Varaus Varauksista}
     */
    public ArrayList<Varaus> haeVaraus(ArrayList<Asiakas> asiakkaat, ArrayList<Mokki> mokit) throws SQLException {
        stm = con.prepareStatement("SELECT * FROM varaus");
        ResultSet rs = stm.executeQuery();
        ArrayList<Varaus> tulokset = varausLuokaksi(rs, asiakkaat, mokit);
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
                    rs.getString("lahiosoite"),
                    rs.getString("email"),
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
                    rs.getString("laskunStatus"))); // TODO laskun statusta ei ole tietokannassa
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
                    rs.getString("tyyppi"),
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

    private ArrayList<Varaus> varausLuokaksi(ResultSet rs, ArrayList<Asiakas> asiakkaat, ArrayList<Mokki> mokit) throws SQLException {
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
                    LocalDateTime.parse(rs.getString("varattu_pvm"), dateTimeFormat),
                    vahvistusPvm,
                    LocalDateTime.parse(rs.getString("varattu_alkupvm"), dateTimeFormat),
                    LocalDateTime.parse(rs.getString("varattu_loppupvm"), dateTimeFormat)));
        }
        return varaukset;
    }
}
