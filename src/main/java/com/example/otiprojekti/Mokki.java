package com.example.otiprojekti;

import java.math.BigDecimal;
import java.util.Objects;

public class Mokki {
    private final int mokkiID;
    private Alue alue;
    private String postiNro;
    private String mokkiNimi;
    private String katuosoite;
    private BigDecimal hinta;
    private String kuvaus;
    private int hloMaara;
    private String varustelu;

    public Mokki(int mokkiID, Alue alue, String postiNro, String mokkiNimi, String katuosoite,
                 BigDecimal hinta, String kuvaus, int hloMaara, String varustelu){
        this.mokkiID = mokkiID;
        this.alue = alue;
        this.postiNro = postiNro;
        this.mokkiNimi = mokkiNimi;
        this.katuosoite = katuosoite;
        this.hinta = hinta;
        this.kuvaus = kuvaus;
        this.hloMaara = hloMaara;
        this.varustelu = varustelu;
    }

    public int getMokkiID() {
        return mokkiID;
    }

    public Alue getAlue() {
        return alue;
    }

    public String getPostiNro() {
        return postiNro;
    }

    public void setPostiNro(String postiNro) {
        this.postiNro = postiNro;
    }

    public String getMokkiNimi() {
        return mokkiNimi;
    }

    public void setMokkiNimi(String mokkiNimi) {
        this.mokkiNimi = mokkiNimi;
    }

    public String getKatuosoite() {
        return katuosoite;
    }

    public void setKatuosoite(String katuosoite) {
        this.katuosoite = katuosoite;
    }

    public BigDecimal getHinta() {
        return hinta;
    }

    public void setHinta(BigDecimal hinta) {
        this.hinta = hinta;
    }

    public String getKuvaus() {
        return kuvaus;
    }

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

    public void setVarustelu(String varustelu) {
        this.varustelu = varustelu;
    }

    @Override
    public String toString() {
        String str= "Mökin ID on: " + mokkiID + "\nAlueen ID: "+ alue + "\nPostinro: "+ postiNro+"\nMökin nimi: "+
                mokkiNimi+"\nKatuosoite: "+ katuosoite+ "\nHinta: "+ hinta + "\nKuvaus: "+ kuvaus+ "\nHenkilömäärä: "+
                hloMaara+"\nVarustelu: "+ varustelu;
        return str;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mokki mokki = (Mokki) o;
        return mokkiID == mokki.mokkiID &&
                alue == mokki.alue &&
                hloMaara == mokki.hloMaara &&
                Objects.equals(postiNro, mokki.postiNro) &&
                Objects.equals(mokkiNimi, mokki.mokkiNimi) &&
                Objects.equals(katuosoite, mokki.katuosoite) &&
                Objects.equals(hinta, mokki.hinta) &&
                Objects.equals(kuvaus, mokki.kuvaus) &&
                Objects.equals(varustelu, mokki.varustelu);
    }
}
