package com.example.otiprojekti;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Nappula extends Label {

    private double leveys;
    private double korkeus;
    private String teksti;
    private Rectangle nappula;
    private Label label = new Label(getTeksti(), getNappula());

    public Nappula(double leveys, double korkeus, String teksti) {
        this.leveys = leveys;
        this.korkeus = korkeus;
        this.teksti = teksti;
        this.nappula = new Rectangle(leveys, korkeus, Color.BLACK);
        nappula.setStroke(Color.BLACK);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font(18));
        label.setOnMouseEntered( e -> nappula.setStroke(Color.WHITE));
        label.setOnMouseExited( e -> nappula.setStroke(Color.BLACK));
    }

    public Nappula(String teksti) {
        this.teksti = teksti;
        this.nappula = new Rectangle(219, 40, Color.BLACK);
        nappula.setStroke(Color.BLACK);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font(18));
        label.setOnMouseEntered( e -> nappula.setStroke(Color.WHITE));
        label.setOnMouseExited( e -> nappula.setStroke(Color.BLACK));
    }




    public double getLeveys() {
        return leveys;
    }


    public void setLeveys(double width) {
        this.leveys = width;
    }

    public double getKorkeus() {
        return korkeus;
    }

    public void setKorkeus(double korkeus) {
        this.korkeus = korkeus;
    }

    public String getTeksti() {
        return teksti;
    }

    public void setTeksti(String teksti) {
        this.teksti = teksti;
    }

    public Rectangle getNappula() {
        return nappula;
    }

    public void setNappula(Rectangle nappula) {
        this.nappula = nappula;
    }

}
