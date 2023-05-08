package com.example.otiprojekti;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Objects;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class Lasku {
    private final int ID;
    private Varaus varaus;
    private BigDecimal summa;
    private int alv;
    private LaskuStatus status;

    /**
     * @param ID
     * @param varaus
     * @param summa Desimaaleja enintään 2, pituus enintään 8
     * @param alv
     * @param status
     */
    public Lasku(int ID, Varaus varaus, BigDecimal summa, int alv, LaskuStatus status) {
        this.ID = ID;
        this.varaus = varaus;
        this.summa = summa;
        this.alv = alv;
        this.status = status;
    }

    public int getID() {
        return ID;
    }

    public Varaus getVaraus() {
        return varaus;
    }

    public void setVaraus(Varaus varaus) {
        this.varaus = varaus;
    }

    public BigDecimal getSumma() {
        return summa;
    }

    /**
     * @param summa Desimaaleja enintään 2, pituus enintään 8
     */
    public void setSumma(BigDecimal summa) {
        this.summa = summa;
    }

    public int getAlv() {
        return alv;
    }

    public void setAlv(int alv) {
        this.alv = alv;
    }

    public LaskuStatus getStatus() {
        return status;
    }

    public void setStatus(LaskuStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String str = "LaskuID: " + ID + "\n VarausID: " + varaus.getID() +
                "\n Laskun summa: " + summa + "\n Laskun alv: " + alv +
                "\n Laskun tila: " + status;
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lasku lasku = (Lasku) o;
        return getID() == lasku.getID() &&
                getVaraus().equals(lasku.getVaraus()) &&
                Objects.equals(getSumma(), lasku.getSumma()) &&
                Double.compare(lasku.getAlv(), getAlv()) == 0 &&
                Objects.equals(getStatus(), lasku.getStatus());
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
