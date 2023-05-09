package com.example.otiprojekti;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Nappula extends Button {
    public Nappula(String teksti, double leveys, double korkeus) {
        super(teksti);
        setPrefWidth(leveys);
        setPrefHeight(korkeus);
        setContentDisplay(ContentDisplay.CENTER);
        setFont(Font.font(18));
        setTextFill(Color.WHITE);
        setStyle("-fx-background-radius: 0; -fx-background-color: black; -fx-border-color: black");
        setOnMouseEntered(e -> setStyle("-fx-background-radius: 0; -fx-background-color: black; -fx-border-color: white"));
        setOnMouseExited(e -> setStyle("-fx-background-radius: 0; -fx-background-color: black; -fx-border-color: black"));
    }

    public Nappula(double leveys, double korkeus) {
        this("", leveys, korkeus);
    }

    public Nappula(String teksti) {
        this(teksti, 219, 40);
    }
}
