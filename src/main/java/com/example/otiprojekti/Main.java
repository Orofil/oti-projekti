package com.example.otiprojekti;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;


public class Main extends Application {
    private static final String IMGPOLKU = "src/main/resources/com/example/otiprojekti/";

    @Override
    public void start(Stage ikkuna) {
        BorderPane paneeli = new BorderPane();

        VBox painikkeet = new VBox();

        Rectangle palkki = new Rectangle(220, 100, Color.BLACK);
        Rectangle palkki2 = new Rectangle(220, 500, Color.BLACK);

        Rectangle aluenappula = new Rectangle(219, 40, Color.BLACK);
        aluenappula.setStroke(Color.BLACK);
        Label alueteksti = new Label("Alueet", aluenappula);
        alueteksti.setContentDisplay(ContentDisplay.CENTER);
        alueteksti.setTextFill(Color.WHITE);
        alueteksti.setFont(Font.font(18));
        alueteksti.setOnMouseEntered( e -> {
            aluenappula.setStroke(Color.WHITE);
        });
        alueteksti.setOnMouseExited( e -> {
            aluenappula.setStroke(Color.BLACK);
        });

        Rectangle mokkinappula = new Rectangle(219, 40, Color.BLACK);
        mokkinappula.setStroke(Color.BLACK);
        Label mokkiteksti = new Label("Mökit", mokkinappula);
        mokkiteksti.setContentDisplay(ContentDisplay.CENTER);
        mokkiteksti.setTextFill(Color.WHITE);
        mokkiteksti.setFont(Font.font(18));
        mokkiteksti.setOnMouseEntered( e -> {
            mokkinappula.setStroke(Color.WHITE);
        });
        mokkiteksti.setOnMouseExited( e -> {
            mokkinappula.setStroke(Color.BLACK);
        });

        Rectangle palvelunappula = new Rectangle(219, 40, Color.BLACK);
        palvelunappula.setStroke(Color.BLACK);
        Label palveluteksti = new Label("Palvelut", palvelunappula);
        palveluteksti.setContentDisplay(ContentDisplay.CENTER);
        palveluteksti.setTextFill(Color.WHITE);
        palveluteksti.setFont(Font.font(18));
        palveluteksti.setOnMouseEntered( e -> {
            palvelunappula.setStroke(Color.WHITE);
        });
        palveluteksti.setOnMouseExited( e -> {
            palvelunappula.setStroke(Color.BLACK);
        });

        Rectangle varausnappula = new Rectangle(219, 40, Color.BLACK);
        varausnappula.setStroke(Color.BLACK);
        Label varausteksti = new Label("Varaukset", varausnappula);
        varausteksti.setContentDisplay(ContentDisplay.CENTER);
        varausteksti.setTextFill(Color.WHITE);
        varausteksti.setFont(Font.font(18));
        varausteksti.setOnMouseEntered( e -> {
            varausnappula.setStroke(Color.WHITE);
        });
        varausteksti.setOnMouseExited( e -> {
            varausnappula.setStroke(Color.BLACK);
        });


        Rectangle asiakasnappula = new Rectangle(219, 40, Color.BLACK);
        asiakasnappula.setStroke(Color.BLACK);
        Label asiakasteksti = new Label("Asiakkaat", asiakasnappula);
        asiakasteksti.setContentDisplay(ContentDisplay.CENTER);
        asiakasteksti.setTextFill(Color.WHITE);
        asiakasteksti.setFont(Font.font(18));
        asiakasteksti.setOnMouseEntered( e -> {
            asiakasnappula.setStroke(Color.WHITE);
        });
        asiakasteksti.setOnMouseExited( e -> {
            asiakasnappula.setStroke(Color.BLACK);
        });


        Rectangle laskunappula = new Rectangle(219, 40, Color.BLACK);
        laskunappula.setStroke(Color.BLACK);
        Label laskuteksti = new Label("Laskut", laskunappula);
        laskuteksti.setContentDisplay(ContentDisplay.CENTER);
        laskuteksti.setTextFill(Color.WHITE);
        laskuteksti.setFont(Font.font(18));
        laskuteksti.setOnMouseEntered( e -> {
            laskunappula.setStroke(Color.WHITE);
        });
        laskuteksti.setOnMouseExited( e -> {
            laskunappula.setStroke(Color.BLACK);
        });


        painikkeet.getChildren().addAll
                (palkki, alueteksti, mokkiteksti, palveluteksti, varausteksti, asiakasteksti, laskuteksti, palkki2);



        paneeli.setLeft(painikkeet);

        try {
            Image ylapalkinkuva = new Image(IMGPOLKU+"ylapalkki.png");
            ImageView ylapalkki = new ImageView(ylapalkinkuva);
            ylapalkki.setFitWidth(1600);
            ylapalkki.setFitHeight(100);
            paneeli.setTop(ylapalkki);
        } catch (Exception e) {
            Image ylapalkinkuva = new Image("ylapalkki.png");
            ImageView ylapalkki = new ImageView(ylapalkinkuva);
            ylapalkki.setFitWidth(1600);
            ylapalkki.setFitHeight(100);
            paneeli.setTop(ylapalkki);
        }

        BorderPane aluepaneeli = new BorderPane();
        paneeli.setCenter(aluepaneeli);

        ScrollPane alueScrollaus = new ScrollPane();
        aluepaneeli.setCenter(alueScrollaus);

        GridPane alueTaulukko = new GridPane();
        alueScrollaus.setContent(alueTaulukko);
        alueTaulukko.add(new Rectangle(350, 350), 0, 0);
        alueTaulukko.add(new Rectangle(350, 350), 1, 1);
        alueTaulukko.add(new Rectangle(350, 350), 0, 2);
        alueTaulukko.add(new Rectangle(350, 350), 1, 3);


        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        System.out.println("Näytön koko on " + bounds.getWidth() + " x " + bounds.getHeight());
        Scene aluekehys = new Scene(paneeli, bounds.getWidth(), bounds.getHeight());
        ikkuna.setScene(aluekehys);
        ikkuna.show();
    }

    public static void main(String[] args) {
        launch();
    }
}