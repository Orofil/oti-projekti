package com.example.otiprojekti;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;

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

        return (getVaraus().getMokki().getHinta().multiply(BigDecimal.valueOf(daysBetween)).
                add(varaustenHinta)).multiply(BigDecimal.valueOf((getAlv() / 100d) + 1));
    }

    public BigDecimal getVarausPalveluSumma() {
        BigDecimal varaustenHinta = BigDecimal.ZERO;
        for (Map.Entry<Palvelu, Integer> vp : getVaraus().getPalvelut().entrySet()) {
            varaustenHinta = varaustenHinta.add(vp.getKey().getHinta());
        }
        return varaustenHinta;
    }

    @Override
    public String toString() {
        String str =
                "LaskuID:\t " + ID +
                "\nVarausID:\t " + varaus.getID() +
                "\nAsiakas:\t " + getVaraus().getAsiakas().getNimi(true)+
                "\nAsiakasID:\t " + getVaraus().getAsiakas().getID()+
                "\n-----------------------------------------------------\n"+
                "\nMaksettava (EURO) :\t " + summa +
                "\nAlv (%) :\t " + alv;
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


    public boolean vieDokumentiksi() { // TODO
        Stage laskuIkkuna = new Stage();
        GridPane laskunTiedot = new GridPane();
        laskunTiedot.setPadding(new Insets(25));
        laskunTiedot.setVgap(10);
        laskunTiedot.setHgap(10);
        TextField saajanNimi = new TextField("Village Newbies Oy");
        TextField tiliNumero = new TextField("FI12 3456 7891 0111 21");

        LocalDate currentDate = LocalDate.now();
        LocalDate eraPaivaDate = currentDate.plusDays(14);
        TextField eraPaiva = new TextField(
                String.valueOf(eraPaivaDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
        );
        TextField viiteNumero = new TextField("404");

        CheckBox lahetetyksi = new CheckBox("Merkitse laskun status lähetetyksi");

        Label saaja = new Label("Saajan nimi");
        Label tilinro = new Label("Saajan IBAN");
        Label erapv = new Label("Laskun eräpäivä");
        Label viitenro = new Label("Laskun viitenumero");
        Nappula syotaTiedot = new Nappula("Luo lasku");

        laskunTiedot.add(saaja,0,0);
        laskunTiedot.add(tilinro,0,1);
        laskunTiedot.add(erapv,0,2);
        laskunTiedot.add(viitenro,0,3);
        laskunTiedot.add(saajanNimi,1,0);
        laskunTiedot.add(tiliNumero,1,1);
        laskunTiedot.add(eraPaiva,1,2);
        laskunTiedot.add(viiteNumero,1,3);
        laskunTiedot.add(lahetetyksi,0,4);
        laskunTiedot.add(syotaTiedot,0,5);

        Scene scene = new Scene(laskunTiedot,400,600);
        laskuIkkuna.setScene(scene);
        laskuIkkuna.setTitle("Syötä laskun tiedot");
        laskuIkkuna.show();

        AtomicReference<Boolean> palautus = new AtomicReference<>(false);

        syotaTiedot.setOnAction(event -> {
            Document lasku = new Document();
            String tiedostonNimi = "Lasku "+String.valueOf(
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd HHmmss")))+".pdf";
            try {
                PdfWriter.getInstance(lasku, new FileOutputStream(tiedostonNimi));
                lasku.open();
                lasku.add(new Paragraph(toString()));
                lasku.add(new Paragraph("\n-----------------------------------------------------\n"));
                lasku.add(new Paragraph("Saajan nimi:\t "+saajanNimi.getText()));
                lasku.add(new Paragraph("Saajan IBAN:\t "+tiliNumero.getText()));
                lasku.add(new Paragraph("Laskun päiväys:\t "+saajanNimi.getText()));
                lasku.add(new Paragraph("Eräpäivä:\t "+eraPaiva.getText()));
                lasku.add(new Paragraph("Viitenumero:\t "+viiteNumero.getText()));
                lasku.close();
            } catch (Exception e) {
                System.out.println("Laskutietojen tallennuksessa tapahtui virhe.");
            }
            // Avataan dokumentti oletusohjelmalla
            try {
                Desktop.getDesktop().open(new File(tiedostonNimi));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (lahetetyksi.isSelected()) {
                palautus.set(true);
            }
            laskuIkkuna.close();
        });
        return palautus.get();
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
