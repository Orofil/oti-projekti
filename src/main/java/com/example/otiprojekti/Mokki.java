package com.example.otiprojekti;

import java.math.BigDecimal;
import java.util.Objects;

public class Mokki {
    private final int ID;
    private Alue alue;
    private Posti postiNro;
    private String nimi;
    private String katuosoite;
    private BigDecimal hinta;
    private String kuvaus;
    private int hloMaara;
    private String varustelu;

    /**
     * @param ID
     * @param alue
     * @param postiNro
     * @param nimi Maksimipituus 45
     * @param katuosoite Maksimipituus 45
     * @param hinta Desimaaleja enintään 2, pituus enintään 8
     * @param kuvaus Maksimipituus 150
     * @param hloMaara
     * @param varustelu Maksimipituus 100
     */
    public Mokki(int ID, Alue alue, Posti postiNro, String nimi, String katuosoite,
                 BigDecimal hinta, String kuvaus, int hloMaara, String varustelu) {
        this.ID = ID;
        this.alue = alue;
        this.postiNro = postiNro;
        this.nimi = nimi;
        this.katuosoite = katuosoite;
        this.hinta = hinta;
        this.kuvaus = kuvaus;
        this.hloMaara = hloMaara;
        this.varustelu = varustelu;
    }

    public int getID() {
        return ID;
    }

    public Alue getAlue() {
        return alue;
    }

    public Posti getPostiNro() {
        return postiNro;
    }

    public void setPostiNro(Posti postiNro) {
        this.postiNro = postiNro;
    }

    public String getNimi() {
        return nimi;
    }

    /**
     * @param nimi Maksimipituus 45
     */
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getKatuosoite() {
        return katuosoite;
    }

    /**
     * @param katuosoite Maksimipituus 45
     */
    public void setKatuosoite(String katuosoite) {
        this.katuosoite = katuosoite;
    }

    public BigDecimal getHinta() {
        return hinta;
    }

    /**
     * @param hinta Desimaaleja enintään 2, pituus enintään 8
     */
    public void setHinta(BigDecimal hinta) {
        this.hinta = hinta;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    /**
     * @param kuvaus Maksimipituus 150
     */
    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public int getHloMaara() {
        return hloMaara;
    }

    public void setHloMaara(int hloMaara) {
        this.hloMaara = hloMaara;
    }

    public String getVarustelu() {
        return varustelu;
    }

    /**
     * @param varustelu Maksimipituus 100
     */
    public void setVarustelu(String varustelu) {
        this.varustelu = varustelu;
    }

    @Override
    public String toString() {
        String str= "Mökin ID on: " + ID + "\nAlueen ID: "+ alue + "\nPostinro: "+ postiNro+"\nMökin nimi: "+
                nimi +"\nKatuosoite: "+ katuosoite+ "\nHinta: "+ hinta + "\nKuvaus: "+ kuvaus+ "\nHenkilömäärä: "+
                hloMaara+"\nVarustelu: "+ varustelu;
        return str;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mokki mokki = (Mokki) o;
        return ID == mokki.ID &&
                alue == mokki.alue &&
                hloMaara == mokki.hloMaara &&
                Objects.equals(postiNro, mokki.postiNro) &&
                Objects.equals(nimi, mokki.nimi) &&
                Objects.equals(katuosoite, mokki.katuosoite) &&
                Objects.equals(hinta, mokki.hinta) &&
                Objects.equals(kuvaus, mokki.kuvaus) &&
                Objects.equals(varustelu, mokki.varustelu);
    }
}
