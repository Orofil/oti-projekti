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
    /**
     * Polku projektin resurssikansioon.
     */
    private static final String IMGPOLKU = "src/main/resources/com/example/otiprojekti/";

    @Override
    public void start(Stage ikkuna) {
        BorderPane paneeli = new BorderPane();

        VBox painikkeet = new VBox();

        // TODO näiden mittojen pitää mukautua ikkunan kokoon jos se muuttuu näytön koon mukaan
        Rectangle palkki = new Rectangle(220, 100, Color.BLACK);
        Rectangle palkki2 = new Rectangle(220, 500, Color.BLACK);


        Nappula aluenappula = new Nappula("Alueet");
        Nappula mokkinappula = new Nappula("Mökit");
        Nappula palvelunappula = new Nappula("Palvelut");
        Nappula varausnappula = new Nappula("Varaukset");
        Nappula asiakasnappula = new Nappula("Asiakkaat");
        Nappula laskunappula = new Nappula("Laskut");
        painikkeet.getChildren().addAll(palkki, aluenappula, mokkinappula, palvelunappula,
                varausnappula, asiakasnappula, laskunappula, palkki2);

        paneeli.setLeft(painikkeet);

        // TODO tälle "laastariratkaisulle" jokin parempi tapa, kuten erillisessä luokassa oleva julkinen metodi kuvan avaamiseen
        try {
            Image ylapalkinkuva = new Image(IMGPOLKU + "ylapalkki.png");
            ImageView ylapalkki = new ImageView(ylapalkinkuva);
            ylapalkki.setFitWidth(1600); // TODO näiden mittojen pitää mukautua ikkunan kokoon jos se muuttuu näytön koon mukaan
            ylapalkki.setFitHeight(100);
            paneeli.setTop(ylapalkki);
        } catch (Exception e) {
            Image ylapalkinkuva = new Image("ylapalkki.png");
            ImageView ylapalkki = new ImageView(ylapalkinkuva);
            ylapalkki.setFitWidth(1600);
            ylapalkki.setFitHeight(100);
            paneeli.setTop(ylapalkki);
        }



        //aluepaneeli.setTop(new Nappula("Paina tästä!")); // TEMP

        // Aluepaneelin luonti ja asetus
        BorderPane aluepaneeli = new BorderPane();
        paneeli.setCenter(aluepaneeli);
        aluenappula.setOnMouseClicked(e -> {
            paneeli.setCenter(aluepaneeli);
        });
        ScrollPane alueScrollaus = new ScrollPane();
        aluepaneeli.setCenter(alueScrollaus);
        GridPane alueTaulukko = new GridPane();
        alueScrollaus.setContent(alueTaulukko);


        // Mokkipaneelin luonti ja asetus
        BorderPane mokkipaneeli = new BorderPane();
        mokkinappula.setOnMouseClicked(e -> {
            paneeli.setCenter(mokkipaneeli);
        });
        ScrollPane mokkiScrollaus = new ScrollPane();
        mokkipaneeli.setCenter(mokkiScrollaus);
        GridPane mokkiTaulukko = new GridPane();
        mokkiScrollaus.setContent(mokkiTaulukko);


        // Palvelupaneelin luonti ja asetus
        BorderPane palvelupaneeli = new BorderPane();
        palvelunappula.setOnMouseClicked(e -> {
            paneeli.setCenter(palvelupaneeli);
        });
        ScrollPane palveluScrollaus = new ScrollPane();
        palvelupaneeli.setCenter(palveluScrollaus);
        GridPane palveluTaulukko = new GridPane();
        palveluScrollaus.setContent(palveluTaulukko);


        // Varauspaneelin luonti ja asetus
        BorderPane varauspaneeli = new BorderPane();
        varausnappula.setOnMouseClicked(e -> {
            paneeli.setCenter(varauspaneeli);
        });
        ScrollPane varausScrollaus = new ScrollPane();
        varauspaneeli.setCenter(varausScrollaus);
        GridPane varausTaulukko = new GridPane();
        varausScrollaus.setContent(varausTaulukko);

        // Asiakaspaneelin luonti ja asetus
        BorderPane asiakaspaneeli = new BorderPane();
        asiakasnappula.setOnMouseClicked(e -> {
            paneeli.setCenter(asiakaspaneeli);
        });
        ScrollPane asiakasScrollaus = new ScrollPane();
        asiakaspaneeli.setCenter(asiakasScrollaus);
        GridPane asiakasTaulukko = new GridPane();
        asiakasScrollaus.setContent(asiakasTaulukko);

        // Laskupaneelin luonti ja asetus
        BorderPane laskupaneeli = new BorderPane();
        laskunappula.setOnMouseClicked(e -> {
            paneeli.setCenter(laskupaneeli);
        });
        ScrollPane laskuScrollaus = new ScrollPane();
        varauspaneeli.setCenter(laskuScrollaus);
        GridPane laskuTaulukko = new GridPane();
        varausScrollaus.setContent(laskuTaulukko);



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