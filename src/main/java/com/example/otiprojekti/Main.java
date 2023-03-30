package com.example.otiprojekti;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        StackPane pane = new StackPane();

        Text hello = new Text("Hello World!");
        pane.getChildren().add(hello);

        Scene scene = new Scene(pane, 600, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}