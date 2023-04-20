package com.example.otiprojekti;

import java.util.Objects;

public class Lasku {
    private int laskuID;
    private int varausID;
    private double laskunSumma;
    private double laskuAlv;
    private String laskunStatus;

    //alustaja
    public Lasku(int laskuID, int varausID, double laskunSumma, double laskuAlv, String laskunStatus) {}

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lasku lasku = (Lasku) o;
        return getLaskuID() == lasku.getLaskuID() &&
                getVarausID() == lasku.getVarausID() &&
                Double.compare(lasku.getLaskunSumma(), getLaskunSumma()) == 0 &&
                Double.compare(lasku.getLaskuAlv(), getLaskuAlv()) == 0 &&
                Objects.equals(getLaskunStatus(), lasku.getLaskunStatus());
    }


    public void vieDokumentiksi() {}
}
