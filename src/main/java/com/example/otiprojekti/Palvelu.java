package com.example.otiprojekti;

import java.math.BigDecimal;
import java.util.Objects;

public class Palvelu {
    private final int ID;
    private Alue alue;
    private String nimi;
    private int tyyppi;
    private String kuvaus;
    private BigDecimal hinta;
    private int alv;

    /**
     * @param ID
     * @param alue
     * @param nimi Maksimipituus 40
     * @param tyyppi
     * @param kuvaus Maksimipituus 255
     * @param hinta Desimaaleja enintään 2, pituus enintään 8
     * @param alv
     */
    public Palvelu(int ID, Alue alue, String nimi, int tyyppi, String kuvaus,
                   BigDecimal hinta, int alv){
        this.ID = ID;
        this.alue = alue;
        this.nimi = nimi;
        this.tyyppi = tyyppi;
        this.kuvaus = kuvaus;
        this.hinta = hinta;
        this.alv = alv;
    }

    public int getID() {
        return ID;
    }

    public Alue getAlue() {
        return alue;
    }
    // Luodaan getterit ja setterit muille kentille

    public String getNimi() {
        return nimi;
    }

    /**
     * @param nimi Maksimipituus 40
     */
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public int getTyyppi() {
        return tyyppi;
    }

    public void setTyyppi(int tyyppi) {
        this.tyyppi = tyyppi;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    /**
     * @param kuvaus Maksimipituus 255
     */
    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
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

    public int getAlv() {
        return alv;
    }

    public void setAlv(int alv) {
        this.alv = alv;
    }

    @Override
    public String toString(){
        String str= "Palvelun ID on: " + ID + "\nAlueen ID: "+ alue + "\nPalvelun nimi: "+ nimi +
                "\nPalvelun tyyppi: "+ tyyppi +"\nPalvelun kuvaus: "+ kuvaus + "\nPalvelun hinta: "
                + hinta + "\nPalvelun Alv: "+ alv;
        return str;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Palvelu palvelu = (Palvelu) o;
        return ID == palvelu.ID &&
                alue == palvelu.alue &&
                Objects.equals(hinta, palvelu.hinta) &&
                Double.compare(palvelu.alv, alv) == 0 &&
                Objects.equals(nimi, palvelu.nimi) &&
                Objects.equals(tyyppi, palvelu.tyyppi) &&
                Objects.equals(kuvaus, palvelu.kuvaus);
    }
}
