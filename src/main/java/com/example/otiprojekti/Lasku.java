package com.example.otiprojekti;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

    public BigDecimal getVarausSumma() {
        int daysBetween = (int) Duration.between(
                getVaraus().getVarausAlkuPvm(), getVaraus().getVarausLoppuPvm()).toDays();

        BigDecimal varaustenHinta = getVarausPalveluSumma();

        return getVaraus().getMokki().getHinta().multiply(BigDecimal.valueOf(daysBetween)).
                add(varaustenHinta.multiply(BigDecimal.valueOf((getAlv() / 100d) + 1))); // TODO onko tämä oikein
    }

    public BigDecimal getVarausPalveluSumma() {
        BigDecimal varaustenHinta = BigDecimal.ZERO;
        for (Map.Entry<Palvelu, Integer> vp : getVaraus().getPalvelut().entrySet()) {
            varaustenHinta = varaustenHinta.add(vp.getKey().getHinta().multiply(
                    BigDecimal.valueOf(((vp.getKey().getAlv() / 100d) + 1)))); // TODO onko tämä oikein
        }
        return varaustenHinta;
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
        Stage laskuIkkuna = new Stage();
        GridPane laskunTiedot = new GridPane();
        laskunTiedot.setVgap(5);
        TextField saajanNimi = new TextField();
        TextField tiliNumero = new TextField();
        TextField eraPaiva = new TextField();
        TextField viiteNumero = new TextField();
        Label saaja = new Label("Saajan nimi");
        Label tilinro = new Label("Saajan tilinumero");
        Label erapv = new Label("Laskun eräpäivä");
        Label viitenro = new Label("Laskun viitenumero");
        Button syotaTiedot = new Button("Syötä");

        laskunTiedot.add(saaja,0,0);
        laskunTiedot.add(tilinro,0,1);
        laskunTiedot.add(erapv,0,2);
        laskunTiedot.add(viitenro,0,3);
        laskunTiedot.add(saajanNimi,1,0);
        laskunTiedot.add(tiliNumero,1,1);
        laskunTiedot.add(eraPaiva,1,2);
        laskunTiedot.add(viiteNumero,1,3);
        laskunTiedot.add(syotaTiedot,0,4);

        Scene scene = new Scene(laskunTiedot,400,600);
        laskuIkkuna.setScene(scene);
        laskuIkkuna.setTitle("Syötä laskun tiedot");
        laskuIkkuna.show();

        syotaTiedot.setOnAction(event -> {
            Document lasku = new Document();
            try {
                PdfWriter.getInstance(lasku, new FileOutputStream("lasku.pdf"));
                lasku.open();
                lasku.add(new Paragraph(toString()));
                lasku.add(new Paragraph("Saajan nimi : "+saajanNimi));
                lasku.add(new Paragraph("Saajan tilinumero: "+tiliNumero));
                lasku.add(new Paragraph("Laskun eräpäivä: "+eraPaiva));
                lasku.add(new Paragraph("Viitenumero: "+viiteNumero));
                lasku.close();
            } catch (Exception e) {
                System.out.println("Laskutietojen tallennuksessa tapahtui virhe.");
            }
        });
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
