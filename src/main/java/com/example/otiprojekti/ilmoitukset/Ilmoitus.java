package com.example.otiprojekti.ilmoitukset;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Ilmoitus extends StackPane {
    private static int leveys = 200;
    private static int korkeus = 70;
    private static int kulmaSade = 20;

    private Rectangle rect;
    private Text text;
    private int kesto;

    public Ilmoitus(IlmoitusTyyppi tyyppi, String teksti, int kesto) {
        super();

        rect = new Rectangle(leveys, korkeus);
        text = new Text(teksti);

        rect.setArcWidth(kulmaSade);
        rect.setArcHeight(kulmaSade);
        text.setWrappingWidth(leveys - 10);
        // Teksti voi olla liian pitkä, ratkaisuna voi olla https://stackoverflow.com/questions/65715608/truncating-text-after-a-specific-number-of-lines-in-javafx mutta meneekö vähän liian monimutkaiseksi

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

    public int getKesto() {
        return kesto;
    }

    public void setKesto(int kesto) {
        this.kesto = kesto;
    }
}
