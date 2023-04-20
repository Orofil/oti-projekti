package com.example.otiprojekti;

public class Lasku {
    private int laskuID;
    private int varausID;
    private double laskunSumma;
    private double laskuAlv;
    private String laskunStatus;

    //alustaja
    public Lasku() {}

    public int getLaskuID() {
        return laskuID;
    }

    public void setLaskuID(int laskuID) {
        this.laskuID = laskuID;
    }

    public int getVarausID() {
        return varausID;
    }

    public void setVarausID(int varausID) {
        this.varausID = varausID;
    }

    public double getLaskunSumma() {
        return laskunSumma;
    }

    public void setLaskunSumma(double laskunSumma) {
        this.laskunSumma = laskunSumma;
    }

    public double getLaskuAlv() {
        return laskuAlv;
    }

    public void setLaskuAlv(double laskuAlv) {
        this.laskuAlv = laskuAlv;
    }

    public String getLaskunStatus() {
        return laskunStatus;
    }

    public void setLaskunStatus(String laskunStatus) {
        this.laskunStatus = laskunStatus;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void vieDokumentiksi() {}
}
