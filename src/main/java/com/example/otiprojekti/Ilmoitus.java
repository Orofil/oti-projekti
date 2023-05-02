package com.example.otiprojekti;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Ilmoitus extends StackPane {
    private Rectangle rect;
    private Text text;
    private int kesto;

    public Ilmoitus(String teksti, int kesto) {
        super();

        rect = new Rectangle(150, 90);
        text = new Text(teksti);

        rect.setArcWidth(20);
        rect.setArcHeight(20);
        rect.setFill(Color.rgb(255,127,127));
        rect.setOpacity(0.7);

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
