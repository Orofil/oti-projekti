package com.example.otiprojekti;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Nappula extends Label {
    private Rectangle nappula;

    public Nappula(String teksti, double leveys, double korkeus) {
        nappula = new Rectangle(leveys, korkeus, Color.BLACK);
        nappula.setStroke(Color.BLACK);
        setText(teksti);
        setGraphic(nappula);
        setContentDisplay(ContentDisplay.CENTER);
        setTextFill(Color.WHITE);
        setFont(Font.font(18));
        setOnMouseEntered( e -> nappula.setStroke(Color.WHITE));
        setOnMouseExited( e -> nappula.setStroke(Color.BLACK));
    }

    public Nappula(String teksti) {
        this(teksti, 219, 40);
    }

    public void select() {
        nappula.setFill(Color.rgb(70,70,70));
    }

    public void deselect() {
        nappula.setFill(Color.BLACK);
    }
}
