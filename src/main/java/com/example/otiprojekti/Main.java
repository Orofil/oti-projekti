package com.example.otiprojekti;

import com.example.otiprojekti.nakymat.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class Main extends Application {
    /**
     * Polku projektin resurssikansioon.
     */
    private static final String IMGPOLKU = "src/main/resources/com/example/otiprojekti/";

    // Ikkunan mittasuhde jaettuna kahteen kenttään
    private final double SUHDE_W = 5.5;
    private final double SUHDE_H = 3;
    // Kuinka suuren osan näytön leveydestä tai korkeudesta ikkuna vie enintään
    private final double MAX_OSUUS = 0.9;
    // Ikkunan suurin sallittu koko
    private final int MAX_LEVEYS = 1600; // TODO Näiden suhde ei ole sama kuin tuo oletussuhde mikä on vähän outoa
    private final int MAX_KORKEUS = 800;

    // TODO tehdäänkö näistä private?
    public ToggleNappula aluenappula = new ToggleNappula("Alueet");
    public ToggleNappula mokkinappula = new ToggleNappula("Mökit");
    public ToggleNappula palvelunappula = new ToggleNappula("Palvelut");
    public ToggleNappula varausnappula = new ToggleNappula("Varaukset");
    public ToggleNappula asiakasnappula = new ToggleNappula("Asiakkaat");
    public ToggleNappula laskunappula = new ToggleNappula("Laskut");
    public ToggleNappula[] nappulat = new ToggleNappula[] {
            aluenappula, mokkinappula, palvelunappula, varausnappula, asiakasnappula, laskunappula};
    private ToggleGroup tgSivuvalikko = new ToggleGroup();

    public BorderPane paneeli = new BorderPane();

    private final Font fontti = Font.font(16);

    Text isoOtsikkoTeksti = new Text("ALUEET");

    @Override
    public void start(Stage ikkuna) {
        // Vasen valikko
        for (ToggleNappula n : nappulat) {
            n.setToggleGroup(tgSivuvalikko);
        }

        VBox painikkeet = new VBox();
        painikkeet.setPadding(new Insets(100,0,0,0));
        painikkeet.getChildren().addAll(aluenappula, mokkinappula, palvelunappula,
                varausnappula, asiakasnappula, laskunappula);

        paneeli.setLeft(painikkeet);
        paneeli.getLeft().setStyle("-fx-background-color: black");

        Image logonkuva = imageKuvasta("vnlogo.png");
        ImageView logo = new ImageView(logonkuva);
        logo.setFitWidth(75);
        logo.setFitHeight(75);

        Pane ylapalkkipaneeli = new Pane();
        ylapalkkipaneeli.getChildren().add(logo);
        ylapalkkipaneeli.setBackground
                (new Background(new BackgroundFill(Color.DARKSEAGREEN, null, null)));
        logo.setX(73);
        logo.setY(22);
        ylapalkkipaneeli.setMinHeight(110);
        paneeli.setTop(ylapalkkipaneeli);

        isoOtsikkoTeksti.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));
        isoOtsikkoTeksti.setFill(Color.DARKGREEN);

        ylapalkkipaneeli.getChildren().add(isoOtsikkoTeksti);
        isoOtsikkoTeksti.setX(318);
        isoOtsikkoTeksti.setY(85);




        //aluepaneeli.setTop(new Nappula("Paina tästä!")); // TEMP

        luoAluenakyma();


        // Mokkipaneelin luonti ja asetus

        luoMokkinakyma();


        // Palvelupaneelin luonti ja asetus
        luoPalvelunakyma();


        // Varauspaneelin luonti ja asetus
        luoVarausnakyma();

        // Asiakaspaneelin luonti ja asetus
        luoAsiakasnakyma();

        // Laskupaneelin luonti ja asetus
        luoLaskunakyma();


        // Lasketaan koko ikkunalle
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double boundsW = bounds.getWidth();
        double boundsH = bounds.getHeight();
        double boundsSuhdeMin = Math.min(boundsW / SUHDE_W, boundsH / SUHDE_H);
        double W = Math.min(boundsSuhdeMin * MAX_OSUUS * SUHDE_W, MAX_LEVEYS);
        double H = Math.min(boundsSuhdeMin * MAX_OSUUS * SUHDE_H, MAX_KORKEUS);

        Scene aluekehys = new Scene(paneeli, W, H);
        ikkuna.setScene(aluekehys);
        ikkuna.setMaxWidth(MAX_LEVEYS);
        ikkuna.setMaxHeight(MAX_KORKEUS);
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
            isoOtsikkoTeksti.setText("ALUEET");
//            for (Nappula2 n : nappulat) {
//                n.deselect();
//            }
//            aluenappula.select();
        });

        GridPane alueHaku = new GridPane();
        alueHaku.setPadding(new Insets(50,50,50,0));
        alueHaku.setHgap(100);
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
        alueTaulukko.setPadding(new Insets(20,20,20,20));
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

        ArrayList<Alue> aluelista = new ArrayList<>();
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
        kolumniLeveys.setPrefWidth(170);

        ColumnConstraints semi = new ColumnConstraints();
        semi.setHalignment(HPos.CENTER);
        semi.setPrefWidth(120);

        ColumnConstraints lyhyt = new ColumnConstraints();
        lyhyt.setHalignment(HPos.CENTER);
        lyhyt.setPrefWidth(80);

        BorderPane mokkipaneeli = new BorderPane();
        //paneeli.setCenter(mokkipaneeli);
        mokkinappula.setOnMouseClicked(e -> {
            paneeli.setCenter(mokkipaneeli);
            isoOtsikkoTeksti.setText("MÖKIT");
//            for (Nappula n : nappulat) {
//                n.deselect();
//            }
//            mokkinappula.select();
        });

        GridPane mokkiHaku = new GridPane();
        mokkiHaku.setPadding(new Insets(50,50,50,0));
        mokkiHaku.setHgap(100);
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

        RadioButton hintaNousevaMokki = new RadioButton("edullisin - kallein");
        mokkiHaku.add(hintaNousevaMokki, 2, 1);
        hintaNousevaMokki.setToggleGroup(toggleMokki);

        RadioButton hintaLaskevaMokki = new RadioButton("kallein - edullisin");
        mokkiHaku.add(hintaLaskevaMokki, 2, 2);
        hintaLaskevaMokki.setToggleGroup(toggleMokki);

        RadioButton aakkosMokki = new RadioButton("A - Ö");
        mokkiHaku.add(aakkosMokki, 3, 1);
        aakkosMokki.setToggleGroup(toggleMokki);

        RadioButton hloMaaraMokki = new RadioButton("hlömäärän mukaan");
        mokkiHaku.add(hloMaaraMokki, 3, 2);
        hloMaaraMokki.setToggleGroup(toggleMokki);

        RadioButton alueittainMokki = new RadioButton("alueittain");
        mokkiHaku.add(alueittainMokki, 3, 3);
        alueittainMokki.setToggleGroup(toggleMokki);

        ScrollPane mokkiScrollaus = new ScrollPane();
        mokkipaneeli.setCenter(mokkiScrollaus);
        GridPane mokkiTaulukko = new GridPane();
        mokkiTaulukko.setPadding(new Insets(20,20,20,20));
        mokkiTaulukko.getColumnConstraints().addAll(
                lyhyt, kolumniLeveys, lyhyt, lyhyt, lyhyt, semi, semi, kolumniLeveys);
        mokkiTaulukko.setGridLinesVisible(true);
        mokkiScrollaus.setContent(mokkiTaulukko);


        Nappula mokkienLisays = new Nappula("Lisää uusi mökki", 200, 30);
        mokkiTaulukko.add(mokkienLisays, 1,0);

        Text mokkitunnusOtsikko = new Text("Tunnus");
        mokkitunnusOtsikko.setFont(fontti);
        Text mokinnimiOtsikko = new Text("Mökki");
        mokinnimiOtsikko.setFont(fontti);
        Text mokinAlueOtsikko = new Text("Alue");
        mokinAlueOtsikko.setFont(fontti);
        Text mokinHintaOtsikko = new Text("Hinta/vrk");
        mokinHintaOtsikko.setFont(fontti);
        Text mokinHloMaaraOtsikko = new Text("Hlö.määrä");
        mokinHloMaaraOtsikko.setFont(fontti);

        mokkiTaulukko.add(mokkitunnusOtsikko, 0, 1);
        mokkiTaulukko.add(mokinnimiOtsikko, 1, 1);
        mokkiTaulukko.add(mokinAlueOtsikko, 2, 1);
        mokkiTaulukko.add(mokinHintaOtsikko, 3, 1);
        mokkiTaulukko.add(mokinHloMaaraOtsikko, 4, 1);



        ArrayList<Mokki> mokkilista = new ArrayList<Mokki>();
        mokkilista.add(new Mokki(1,1, 34110,"Sininen mökki", "Sinitie 2",
        200, "Valoisa hirsimökki koko perheelle tai pienelle kaveriporukalle saunalla ja porealtaalla.",
        6, "Sauna, poreallas"));       //TEMP
        mokkilista.add(new Mokki(2,1, 34100,"Punainen mökki", "Sinitie 3",
                250, "Viihtyisä ja tilava hirsimökki koko perheelle tai kaveriporukalle saunalla ja porealtaalla.",
                8, "Sauna, poreallas"));       //TEMP


        int mokkiLaskuri = 2;
        for (Mokki obj : mokkilista) {
            Text mokkiID = new Text(String.valueOf(obj.getMokkiID()));
            mokkiID.setFont(fontti);
            Text mokkiNimi = new Text(String.valueOf(obj.getMokkiNimi()));
            mokkiNimi.setFont(fontti);
            Text mokkiAlue = new Text(String.valueOf(obj.getAlueID()));
            mokkiAlue.setFont(fontti);
            Text mokkiHinta = new Text(String.valueOf(obj.getHinta()));
            mokkiHinta.setFont(fontti);
            Text mokkiHloMaara = new Text(String.valueOf(obj.getHloMaara()));
            mokkiHloMaara.setFont(fontti);

            mokkiTaulukko.add(mokkiID, 0, mokkiLaskuri);
            mokkiTaulukko.add(mokkiNimi, 1, mokkiLaskuri);
            mokkiTaulukko.add(mokkiAlue, 2, mokkiLaskuri);
            mokkiTaulukko.add(mokkiHinta, 3, mokkiLaskuri);
            mokkiTaulukko.add(mokkiHloMaara, 4, mokkiLaskuri);

            Nappula poistoNappula = new Nappula("Poista", 120, 30);
            mokkiTaulukko.add(poistoNappula, 5, mokkiLaskuri);
            poistoNappula.setOnMouseClicked(e -> {
                // poistamokki();                          //TODO  poistamokki() - metodin luominen
            });
            Nappula muokkausNappula = new Nappula("Muokkaa", 120, 30);
            mokkiTaulukko.add(muokkausNappula, 6, mokkiLaskuri);
            muokkausNappula.setOnMouseClicked(e -> {
                // muokkaaMokki();                          //TODO  muokkaamokki() - metodin luominen
            });
            Nappula tarkasteleNappula = new Nappula("Tarkastele tietoja", 170, 30);
            mokkiTaulukko.add(tarkasteleNappula, 7, mokkiLaskuri);
            tarkasteleNappula.setOnMouseClicked(e -> {
                // tarkasteleMokkia();                          //TODO  tarkasteleMokkia() - metodin luominen
            });

            mokkiLaskuri++;
        }
    }
    
    public void luoPalvelunakyma() {

        ColumnConstraints kolumniLeveys = new ColumnConstraints();
        kolumniLeveys.setHalignment(HPos.CENTER);
        kolumniLeveys.setPrefWidth(200);

        ColumnConstraints semi = new ColumnConstraints();
        semi.setHalignment(HPos.CENTER);
        semi.setPrefWidth(120);

        ColumnConstraints lyhyt = new ColumnConstraints();
        lyhyt.setHalignment(HPos.CENTER);
        lyhyt.setPrefWidth(80);

        BorderPane palvelupaneeli = new BorderPane();
        //paneeli.setCenter(palvelupaneeli);
        palvelunappula.setOnMouseClicked(e -> {
            paneeli.setCenter(palvelupaneeli);
            isoOtsikkoTeksti.setText("PALVELUT");
//            for (Nappula n : nappulat) {
//                n.deselect();
//            }
//            palvelunappula.select();
        });

        GridPane palveluHaku = new GridPane();
        palveluHaku.setPadding(new Insets(50,50,50,0));
        palveluHaku.setHgap(100);
        palveluHaku.setVgap(15);
        palvelupaneeli.setTop(palveluHaku);

        TextField palveluHakuKentta = new TextField();
        Label palveluHakuKenttaLabel = new Label("Hae palveluita: ", palveluHakuKentta);
        palveluHakuKenttaLabel.setFont(fontti);
        palveluHakuKenttaLabel.setContentDisplay(ContentDisplay.RIGHT);
        palveluHaku.add(palveluHakuKenttaLabel, 1, 1);
        Nappula palveluHakuNappula = new Nappula("Suorita haku", 190, 30);
        palveluHaku.add(palveluHakuNappula, 1, 2);
        palveluHaku.add(new Text("Näytä tulokset järjestyksessä"), 2, 0);

        ToggleGroup togglepalvelu = new ToggleGroup();

        RadioButton uusinpalvelu = new RadioButton("uusin - vanhin");
        palveluHaku.add(uusinpalvelu, 2, 1);
        uusinpalvelu.setToggleGroup(togglepalvelu);

        RadioButton vanhinpalvelu = new RadioButton("vanhin - uusin");
        palveluHaku.add(vanhinpalvelu, 2, 2);
        vanhinpalvelu.setToggleGroup(togglepalvelu);

        RadioButton aakkospalvelu = new RadioButton("A - Ö");
        palveluHaku.add(aakkospalvelu, 3, 1);
        aakkospalvelu.setToggleGroup(togglepalvelu);

        RadioButton alueittainpalvelu = new RadioButton("alueittain");
        palveluHaku.add(alueittainpalvelu, 3, 2);
        aakkospalvelu.setToggleGroup(togglepalvelu);

        ScrollPane palveluScrollaus = new ScrollPane();
        palvelupaneeli.setCenter(palveluScrollaus);
        GridPane palveluTaulukko = new GridPane();
        palveluTaulukko.setPadding(new Insets(20,20,20,20));
        palveluTaulukko.getColumnConstraints().addAll(semi, kolumniLeveys, lyhyt, lyhyt);
        palveluTaulukko.setGridLinesVisible(true);
        palveluScrollaus.setContent(palveluTaulukko);


        Nappula palvelunLisays = new Nappula("Lisää uusi palvelu", 200, 30);
        palveluTaulukko.add(palvelunLisays, 1,0);

        Text palvelutunnusOtsikko = new Text("Tunnus");
        palvelutunnusOtsikko.setFont(fontti);
        Text palvelunnimiOtsikko = new Text("Palvelu");
        palvelunnimiOtsikko.setFont(fontti);
        Text palveluAlueOtsikko = new Text("Alue");
        palveluAlueOtsikko.setFont(fontti);
        Text palvelunHintaOtsikko = new Text("Hinta");
        palvelunHintaOtsikko.setFont(fontti);

        palveluTaulukko.add(palvelutunnusOtsikko, 0, 1);
        palveluTaulukko.add(palvelunnimiOtsikko, 1, 1);
        palveluTaulukko.add(palveluAlueOtsikko, 2, 1);
        palveluTaulukko.add(palvelunHintaOtsikko, 3, 1);

        ArrayList<Palvelu> palvelulista = new ArrayList<Palvelu>();
        palvelulista.add(new Palvelu(1, 1, "Moottorikelkkavuokra",
                "Välinevuokraus", "Moottorikelkan vuokraus 1 hlö 3h", 60, 24));       //TEMP
        palvelulista.add(new Palvelu(2, 1, "Kuumakivihieronta 60 min",
                "Hieronta", "Kuumakivihieronta 60 min koulutetulla hierojalla Levin elämyshoitolassa",
                70, 24));        //TEMP


        int palveluLaskuri = 2;
        for (Palvelu obj : palvelulista) {
            Text palveluID = new Text(String.valueOf(obj.getPalveluID()));
            palveluID.setFont(fontti);
            Text palveluNimi = new Text(String.valueOf(obj.getPalvelunNimi()));
            palveluNimi.setFont(fontti);
            Text palveluAlue = new Text(String.valueOf(obj.getAlueID()));
            palveluAlue.setFont(fontti);
            Text palveluHinta = new Text(String.valueOf(obj.getPalvelunHinta()));
            palveluHinta.setFont(fontti);


            palveluID.setTextAlignment(TextAlignment.CENTER);
            palveluTaulukko.add(palveluID, 0, palveluLaskuri);
            palveluTaulukko.add(palveluNimi, 1, palveluLaskuri);
            palveluTaulukko.add(palveluAlue, 2, palveluLaskuri);
            palveluTaulukko.add(palveluHinta, 3, palveluLaskuri);

            //palveluNimi.setAlignment(Pos.CENTER);
            palveluNimi.setTextAlignment(TextAlignment.CENTER);

            Nappula poistoNappula = new Nappula("Poista palvelu", 150, 30);
            palveluTaulukko.add(poistoNappula, 4, palveluLaskuri);
            poistoNappula.setOnMouseClicked(e -> {
                // poistapalvelu();                          //TODO  poistapalvelu() - metodin luominen
            });

            Nappula muokkausNappula = new Nappula("Muokkaa", 120, 30);
            palveluTaulukko.add(muokkausNappula, 5, palveluLaskuri);
            muokkausNappula.setOnMouseClicked(e -> {
                // muokkaaMokki();                          //TODO  muokkaamokki() - metodin luominen
            });
            Nappula tarkasteleNappula = new Nappula("Tarkastele tietoja", 170, 30);
            palveluTaulukko.add(tarkasteleNappula, 6, palveluLaskuri);
            tarkasteleNappula.setOnMouseClicked(e -> {
                // tarkasteleMokkia();                          //TODO  tarkasteleMokkia() - metodin luominen
            });

            palveluLaskuri++;
        }
    }

    public void luoVarausnakyma() {
        ColumnConstraints kolumniLeveys = new ColumnConstraints();
        kolumniLeveys.setHalignment(HPos.CENTER);
        kolumniLeveys.setPrefWidth(200);

        ColumnConstraints semi = new ColumnConstraints();
        semi.setHalignment(HPos.CENTER);
        semi.setPrefWidth(120);

        ColumnConstraints lyhyt = new ColumnConstraints();
        lyhyt.setHalignment(HPos.CENTER);
        lyhyt.setPrefWidth(80);

        BorderPane varauspaneeli = new BorderPane();
        //paneeli.setCenter(varauspaneeli);
        varausnappula.setOnMouseClicked(e -> {
            paneeli.setCenter(varauspaneeli);
            isoOtsikkoTeksti.setText("VARAUKSET");
//            for (Nappula n : nappulat) {
//                n.deselect();
//            }
//            varausnappula.select();
        });

        GridPane varausHaku = new GridPane();
        varausHaku.setPadding(new Insets(50,50,50,0));
        varausHaku.setHgap(100);
        varausHaku.setVgap(15);
        varauspaneeli.setTop(varausHaku);

        TextField varausHakuKentta = new TextField();
        Label varausHakuKenttaLabel = new Label("Hae varausta: ", varausHakuKentta);
        varausHakuKenttaLabel.setFont(fontti);
        varausHakuKenttaLabel.setContentDisplay(ContentDisplay.RIGHT);
        varausHaku.add(varausHakuKenttaLabel, 1, 1);
        Nappula varausHakuNappula = new Nappula("Suorita haku", 190, 30);
        varausHaku.add(varausHakuNappula, 1, 2);
        varausHaku.add(new Text("Näytä tulokset järjestyksessä"), 2, 0);

        ToggleGroup togglevaraus = new ToggleGroup();

        RadioButton uusinvaraus = new RadioButton("uusin - vanhin");
        varausHaku.add(uusinvaraus, 2, 1);
        uusinvaraus.setToggleGroup(togglevaraus);

        RadioButton vanhinvaraus = new RadioButton("vanhin - uusin");
        varausHaku.add(vanhinvaraus, 2, 2);
        vanhinvaraus.setToggleGroup(togglevaraus);

        RadioButton aakkosvaraus = new RadioButton("A - Ö");
        varausHaku.add(aakkosvaraus, 3, 1);
        aakkosvaraus.setToggleGroup(togglevaraus);

        RadioButton alueittainvaraus = new RadioButton("alueittain");
        varausHaku.add(alueittainvaraus, 3, 2);
        aakkosvaraus.setToggleGroup(togglevaraus);

        ScrollPane varausScrollaus = new ScrollPane();
        varauspaneeli.setCenter(varausScrollaus);
        GridPane varausTaulukko = new GridPane();
        varausTaulukko.setPadding(new Insets(20,20,20,20));
        varausTaulukko.getColumnConstraints().addAll(semi, kolumniLeveys, lyhyt, kolumniLeveys);
        varausTaulukko.setGridLinesVisible(true);
        varausScrollaus.setContent(varausTaulukko);


        Nappula varausnLisays = new Nappula("Lisää uusi varaus", 200, 30);
        varausTaulukko.add(varausnLisays, 1,0);

        Text varaustunnusOtsikko = new Text("Varaustunnus");
        varaustunnusOtsikko.setFont(fontti);
        Text varausAsiakasOtsikko = new Text("Asiakas");
        varausAsiakasOtsikko.setFont(fontti);
        Text varausAlueOtsikko = new Text("Alue");
        varausAlueOtsikko.setFont(fontti);
        Text varausSummaOtsikko = new Text("Summa");
        varausSummaOtsikko.setFont(fontti);
        Text varausMokkiOtsikko = new Text("Mökki");
        varausMokkiOtsikko.setFont(fontti);
        Text varausPalvelutOtsikko = new Text("Palvelut");
        varausPalvelutOtsikko.setFont(fontti);

        varausTaulukko.add(varaustunnusOtsikko, 0, 1);
        varausTaulukko.add(varausAsiakasOtsikko, 1, 1);
        varausTaulukko.add(varausMokkiOtsikko, 2, 1);

        ArrayList<Varaus> varauslista = new ArrayList<Varaus>();
        varauslista.add(new Varaus(123, 122, 234,
                LocalDateTime.of(2022, 9, 30, 12, 50),
                LocalDateTime.of(2022, 9, 30, 12, 52 ),
                LocalDateTime.of(2022, 10, 8, 15, 30),
                LocalDateTime.of(2022, 10, 10, 12, 0 )));
        varauslista.add(new Varaus(13, 12, 24,
                LocalDateTime.of(2023, 1, 15, 13, 50),
                LocalDateTime.of(2023, 1, 15, 14, 52 ),
                LocalDateTime.of(2023, 3, 6, 15, 30),
                LocalDateTime.of(2023, 3, 12, 12, 0 )));        //TEMP


        int varausLaskuri = 2;
        for (Varaus obj : varauslista) {
            Text varausID = new Text(String.valueOf(obj.getVarausID()));
            varausID.setFont(fontti);
            Text varausNimi = new Text(String.valueOf(obj.getAsiakasID()));
            varausNimi.setFont(fontti);
            Text varausMokki = new Text(String.valueOf(obj.getMokkiID()));
            varausMokki.setFont(fontti);


            varausID.setTextAlignment(TextAlignment.CENTER);
            varausTaulukko.add(varausID, 0, varausLaskuri);
            varausTaulukko.add(varausNimi, 1, varausLaskuri);
            varausTaulukko.add(varausMokki, 2, varausLaskuri);

            //varausNimi.setAlignment(Pos.CENTER);
            varausNimi.setTextAlignment(TextAlignment.CENTER);

            Nappula poistoNappula = new Nappula("Poista varaus", 200, 30);
            varausTaulukko.add(poistoNappula, 3, varausLaskuri);
            poistoNappula.setOnMouseClicked(e -> {
                // poistavaraus();                          //TODO  poistavaraus() - metodin luominen
            });

            Nappula muokkausNappula = new Nappula("Muokkaa", 120, 30);
            varausTaulukko.add(muokkausNappula, 4, varausLaskuri);
            muokkausNappula.setOnMouseClicked(e -> {
                // muokkaaMokki();                          //TODO  muokkaamokki() - metodin luominen
            });
            Nappula tarkasteleNappula = new Nappula("Tarkastele tietoja", 170, 30);
            varausTaulukko.add(tarkasteleNappula, 5, varausLaskuri);
            tarkasteleNappula.setOnMouseClicked(e -> {
                // tarkasteleMokkia();                          //TODO  tarkasteleMokkia() - metodin luominen
            });

            varausLaskuri++;
        }
    }

    public void luoAsiakasnakyma() {
        ColumnConstraints kolumniLeveys = new ColumnConstraints();
        kolumniLeveys.setHalignment(HPos.CENTER);
        kolumniLeveys.setPrefWidth(200);

        ColumnConstraints semi = new ColumnConstraints();
        semi.setHalignment(HPos.CENTER);
        semi.setPrefWidth(140);

        ColumnConstraints lyhyt = new ColumnConstraints();
        lyhyt.setHalignment(HPos.CENTER);
        lyhyt.setPrefWidth(80);

        BorderPane asiakaspaneeli = new BorderPane();
        //paneeli.setCenter(asiakaspaneeli);
        asiakasnappula.setOnMouseClicked(e -> {
            paneeli.setCenter(asiakaspaneeli);
            isoOtsikkoTeksti.setText("ASIAKKAAT");
//            for (Nappula n : nappulat) {
//                n.deselect();
//            }
//            asiakasnappula.select();
        });

        GridPane asiakasHaku = new GridPane();
        asiakasHaku.setPadding(new Insets(50,50,50,0));
        asiakasHaku.setHgap(100);
        asiakasHaku.setVgap(15);
        asiakaspaneeli.setTop(asiakasHaku);

        TextField asiakasHakuKentta = new TextField();
        Label asiakasHakuKenttaLabel = new Label("Hae asiakasta: ", asiakasHakuKentta);
        asiakasHakuKenttaLabel.setFont(fontti);
        asiakasHakuKenttaLabel.setContentDisplay(ContentDisplay.RIGHT);
        asiakasHaku.add(asiakasHakuKenttaLabel, 1, 1);
        Nappula asiakasHakuNappula = new Nappula("Suorita haku", 190, 30);
        asiakasHaku.add(asiakasHakuNappula, 1, 2);
        asiakasHaku.add(new Text("Näytä tulokset järjestyksessä"), 2, 0);

        ToggleGroup toggleasiakas = new ToggleGroup();

        RadioButton uusinasiakas = new RadioButton("uusin - vanhin");
        asiakasHaku.add(uusinasiakas, 2, 1);
        uusinasiakas.setToggleGroup(toggleasiakas);

        RadioButton vanhinasiakas = new RadioButton("vanhin - uusin");
        asiakasHaku.add(vanhinasiakas, 2, 2);
        vanhinasiakas.setToggleGroup(toggleasiakas);

        RadioButton aakkosasiakas = new RadioButton("A - Ö");
        asiakasHaku.add(aakkosasiakas, 2, 3);
        aakkosasiakas.setToggleGroup(toggleasiakas);


        ScrollPane asiakasScrollaus = new ScrollPane();
        asiakaspaneeli.setCenter(asiakasScrollaus);
        GridPane asiakasTaulukko = new GridPane();
        asiakasTaulukko.setPadding(new Insets(20,20,20,20));
        asiakasTaulukko.getColumnConstraints().addAll(lyhyt, kolumniLeveys, kolumniLeveys, semi, lyhyt);
        asiakasTaulukko.setGridLinesVisible(true);
        asiakasScrollaus.setContent(asiakasTaulukko);


        Nappula asiakasnLisays = new Nappula("Lisää uusi asiakas", 200, 30);
        asiakasTaulukko.add(asiakasnLisays, 1,0);

        Text asiakastunnusOtsikko = new Text("AsiakasID");
        asiakastunnusOtsikko.setFont(fontti);
        Text asiakasNimiOtsikko = new Text("Asiakas");
        asiakasNimiOtsikko.setFont(fontti);
        Text asiakasEmailOtsikko = new Text("Email");
        asiakasEmailOtsikko.setFont(fontti);
        Text asiakasPuhNroOtsikko = new Text("Puh.nro.");
        asiakasPuhNroOtsikko.setFont(fontti);


        asiakasTaulukko.add(asiakastunnusOtsikko, 0, 1);
        asiakasTaulukko.add(asiakasNimiOtsikko, 1, 1);
        asiakasTaulukko.add(asiakasEmailOtsikko, 2, 1);
        asiakasTaulukko.add(asiakasPuhNroOtsikko, 3, 1);

        ArrayList<Asiakas> asiakaslista = new ArrayList<Asiakas>();
        asiakaslista.add(new Asiakas(
                24, 34560, "Kukko", "Veikka",
                "veikka.kukko@gmail.com", "Savontie 26", "0440153888"));
        asiakaslista.add(new Asiakas(
                25, 34572, "Kukka", "Jukka",
                "jukka.kukka@gmail.com", "Savontie 27", "0504643299"));       //TEMP


        int asiakasLaskuri = 2;
        for (Asiakas obj : asiakaslista) {
            Text asiakasID = new Text(String.valueOf(obj.getAsiakasID()));
            asiakasID.setFont(fontti);
            Text asiakasNimi = new Text(obj.getEtunimi() +" "+ obj.getSukunimi());
            asiakasNimi.setFont(fontti);
            Text asiakasEmail = new Text(String.valueOf(obj.getEmail()));
            asiakasEmail.setFont(fontti);
            Text asiakasPuhNro = new Text(String.valueOf(obj.getPuhnro()));
            asiakasPuhNro.setFont(fontti);


            asiakasTaulukko.add(asiakasID, 0, asiakasLaskuri);
            asiakasTaulukko.add(asiakasNimi, 1, asiakasLaskuri);
            asiakasTaulukko.add(asiakasEmail, 2, asiakasLaskuri);
            asiakasTaulukko.add(asiakasPuhNro, 3, asiakasLaskuri);


            Nappula poistoNappula = new Nappula("Poista", 200, 30);
            asiakasTaulukko.add(poistoNappula, 4, asiakasLaskuri);
            poistoNappula.setOnMouseClicked(e -> {
                // poistaasiakas();                          //TODO  poistaasiakas() - metodin luominen
            });

            Nappula muokkausNappula = new Nappula("Muokkaa", 120, 30);
            asiakasTaulukko.add(muokkausNappula, 5, asiakasLaskuri);
            muokkausNappula.setOnMouseClicked(e -> {
                // muokkaaMokki();                          //TODO  muokkaamokki() - metodin luominen
            });
            Nappula tarkasteleNappula = new Nappula("Tarkastele tietoja", 170, 30);
            asiakasTaulukko.add(tarkasteleNappula, 6, asiakasLaskuri);
            tarkasteleNappula.setOnMouseClicked(e -> {
                // tarkasteleMokkia();                          //TODO  tarkasteleMokkia() - metodin luominen
            });

            asiakasLaskuri++;
        }
    }

    public void luoLaskunakyma() {
        ColumnConstraints kolumniLeveys = new ColumnConstraints();
        kolumniLeveys.setHalignment(HPos.CENTER);
        kolumniLeveys.setPrefWidth(200);

        ColumnConstraints semi = new ColumnConstraints();
        semi.setHalignment(HPos.CENTER);
        semi.setPrefWidth(140);

        ColumnConstraints lyhyt = new ColumnConstraints();
        lyhyt.setHalignment(HPos.CENTER);
        lyhyt.setPrefWidth(80);

        BorderPane laskupaneeli = new BorderPane();
        //paneeli.setCenter(laskupaneeli);
        laskunappula.setOnMouseClicked(e -> {
            paneeli.setCenter(laskupaneeli);
            isoOtsikkoTeksti.setText("LASKUT");
//            for (Nappula n : nappulat) {
//                n.deselect();
//            }
//            laskunappula.select();
        });

        GridPane laskuHaku = new GridPane();
        laskuHaku.setPadding(new Insets(50,50,50,0));
        laskuHaku.setHgap(100);
        laskuHaku.setVgap(15);
        laskupaneeli.setTop(laskuHaku);

        TextField laskuHakuKentta = new TextField();
        Label laskuHakuKenttaLabel = new Label("Hae laskuja: ", laskuHakuKentta);
        laskuHakuKenttaLabel.setFont(fontti);
        laskuHakuKenttaLabel.setContentDisplay(ContentDisplay.RIGHT);
        laskuHaku.add(laskuHakuKenttaLabel, 1, 1);
        Nappula laskuHakuNappula = new Nappula("Suorita haku", 190, 30);
        laskuHaku.add(laskuHakuNappula, 1, 2);
        laskuHaku.add(new Text("Näytä tulokset järjestyksessä"), 2, 0);

        ToggleGroup togglelasku = new ToggleGroup();

        RadioButton uusinlasku = new RadioButton("uusin - vanhin");
        laskuHaku.add(uusinlasku, 2, 1);
        uusinlasku.setToggleGroup(togglelasku);

        RadioButton vanhinlasku = new RadioButton("vanhin - uusin");
        laskuHaku.add(vanhinlasku, 2, 2);
        vanhinlasku.setToggleGroup(togglelasku);

        RadioButton aakkoslasku = new RadioButton("varaustunnuksen mukaan");
        laskuHaku.add(aakkoslasku, 2, 3);
        aakkoslasku.setToggleGroup(togglelasku);


        ScrollPane laskuScrollaus = new ScrollPane();
        laskupaneeli.setCenter(laskuScrollaus);
        GridPane laskuTaulukko = new GridPane();
        laskuTaulukko.setPadding(new Insets(20,20,20,20));
        laskuTaulukko.getColumnConstraints().addAll(lyhyt, kolumniLeveys, lyhyt, semi, lyhyt);
        laskuTaulukko.setGridLinesVisible(true);
        laskuScrollaus.setContent(laskuTaulukko);


        Nappula laskunLisays = new Nappula("Lisää uusi lasku", 200, 30);
        laskuTaulukko.add(laskunLisays, 1,0);

        Text laskutunnusOtsikko = new Text("Laskunro.");
        laskutunnusOtsikko.setFont(fontti);
        Text laskuVarausOtsikko = new Text("Varaus");
        laskuVarausOtsikko.setFont(fontti);
        Text laskuSummaOtsikko = new Text("Summa");
        laskuSummaOtsikko.setFont(fontti);
        Text laskuStatusOtsikko = new Text("Status");
        laskuStatusOtsikko.setFont(fontti);


        laskuTaulukko.add(laskutunnusOtsikko, 0, 1);
        laskuTaulukko.add(laskuVarausOtsikko, 1, 1);
        laskuTaulukko.add(laskuSummaOtsikko, 2, 1);
        laskuTaulukko.add(laskuStatusOtsikko, 3, 1);

        ArrayList<Lasku> laskulista = new ArrayList<Lasku>();
        laskulista.add(new Lasku(
                24, 345, 240, 14,
                "Maksettu"));
        laskulista.add(new Lasku(
                25, 346, 380, 14,
                "Maksamatta"));     //TEMP


        int laskuLaskuri = 2;
        for (Lasku obj : laskulista) {
            Text laskuID = new Text(String.valueOf(obj.getLaskuID()));
            laskuID.setFont(fontti);
            Text laskuVaraus = new Text(String.valueOf(obj.getVarausID()));
            laskuVaraus.setFont(fontti);
            Text laskuSumma = new Text(String.valueOf(obj.getLaskunSumma()));
            laskuSumma.setFont(fontti);
            Text laskuStatus = new Text(String.valueOf(obj.getLaskunStatus()));
            laskuStatus.setFont(fontti);


            laskuTaulukko.add(laskuID, 0, laskuLaskuri);
            laskuTaulukko.add(laskuVaraus, 1, laskuLaskuri);
            laskuTaulukko.add(laskuSumma, 2, laskuLaskuri);
            laskuTaulukko.add(laskuStatus, 3, laskuLaskuri);


            Nappula poistoNappula = new Nappula("Poista", 200, 30);
            laskuTaulukko.add(poistoNappula, 4, laskuLaskuri);
            poistoNappula.setOnMouseClicked(e -> {
                // poistalasku();                          //TODO  poistalasku() - metodin luominen
            });

            Nappula muokkausNappula = new Nappula("Muokkaa", 100, 30);
            laskuTaulukko.add(muokkausNappula, 5, laskuLaskuri);
            muokkausNappula.setOnMouseClicked(e -> {
                // muokkaaLasku();                          //TODO  muokkaamokki() - metodin luominen
            });

            Nappula tarkasteleNappula = new Nappula("Tarkastele tietoja", 160, 30);
            laskuTaulukko.add(tarkasteleNappula, 6, laskuLaskuri);
            tarkasteleNappula.setOnMouseClicked(e -> {
                // tarkasteleLasku();                          //TODO  tarkasteleMokkia() - metodin luominen
            });

            Nappula luoLaskuNappula = new Nappula("Vie tiedostoksi", 170, 30);
            laskuTaulukko.add(luoLaskuNappula, 7, laskuLaskuri);
            luoLaskuNappula.setOnMouseClicked(e -> {
                // luoLasku();                          //TODO  tarkasteleMokkia() - metodin luominen
            });

            laskuLaskuri++;
        }
    }

    public static void main(String[] args) {
        launch();
    }

    private Image imageKuvasta(String kuva) {
        try {
            return new Image(IMGPOLKU + kuva);
        } catch (Exception e) { // TODO tarkenna vain siihen tiettyyn Exceptioniin mikä tulee
            return new Image(kuva);
        }
    }
}