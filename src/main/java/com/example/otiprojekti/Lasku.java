package com.example.otiprojekti;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Objects;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class Lasku {
    private final int laskuID;
    private Varaus varaus;
    private BigDecimal laskunSumma;
    private int laskuAlv;
    private String laskunStatus;

    //alustaja
    public Lasku(int laskuID, Varaus varaus, BigDecimal laskunSumma, int laskuAlv, String laskunStatus) {
        this.laskuID = laskuID;
        this.varaus = varaus;
        this.laskunSumma = laskunSumma;
        this.laskuAlv = laskuAlv;
        this.laskunStatus = laskunStatus;
    }

    public int getLaskuID() {
        return laskuID;
    }

    public Varaus getVaraus() {
        return varaus;
    }

    public void setVaraus(Varaus varaus) {
        this.varaus = varaus;
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
        String str = "LaskuID: " + laskuID + "\n VarausID: " + varaus.getVarausID() +
                "\n Laskun summa: " + laskunSumma + "\n Laskun alv: " + laskuAlv +
                "\n Laskun tila: " + laskunStatus;
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lasku lasku = (Lasku) o;
        return getLaskuID() == lasku.getLaskuID() &&
                getVaraus().equals(lasku.getVaraus()) &&
                Objects.equals(getLaskunSumma(), lasku.getLaskunSumma()) &&
                Double.compare(lasku.getLaskuAlv(), getLaskuAlv()) == 0 &&
                Objects.equals(getLaskunStatus(), lasku.getLaskunStatus());
    }


    public void vieDokumentiksi() { // TODO
        Document lasku = new Document();
        try {
            PdfWriter.getInstance(lasku, new FileOutputStream("lasku.pdf"));
            lasku.open();
            lasku.add(new Paragraph(toString()));
            lasku.close();
        }
        catch (Exception e) {
            System.out.println("Laskutietojen tallennuksessa tapahtui virhe.");
        }
    }
    public void maksuMuistutus() {
        Document muistutus = new Document();
        try {
            PdfWriter.getInstance(muistutus, new FileOutputStream("muistutus.pdf"));
            muistutus.open();
            muistutus.add(new Paragraph("Tämä on maksumuistutus mökin vuokrasta. Maksathan laskun pikimmiten"));
            muistutus.add(new Paragraph(toString()));
            muistutus.close();
        }
        catch (Exception e) {
            System.out.println("Laskutietojen tallennuksessa tapahtui virhe.");
        }
    }
}
