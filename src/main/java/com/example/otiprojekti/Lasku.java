package com.example.otiprojekti;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;
import java.io.FileWriter;

public class Lasku {
    private int laskuID;
    private int varausID;
    private BigDecimal laskunSumma;
    private int laskuAlv;
    private String laskunStatus;

    //alustaja
    public Lasku(int laskuID, int varausID, BigDecimal laskunSumma, int laskuAlv, String laskunStatus) {
        this.laskuID = laskuID;
        this.varausID = varausID;
        this.laskunSumma = laskunSumma;
        this.laskuAlv = laskuAlv;
        this.laskunStatus = laskunStatus;
    }

    public int getLaskuID() {
        return laskuID;
    }

    public int getVarausID() {
        return varausID;
    }

    public void setVarausID(int varausID) {
        this.varausID = varausID;
    }

    public BigDecimal getLaskunSumma() {
        return laskunSumma;
    }

    public void setLaskunSumma(BigDecimal laskunSumma) {
        this.laskunSumma = laskunSumma;
    }

    public int getLaskuAlv() {
        return laskuAlv;
    }

    public void setLaskuAlv(int laskuAlv) {
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
        String str = "LaskuID on " + laskuID + "\n VarausID on " + varausID +
                "\n Laskun summa on " + laskunSumma + "\n Laskun alv on " + laskuAlv +
                "\n Laskun tila on " + laskunStatus;
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lasku lasku = (Lasku) o;
        return getLaskuID() == lasku.getLaskuID() &&
                getVarausID() == lasku.getVarausID() &&
                Objects.equals(getLaskunSumma(), lasku.getLaskunSumma()) &&
                Double.compare(lasku.getLaskuAlv(), getLaskuAlv()) == 0 &&
                Objects.equals(getLaskunStatus(), lasku.getLaskunStatus());
    }


    public void vieDokumentiksi() { // TODO
        try {
            FileWriter writer = new FileWriter("tiedostonimi.txt");
            writer.write("Laskun tunnus: " + getLaskuID() + "\n");
            writer.write("Varauksen tunnus: " + getVarausID() + "\n");
            writer.write("Laskun summa: " + getLaskunSumma() + "\n");
            writer.write("Laskun alv: " + getLaskuAlv() + "\n");
            writer.write("Laskun tila: " + getLaskunStatus() + "\n");
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Laskutietojen tallennuksessa tapahtui virhe.");
        }
    }
}
