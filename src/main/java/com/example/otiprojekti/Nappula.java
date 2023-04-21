package com.example.otiprojekti;

import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Nappula extends Label {

    public Nappula(String teksti, double leveys, double korkeus) {
        Rectangle nappula = new Rectangle(leveys, korkeus, Color.BLACK);
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
        Rectangle nappula = new Rectangle(219, 40, Color.BLACK);
        nappula.setStroke(Color.BLACK);
        setText(teksti);
        setGraphic(nappula);
        setContentDisplay(ContentDisplay.CENTER);
        setTextFill(Color.WHITE);
        setFont(Font.font(18));
        setOnMouseEntered( e -> nappula.setStroke(Color.WHITE));
        setOnMouseExited( e -> nappula.setStroke(Color.BLACK));
    }


}
