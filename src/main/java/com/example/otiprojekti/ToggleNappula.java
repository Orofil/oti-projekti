package com.example.otiprojekti;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ToggleNappula extends ToggleButton {
    public ToggleNappula(String teksti, double leveys, double korkeus) {
        super(teksti);
        setPrefWidth(leveys);
        setPrefHeight(korkeus);
        setContentDisplay(ContentDisplay.CENTER);
        setFont(Font.font(18));
        setTextFill(Color.WHITE);
        // TODO näille erillinen stylesheet
        // TODO tämä ei vielä eroa visuaalisesti normaalista nappulasta
        setStyle("-fx-background-radius: 0; -fx-background-color: black; -fx-border-color: black;");
        setOnMouseEntered(e -> setStyle("-fx-background-radius: 0; -fx-background-color: black; -fx-border-color: white;"));
        setOnMouseExited(e -> setStyle("-fx-background-radius: 0; -fx-background-color: black; -fx-border-color: black;"));
    }

    public ToggleNappula(String teksti) {
        this(teksti, 219, 40);
    }
}
