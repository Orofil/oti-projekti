package com.example.otiprojekti;

import com.example.otiprojekti.nakymat.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;


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
    public Nappula[] nappulat = new Nappula[] {
            aluenappula, mokkinappula, palvelunappula, varausnappula, asiakasnappula, laskunappula};

    public BorderPane paneeli = new BorderPane();

    private final Font fontti = Font.font(16);

    @Override
    public void start(Stage ikkuna) {
        // Vasen valikko
        VBox painikkeet = new VBox();
        painikkeet.setPadding(new Insets(100,0,0,0));
        painikkeet.getChildren().addAll(aluenappula, mokkinappula, palvelunappula,
                varausnappula, asiakasnappula, laskunappula);

        paneeli.setLeft(painikkeet);
        paneeli.getLeft().setStyle("-fx-background-color: black");

        // TODO tälle "laastariratkaisulle" jokin parempi tapa, kuten erillisessä luokassa oleva julkinen metodi kuvan avaamiseen
        Image ylapalkinkuva;
        try {
            ylapalkinkuva = new Image(IMGPOLKU + "ylapalkki.png");
        } catch (Exception e) {
            ylapalkinkuva = new Image("ylapalkki.png");
        }
        ImageView ylapalkki = new ImageView(ylapalkinkuva);
        ylapalkki.setFitWidth(1600); // TODO näiden mittojen pitää mukautua ikkunan kokoon jos se muuttuu näytön koon mukaan
        ylapalkki.setFitHeight(100);
        paneeli.setTop(ylapalkki);



        //aluepaneeli.setTop(new Nappula("Paina tästä!")); // TEMP

        luoAluenakyma();


        // Mokkipaneelin luonti ja asetus

        luoMokkinakyma();


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


        // Lasketaan koko ikkunalle
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double boundsW = bounds.getWidth();
        double boundsH = bounds.getHeight();
        // Ikkunan mittasuhde jaettuna kahteen muuttujaan
        double suhdeW = 5;
        double suhdeH = 3;
        double boundsSuhdeMin = Math.min(boundsW / suhdeW, boundsH / suhdeH);
        // Kuinka suuren osan näytön leveydestä tai korkeudesta ikkuna vie enintään
        double maxOsuus = 0.9;
        double W = boundsSuhdeMin * maxOsuus * suhdeW;
        double H = boundsSuhdeMin * maxOsuus * suhdeH;
        Scene aluekehys = new Scene(paneeli, W, H);
        ikkuna.setScene(aluekehys);
        ikkuna.setMaxWidth(1600);
        ikkuna.setMaxHeight(800);
        ikkuna.show();
    }


    public void luoAluenakyma() {

        ColumnConstraints kolumniLeveys = new ColumnConstraints();
        kolumniLeveys.setHalignment(HPos.CENTER);
        kolumniLeveys.setPrefWidth(200);

        BorderPane aluepaneeli = new BorderPane();
        paneeli.setCenter(aluepaneeli);
        aluenappula.setOnMouseClicked(e -> {
            paneeli.setCenter(aluepaneeli);
//            for (Nappula2 n : nappulat) {
//                n.deselect();
//            }
//            aluenappula.select();
        });

        GridPane alueHaku = new GridPane();
        alueHaku.setPadding(new Insets(50,50,50,0));
        alueHaku.setHgap(200);
        alueHaku.setVgap(15);
        aluepaneeli.setTop(alueHaku);

        TextField alueHakuKentta = new TextField();
        Label alueHakuKenttaLabel = new Label("Hae aluetta: ", alueHakuKentta);
        alueHakuKenttaLabel.setFont(fontti);
        alueHakuKenttaLabel.setContentDisplay(ContentDisplay.RIGHT);
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
        alueTaulukko.getColumnConstraints().addAll(kolumniLeveys, kolumniLeveys);
        alueTaulukko.setGridLinesVisible(true);
        alueScrollaus.setContent(alueTaulukko);


        Nappula alueenLisays = new Nappula("Lisää uusi alue", 200, 30);
        alueTaulukko.add(alueenLisays, 1,0);

        Text aluetunnusOtsikko = new Text("Aluetunnus");
        aluetunnusOtsikko.setFont(fontti);
        Text alueennimiOtsikko = new Text("Alueen nimi");
        alueennimiOtsikko.setFont(fontti);
        alueTaulukko.add(aluetunnusOtsikko, 0, 1);
        alueTaulukko.add(alueennimiOtsikko, 1, 1);

        ArrayList<Alue> aluelista = new ArrayList<Alue>();
        aluelista.add(new Alue(1, "Ylläs"));       //TEMP
        aluelista.add(new Alue(2, "Levi"));        //TEMP


        int laskuri = 2;
        for (Alue obj : aluelista) {
            Text alueID = new Text(String.valueOf(obj.getAlueID()));
            alueID.setFont(fontti);
            Text alueNimi = new Text(String.valueOf(obj.getAlueenNimi()));
            alueNimi.setFont(fontti);
            alueTaulukko.add(alueID, 0, laskuri);

            alueID.setTextAlignment(TextAlignment.CENTER);
            alueTaulukko.add(alueNimi, 1, laskuri);
            //alueNimi.setAlignment(Pos.CENTER);
            alueNimi.setTextAlignment(TextAlignment.CENTER);

            Nappula poistoNappula = new Nappula("Poista alue", 150, 30);
            alueTaulukko.add(poistoNappula, 2, laskuri);
            poistoNappula.setOnMouseClicked(e -> {
                // poistaAlue();                          //TODO  poistaAlue() - metodin luominen
            });

            laskuri++;
        }
    }
    
    public void luoMokkinakyma() {
        ColumnConstraints kolumniLeveys = new ColumnConstraints();
        kolumniLeveys.setHalignment(HPos.CENTER);
        kolumniLeveys.setPrefWidth(200);

        BorderPane mokkipaneeli = new BorderPane();
        paneeli.setCenter(mokkipaneeli);
        mokkinappula.setOnMouseClicked(e -> {
            paneeli.setCenter(mokkipaneeli);
//            for (Nappula n : nappulat) {
//                n.deselect();
//            }
//            mokkinappula.select();
        });

        GridPane mokkiHaku = new GridPane();
        mokkiHaku.setPadding(new Insets(50,50,50,0));
        mokkiHaku.setHgap(200);
        mokkiHaku.setVgap(15);
        mokkipaneeli.setTop(mokkiHaku);

        TextField mokkiHakuKentta = new TextField();
        Label mokkiHakuKenttaLabel = new Label("Hae mökkiä: ", mokkiHakuKentta);
        mokkiHakuKenttaLabel.setFont(fontti);
        mokkiHakuKenttaLabel.setContentDisplay(ContentDisplay.RIGHT);
        mokkiHaku.add(mokkiHakuKenttaLabel, 1, 1);
        Nappula mokkiHakuNappula = new Nappula("Suorita haku", 190, 30);
        mokkiHaku.add(mokkiHakuNappula, 1, 2);
        mokkiHaku.add(new Text("Näytä tulokset järjestyksessä"), 2, 0);

        ToggleGroup toggleMokki = new ToggleGroup();

        RadioButton uusinMokki = new RadioButton("uusin - vanhin");
        mokkiHaku.add(uusinMokki, 2, 1);
        uusinMokki.setToggleGroup(toggleMokki);

        RadioButton vanhinMokki = new RadioButton("vanhin - uusin");
        mokkiHaku.add(vanhinMokki, 2, 2);
        vanhinMokki.setToggleGroup(toggleMokki);

        RadioButton aakkosMokki = new RadioButton("A - Ö");
        mokkiHaku.add(aakkosMokki, 2, 3);
        aakkosMokki.setToggleGroup(toggleMokki);

        ScrollPane mokkiScrollaus = new ScrollPane();
        mokkipaneeli.setCenter(mokkiScrollaus);
        GridPane mokkiTaulukko = new GridPane();
        mokkiTaulukko.setPadding(new Insets(50,50,50,150));
        mokkiTaulukko.getColumnConstraints().addAll(kolumniLeveys, kolumniLeveys);
        mokkiTaulukko.setGridLinesVisible(true);
        mokkiScrollaus.setContent(mokkiTaulukko);


        Nappula mokkienLisays = new Nappula("Lisää uusi mökki", 200, 30);
        mokkiTaulukko.add(mokkienLisays, 1,0);

        Text mokkitunnusOtsikko = new Text("Mökkitunnus");
        mokkitunnusOtsikko.setFont(fontti);
        Text mokkiennimiOtsikko = new Text("Mökin nimi");
        mokkiennimiOtsikko.setFont(fontti);
        mokkiTaulukko.add(mokkitunnusOtsikko, 0, 1);
        mokkiTaulukko.add(mokkiennimiOtsikko, 1, 1);

        ArrayList<Mokki> mokkilista = new ArrayList<Mokki>();
        mokkilista.add(new Mokki(1, "Ylläs"));       //TEMP
        mokkilista.add(new Mokki(2, "Levi"));        //TEMP


        int mokkiLaskuri = 2;
        for (Mokki obj : mokkilista) {
            Text mokkiID = new Text(String.valueOf(obj.getMokkiID()));
            mokkiID.setFont(fontti);
            Text mokkiNimi = new Text(String.valueOf(obj.getMokkiNimi()));
            mokkiNimi.setFont(fontti);
            mokkiTaulukko.add(mokkiID, 0, mokkiLaskuri);

            mokkiID.setTextAlignment(TextAlignment.CENTER);
            mokkiTaulukko.add(mokkiNimi, 1, mokkiLaskuri);
            //mokkiNimi.setAlignment(Pos.CENTER);
            mokkiNimi.setTextAlignment(TextAlignment.CENTER);

            Nappula poistoNappula = new Nappula("Poista mökki", 150, 30);
            mokkiTaulukko.add(poistoNappula, 2, mokkiLaskuri);
            poistoNappula.setOnMouseClicked(e -> {
                // poistamokki();                          //TODO  poistamokki() - metodin luominen
            });

            mokkiLaskuri++;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}