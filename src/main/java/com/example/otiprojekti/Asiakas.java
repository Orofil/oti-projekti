package com.example.otiprojekti;

import java.util.Objects;

public class Asiakas {
    private final int ID;
    private Posti postiNro;

    private String sukunimi;
    private String etunimi;
    private String email;
    private String lahiosoite;
    private String puhelinNro;

    /**
     * @param ID
     * @param postiNro
     * @param sukunimi Maksimipituus 40
     * @param etunimi Maksimipituus 20
     * @param email Maksimipituus 50
     * @param lahiosoite Maksimipituus 40
     * @param puhelinNro Maksimipituus 15
     */
    public Asiakas(int ID, Posti postiNro, String sukunimi, String etunimi,
                   String email, String lahiosoite, String puhelinNro) {
        this.ID = ID;
        this.postiNro = postiNro;

        this.sukunimi = sukunimi;
        this.etunimi = etunimi;
        this.email = email;
        this.lahiosoite = lahiosoite;
        this.puhelinNro = puhelinNro;
    }

    public int getID() {
        return ID;
    }

    public Posti getPostiNro() {
        return postiNro;
    }

    public void setPostiNro(Posti postiNro) {
        this.postiNro = postiNro;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    /**
     * @param sukunimi Maksimipituus 40
     */
    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }

    public String getEtunimi() {
        return etunimi;
    }

    /**
     * @param etunimi Maksimipituus 20
     */
    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }

    /**
     * @param sukunimiEnsin Kirjoitetaanko sukunimi ensin vai ei
     * @return Asiakkaan etu- ja sukunimi
     */
    public String getNimi(boolean sukunimiEnsin) {
        return sukunimiEnsin ? sukunimi + " " + etunimi : etunimi + " " + sukunimi;
    }

    public String getEmail() {
        return email;
    }

    /**
     * @param email Maksimipituus 50
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getLahiosoite() {
        return lahiosoite;
    }

    /**
     * @param lahiosoite Maksimipituus 40
     */
    public void setLahiosoite(String lahiosoite) {
        this.lahiosoite = lahiosoite;
    }

    public String getPuhelinNro() {
        return puhelinNro;
    }

    /**
     * @param puhelinNro Maksimipituus 15
     */
    public void setPuhelinNro(String puhelinNro) {
        this.puhelinNro = puhelinNro;
    }

    @Override
    public String toString() {
        return "Asiakas:" +
                "\nasiakasID=" + ID +
                "\nsukunimi=" + sukunimi +
                "\netunimi=" + etunimi +
                "\nemail=" + email +
                "\nlahiosoite=" + lahiosoite +
                "\npostiNro=" + postiNro +
                "\npuhelinNro=" + puhelinNro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asiakas asiakas = (Asiakas) o;
        return ID == asiakas.ID &&
                Objects.equals(postiNro, asiakas.postiNro) &&
                Objects.equals(sukunimi, asiakas.sukunimi) &&
                Objects.equals(etunimi, asiakas.etunimi) &&
                Objects.equals(email, asiakas.email) &&
                Objects.equals(lahiosoite, asiakas.lahiosoite) &&
                Objects.equals(puhelinNro, asiakas.puhelinNro);
    }
}
