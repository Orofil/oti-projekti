package com.example.otiprojekti;

import com.example.otiprojekti.nakymat.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
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

    public Nappula aluenappula = new Nappula("Alueet");
    public Nappula mokkinappula = new Nappula("Mökit");
    public Nappula palvelunappula = new Nappula("Palvelut");
    public Nappula varausnappula = new Nappula("Varaukset");
    public Nappula asiakasnappula = new Nappula("Asiakkaat");
    public Nappula laskunappula = new Nappula("Laskut");

    public BorderPane paneeli = new BorderPane();

    @Override
    public void start(Stage ikkuna) {



        VBox painikkeet = new VBox();

        // TODO näiden mittojen pitää mukautua ikkunan kokoon jos se muuttuu näytön koon mukaan
        Rectangle palkki = new Rectangle(220, 100, Color.BLACK);
        Rectangle palkki2 = new Rectangle(220, 500, Color.BLACK);

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
        Aluenakyma aluepaneeli = new Aluenakyma();
        paneeli.setCenter(aluepaneeli);
        aluenappula.setOnMouseClicked(e -> {
            paneeli.setCenter(aluepaneeli);
        });

        GridPane alueHaku = new GridPane();
        alueHaku.setPadding(new Insets(50,50,50,0));
        alueHaku.setHgap(200);
        alueHaku.setVgap(15);
        aluepaneeli.setTop(alueHaku);

        TextField alueHakuKentta = new TextField();
        Label alueHakuKenttaLabel = new Label("Hae aluetta: ", alueHakuKentta);
        alueHakuKenttaLabel.setFont(Font.font(16));
        alueHakuKenttaLabel.setContentDisplay(ContentDisplay.BOTTOM);
        alueHaku.add(alueHakuKenttaLabel, 1, 1);
        Nappula alueHakuNappula = new Nappula("Suorita haku", 190, 30);
        alueHaku.add(alueHakuNappula, 1, 2);
        alueHaku.add(new Text("Näytä tulokset järjestyksessä"), 2, 0);

        ToggleGroup toggleAlue = new ToggleGroup();

        RadioButton uusinAlue = new RadioButton("uusin - vanhin");
        alueHaku.add(uusinAlue, 2, 1);
        uusinAlue.setToggleGroup(toggleAlue);

        RadioButton vanhinAlue = new RadioButton("vanhin - uusin");
        alueHaku.add(vanhinAlue, 2, 2);
        vanhinAlue.setToggleGroup(toggleAlue);

        RadioButton aakkosAlue = new RadioButton("A - Ö");
        alueHaku.add(aakkosAlue, 2, 3);
        aakkosAlue.setToggleGroup(toggleAlue);

        ScrollPane alueScrollaus = new ScrollPane();
        aluepaneeli.setCenter(alueScrollaus);
        GridPane alueTaulukko = new GridPane();
        alueTaulukko.setPadding(new Insets(50,50,50,150));
        alueTaulukko.setAlignment(Pos.CENTER);
        alueTaulukko.setGridLinesVisible(true);
        alueScrollaus.setContent(alueTaulukko);


        Nappula alueenLisays = new Nappula("Lisää uusi alue", 190, 30);
        alueTaulukko.add(alueenLisays, 1,0);

        Text aluetunnusOtsikko = new Text("    Aluetunnus    ");
        aluetunnusOtsikko.setFont(Font.font(16));
        Text alueennimiOtsikko = new Text("    Alueen nimi    ");
        alueennimiOtsikko.setFont(Font.font(16));
        alueTaulukko.add(aluetunnusOtsikko, 0, 1);
        alueTaulukko.add(alueennimiOtsikko, 1, 1);



        // Mokkipaneelin luonti ja asetus
        Mokkinakyma mokkipaneeli = new Mokkinakyma();
        mokkinappula.setOnMouseClicked(e -> {
            paneeli.setCenter(mokkipaneeli);
        });
        ScrollPane mokkiScrollaus = new ScrollPane();
        mokkipaneeli.setCenter(mokkiScrollaus);
        GridPane mokkiTaulukko = new GridPane();
        mokkiTaulukko.setGridLinesVisible(true);
        mokkiScrollaus.setContent(mokkiTaulukko);


        // Palvelupaneelin luonti ja asetus
        Palvelunakyma palvelupaneeli = new Palvelunakyma();
        palvelunappula.setOnMouseClicked(e -> {
            paneeli.setCenter(palvelupaneeli);
        });
        ScrollPane palveluScrollaus = new ScrollPane();
        palvelupaneeli.setCenter(palveluScrollaus);
        GridPane palveluTaulukko = new GridPane();
        palveluTaulukko.setGridLinesVisible(true);
        palveluScrollaus.setContent(palveluTaulukko);


        // Varauspaneelin luonti ja asetus
        Varausnakyma varauspaneeli = new Varausnakyma();
        varausnappula.setOnMouseClicked(e -> {
            paneeli.setCenter(varauspaneeli);
        });
        ScrollPane varausScrollaus = new ScrollPane();
        varauspaneeli.setCenter(varausScrollaus);
        GridPane varausTaulukko = new GridPane();
        varausTaulukko.setGridLinesVisible(true);
        varausScrollaus.setContent(varausTaulukko);

        // Asiakaspaneelin luonti ja asetus
        Asiakasnakyma asiakaspaneeli = new Asiakasnakyma();
        asiakasnappula.setOnMouseClicked(e -> {
            paneeli.setCenter(asiakaspaneeli);
        });
        ScrollPane asiakasScrollaus = new ScrollPane();
        asiakaspaneeli.setCenter(asiakasScrollaus);
        GridPane asiakasTaulukko = new GridPane();
        asiakasTaulukko.setGridLinesVisible(true);
        asiakasScrollaus.setContent(asiakasTaulukko);

        // Laskupaneelin luonti ja asetus
        Laskunakyma laskupaneeli = new Laskunakyma();
        laskunappula.setOnMouseClicked(e -> {
            paneeli.setCenter(laskupaneeli);
        });
        ScrollPane laskuScrollaus = new ScrollPane();
        varauspaneeli.setCenter(laskuScrollaus);
        GridPane laskuTaulukko = new GridPane();
        laskuTaulukko.setGridLinesVisible(true);
        varausScrollaus.setContent(laskuTaulukko);



        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        System.out.println("Näytön koko on " + bounds.getWidth() + " x " + bounds.getHeight()); // TEMP
        Scene aluekehys = new Scene(paneeli, bounds.getWidth(), bounds.getHeight()); // TODO parempi ratkaisu ikkunan koolle
        ikkuna.setScene(aluekehys);
        ikkuna.show();
    }

    public static void main(String[] args) {
        launch();
    }
}