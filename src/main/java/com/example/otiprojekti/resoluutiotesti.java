package com.example.otiprojekti;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class resoluutiotesti extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        System.out.println("Näytön koko on " + bounds.getWidth() + " x " + bounds.getHeight());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
