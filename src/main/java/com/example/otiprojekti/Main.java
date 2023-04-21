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

        painikkeet.getChildren().add(palkki);

        // Määritellään tekstit sivuvalikolle
        String[] nakymaTekstit = new String[] {
                "Alueet",
                "Mökit",
                "Palvelut",
                "Varaukset",
                "Asiakkaat",
                "Laskut"
        };

        // Luodaan sivuvalikon painikkeet
        for (String teksti : nakymaTekstit) {
            Rectangle nappula = new Rectangle(219, 40, Color.BLACK);
            nappula.setStroke(Color.BLACK);

            Label label = new Label(teksti, nappula);
            label.setContentDisplay(ContentDisplay.CENTER);
            label.setTextFill(Color.WHITE);
            label.setFont(Font.font(18));
            label.setOnMouseEntered( e -> nappula.setStroke(Color.WHITE));
            label.setOnMouseExited( e -> nappula.setStroke(Color.BLACK));

            painikkeet.getChildren().add(label);
        }

        painikkeet.getChildren().add(palkki2);

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

        BorderPane aluepaneeli = new BorderPane();
        paneeli.setCenter(aluepaneeli);

        //aluepaneeli.setTop(new Nappula("Paina tästä!"));

        ScrollPane alueScrollaus = new ScrollPane();
        aluepaneeli.setCenter(alueScrollaus);

        GridPane alueTaulukko = new GridPane();
        alueScrollaus.setContent(alueTaulukko);
        alueTaulukko.add(new Rectangle(350, 350), 0, 0); // TEMP
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