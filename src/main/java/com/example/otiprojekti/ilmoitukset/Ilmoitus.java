package com.example.otiprojekti.ilmoitukset;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Ilmoitus extends StackPane {
    private final static int leveys = 200;
    private final static int korkeus = 70;
    private final static int kulmaSade = 20;

    private String teksti;
    private int kesto;

    public Ilmoitus(IlmoitusTyyppi tyyppi, String teksti, int kesto) {
        super();

        Rectangle rect = new Rectangle(leveys, korkeus);

        this.teksti = teksti;
        // Lyhennetään teksti jos se on liian pitkä
        if (teksti.length() > 72) {
            teksti = teksti.substring(0, 71) + "...";
        }
        Text text = new Text(teksti);

        rect.setArcWidth(kulmaSade);
        rect.setArcHeight(kulmaSade);
        text.setWrappingWidth(leveys - 10);


        Color vari = null;
        switch (tyyppi) {
            case ILMOITUS -> vari = Color.rgb(140, 170, 255);
            case VAROITUS -> vari = Color.rgb(255, 127, 127);
        }
        rect.setFill(vari);
        rect.setOpacity(0.8);

        getChildren().addAll(rect, text);

        this.kesto = kesto;
    }

    public String getTeksti() {
        return teksti;
    }

    public void setTeksti(String teksti) {
        this.teksti = teksti;
    }

    public int getKesto() {
        return kesto;
    }

    public void setKesto(int kesto) {
        this.kesto = kesto;
    }
}
