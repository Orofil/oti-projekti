package com.example.otiprojekti;

import java.math.BigDecimal;
import java.util.Objects;

public class Palvelu {
    private int palveluID;
    private int alueID;
    private String palvelunNimi;
    private String palvelunTyyppi;
    private String palvelunKuvaus;
    private BigDecimal palvelunHinta;
    private int palvelunAlv;

    //Alustaja perustiedoille
    public Palvelu(int palveluID, int alueID, String palvelunNimi, String palvelunTyyppi, String palvelunKuvaus,
                   BigDecimal palvelunHinta, int palvelunAlv){
        this.palveluID=palveluID;
        this.alueID=alueID;
        this.palvelunNimi=palvelunNimi;
        this.palvelunTyyppi=palvelunTyyppi;
        this.palvelunKuvaus=palvelunKuvaus;
        this.palvelunHinta=palvelunHinta;
        this.palvelunAlv=palvelunAlv;
    }


    // Getterit palveluille ja alueille

    public int getPalveluID() {
        return palveluID;
    }

    public int getAlueID() {
        return alueID;
    }
    // Luodaan getterit ja setterit muille kentille

    public String getPalvelunNimi() {
        return palvelunNimi;
    }

    public void setPalvelunNimi(String palvelunNimi) {
        this.palvelunNimi = palvelunNimi;
    }

    public String getPalvelunTyyppi() {
        return palvelunTyyppi;
    }

    public void setPalvelunTyyppi(String palvelunTyyppi) {
        this.palvelunTyyppi = palvelunTyyppi;
    }

    public String getPalvelunKuvaus() {
        return palvelunKuvaus;
    }

    public void setPalvelunKuvaus(String palvelunKuvaus) {
        this.palvelunKuvaus = palvelunKuvaus;
    }

    public BigDecimal getPalvelunHinta() {
        return palvelunHinta;
    }

    public void setPalvelunHinta(BigDecimal palvelunHinta) {
        this.palvelunHinta = palvelunHinta;
    }

    public int getPalvelunAlv() {
        return palvelunAlv;
    }

    public void setPalvelunAlv(int palvelunAlv) {
        this.palvelunAlv = palvelunAlv;
    }

    @Override
    public String toString(){
        String str= "Palvelun ID on: " + palveluID + "\nAlueen ID: "+ alueID+ "\nPalvelun nimi: "+ palvelunNimi+
                "\nPalvelun tyyppi: "+ palvelunTyyppi+"\nPalvelun kuvaus: "+ palvelunKuvaus+ "\nPalvelun hinta: "
                + palvelunHinta + "\nPalvelun Alv: "+ palvelunAlv;
        return str;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Palvelu palvelu = (Palvelu) o;
        return palveluID == palvelu.palveluID &&
                alueID == palvelu.alueID &&
                Objects.equals(palvelunHinta, palvelu.palvelunHinta) &&
                Double.compare(palvelu.palvelunAlv, palvelunAlv) == 0 &&
                Objects.equals(palvelunNimi, palvelu.palvelunNimi) &&
                Objects.equals(palvelunTyyppi, palvelu.palvelunTyyppi) &&
                Objects.equals(palvelunKuvaus, palvelu.palvelunKuvaus);
    }
}
