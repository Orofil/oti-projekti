package com.example.otiprojekti;

import java.math.BigDecimal;
import java.util.Objects;

public class Mokki {
    private int mokkiID;
    private int alueID;
    private int postiNro;
    private String mokkiNimi;
    private String katuosoite;
    private BigDecimal hinta;
    private String kuvaus;
    private int hloMaara;
    private String varustelu;

    //Alustaja tiedoille
    public Mokki(int mokkiID,int alueID, int postiNro,String mokkiNimi, String katuosoite, BigDecimal hinta, String kuvaus,
                 int hloMaara, String varustelu){
        this.mokkiID=mokkiID;
        this.alueID=alueID;
        this.postiNro=postiNro;
        this.mokkiNimi=mokkiNimi;
        this.katuosoite=katuosoite;
        this.hinta=hinta;
        this.kuvaus=kuvaus;
        this.hloMaara=hloMaara;
        this.varustelu=varustelu;
    }


    //Palauttaa mökin ID:n
    public int getMokkiID() {
        return mokkiID;
    }

    //Palauttaa alueen ID:n
    public int getAlueID() {
        return alueID;
    }

    //Luodaan getterit ja setterit muille kentille
    public int getPostiNro() {
        return postiNro;
    }


    public void setPostiNro(int postiNro) {
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
        String str= "Mökin ID on: " + mokkiID + "\nAlueen ID: "+ alueID+ "\nPostinro: "+ postiNro+"\nMökin nimi: "+
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
                alueID == mokki.alueID &&
                postiNro == mokki.postiNro &&
                hloMaara == mokki.hloMaara &&
                Objects.equals(mokkiNimi, mokki.mokkiNimi) &&
                Objects.equals(katuosoite, mokki.katuosoite) &&
                Objects.equals(hinta, mokki.hinta) &&
                Objects.equals(kuvaus, mokki.kuvaus) &&
                Objects.equals(varustelu, mokki.varustelu);
    }
}
