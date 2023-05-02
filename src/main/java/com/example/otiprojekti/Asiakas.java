package com.example.otiprojekti;

import java.util.Objects;

public class Asiakas {
    private int asiakasID;
    private String postinro;

    private String sukunimi;
    private String etunimi;
    private String email;
    private String lahiosoite;
    private String puhnro;

    public Asiakas(int asiakasID,
                   String postinro,
                   String sukunimi,
                   String etunimi,
                   String email,
                   String lahiosoite,
                   String puhnro) {
        this.asiakasID = asiakasID;
        this.postinro = postinro;

        this.sukunimi = sukunimi;
        this.etunimi = etunimi;
        this.email = email;
        this.lahiosoite = lahiosoite;
        this.puhnro = puhnro;
    }

    public int getAsiakasID() {
        return asiakasID;
    }

    public String getPostinro() {
        return postinro;
    }

    public void setPostinro(String postinro) {
        this.postinro = postinro;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }

    public String getEtunimi() {
        return etunimi;
    }

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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLahiosoite() {
        return lahiosoite;
    }

    public void setLahiosoite(String lahiosoite) {
        this.lahiosoite = lahiosoite;
    }

    public String getPuhnro() {
        return puhnro;
    }

    public void setPuhnro(String puhnro) {
        this.puhnro = puhnro;
    }

    @Override
    public String toString() {
        return "Asiakas[" +
                "asiakasID=" + asiakasID +
                ", sukunimi=" + sukunimi +
                ", etunimi=" + etunimi +
                ", email=" + email +
                ", lahiosoite=" + lahiosoite +
                ", postinro=" + postinro +
                ", puhnro=" + puhnro +
                "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asiakas asiakas = (Asiakas) o;
        return asiakasID == asiakas.asiakasID &&
                postinro == asiakas.postinro &&
                Objects.equals(sukunimi, asiakas.sukunimi) &&
                Objects.equals(etunimi, asiakas.etunimi) &&
                Objects.equals(email, asiakas.email) &&
                Objects.equals(lahiosoite, asiakas.lahiosoite) &&
                Objects.equals(puhnro, asiakas.puhnro);
    }
}
