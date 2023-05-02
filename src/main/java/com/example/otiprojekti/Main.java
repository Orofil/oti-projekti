package com.example.otiprojekti;

import com.example.otiprojekti.nakymat.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


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
    private final ToggleNappula aluenappula = new ToggleNappula("Alueet");
    private final ToggleNappula mokkinappula = new ToggleNappula("Mökit");
    private final ToggleNappula palvelunappula = new ToggleNappula("Palvelut");
    private final ToggleNappula varausnappula = new ToggleNappula("Varaukset");
    private final ToggleNappula asiakasnappula = new ToggleNappula("Asiakkaat");
    private final ToggleNappula laskunappula = new ToggleNappula("Laskut");
    private final ToggleNappula[] nappulat = new ToggleNappula[] {
            aluenappula, mokkinappula, palvelunappula, varausnappula, asiakasnappula, laskunappula};
    private final ToggleGroup tgSivuvalikko = new ToggleGroup();

    private final BorderPane paneeli = new BorderPane();
    private final Text isoOtsikkoTeksti = new Text();

    /**
     * Fontti, jota käytetään tavalliseen tekstiin.
     */
    private static final Font fontti = Font.font(16);


    // Tietokantayhteys
    private final Tietokanta tietokanta = new Tietokanta(); // TODO jos tehdään nappi uudelleen yhdistämiseen niin sitten final pitää poistaa koska olio luodaan uudelleen

    ArrayList<Varaus> varauslista = null;


    @Override
    public void start(Stage ikkuna) {

        try {
            varauslista = tietokanta.haeVaraus();
        } catch (SQLException e) {
            // TODO miten käsitellään SQL exceptionit? Tehdäänkö joku ilmoitus joka tulee ikkunan nurkkaan jos virhe tapahtuu?
        }

        // Vasen valikko
        for (ToggleNappula n : nappulat) {
            n.setToggleGroup(tgSivuvalikko);
        }

        VBox painikkeet = new VBox();
        painikkeet.setPadding(new Insets(100,0,0,0));
        for (ToggleNappula n : nappulat) {
            painikkeet.getChildren().add(n);
        }

        paneeli.setLeft(painikkeet);
        paneeli.getLeft().setStyle("-fx-background-color: black");

        // Yläpalkki
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

        // Luodaan eri paneelit
        luoAluenakyma();
        luoMokkinakyma();
        luoPalvelunakyma();
        luoVarausnakyma();
        luoAsiakasnakyma();
        luoLaskunakyma();

        // Valitaan oletuksena aluepaneeli
        aluenappula.fire();


        // Lasketaan koko ikkunalle
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double boundsW = bounds.getWidth();
        double boundsH = bounds.getHeight();
        double boundsSuhdeMin = Math.min(boundsW / SUHDE_W, boundsH / SUHDE_H);
        double W = Math.min(boundsSuhdeMin * MAX_OSUUS * SUHDE_W, MAX_LEVEYS);
        double H = Math.min(boundsSuhdeMin * MAX_OSUUS * SUHDE_H, MAX_KORKEUS); // TODO tämä ei ihan toimi, ikkuna voi mennä yhä vähän liian isoksi

        Scene kehys = new Scene(paneeli, W, H);
        ikkuna.setScene(kehys);
        ikkuna.setMaxWidth(MAX_LEVEYS);
        ikkuna.setMaxHeight(MAX_KORKEUS);
        ikkuna.show();
    }

    public void luoAluenakyma() {
        ColumnConstraints kolumniLeveys = new ColumnConstraints();
        kolumniLeveys.setHalignment(HPos.CENTER);
        kolumniLeveys.setPrefWidth(200);

        ColumnConstraints lyhyt = new ColumnConstraints();
        lyhyt.setHalignment(HPos.CENTER);
        lyhyt.setPrefWidth(80);

        BorderPane aluepaneeli = new BorderPane();
        aluenappula.setOnAction(e -> {
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

        alueHaku.add(new Text("Lajittelu:"), 2, 0);
        ComboBox<String> alueLajittelu = new ComboBox<>(FXCollections.observableList(Arrays.asList(
                "Tunnuksen mukaan",
                "Uusin > Vanhin",
                "Vanhin > Uusin",
                "A > Ö"
        )));
        alueLajittelu.setValue("Uusin > Vanhin"); // Oletuksena valittu vaihtoehto, TODO mitkään näistä ei vielä tee mitään
        alueHaku.add(alueLajittelu, 2, 1);

        ScrollPane alueScrollaus = new ScrollPane();
        aluepaneeli.setCenter(alueScrollaus);
        GridPane alueTaulukko = new GridPane();
        alueTaulukko.setPadding(new Insets(20,20,20,20));
        alueTaulukko.getColumnConstraints().addAll(kolumniLeveys, kolumniLeveys, lyhyt);
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


        int rivi = 2;
        for (Alue obj : aluelista) {
            Text alueID = new Text(String.valueOf(obj.getAlueID()));
            alueID.setFont(fontti);
            Text alueNimi = new Text(String.valueOf(obj.getAlueenNimi()));
            alueNimi.setFont(fontti);
            alueTaulukko.add(alueID, 0, rivi);

            alueID.setTextAlignment(TextAlignment.CENTER);
            alueTaulukko.add(alueNimi, 1, rivi);
            //alueNimi.setAlignment(Pos.CENTER);
            alueNimi.setTextAlignment(TextAlignment.CENTER);

            Nappula poistoNappula = new Nappula("", 150, 30);
            ImageView roskis = new ImageView(imageKuvasta("roskis.png"));
            roskis.setFitWidth(22);
            roskis.setFitHeight(22);
            poistoNappula.setGraphic(roskis);
            alueTaulukko.add(poistoNappula, 2, rivi);
            poistoNappula.setOnMouseClicked(e -> {
                // poistaAlue();                          //TODO  poistaAlue() - metodin luominen
            });

            rivi++;
        }
    }
    
    public void luoMokkinakyma() {
        // TODO voisiko kolumnileveyden määritellä Main-luokan alussa jos sitä käytetään joka näkymässä, leveyden voisi sitten asettaa täällä erikseen
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
        mokkinappula.setOnAction(e -> {
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

        mokkiHaku.add(new Text("Lajittelu:"), 2, 0);
        ComboBox<String> mokkiLajittelu = new ComboBox<>(FXCollections.observableList(Arrays.asList(
                "Tunnuksen mukaan",
                "Edullisin > Kallein",
                "Kallein > Edullisin",
                "A > Ö",
                "Henkilömäärän mukaan",
                "Alueittain"
        )));
        mokkiLajittelu.setValue("Tunnuksen mukaan"); // Oletuksena valittu vaihtoehto
        mokkiHaku.add(mokkiLajittelu, 2, 1);

        ScrollPane mokkiScrollaus = new ScrollPane();
        mokkipaneeli.setCenter(mokkiScrollaus);
        GridPane mokkiTaulukko = new GridPane();
        mokkiTaulukko.setPadding(new Insets(20,20,20,20));
        mokkiTaulukko.getColumnConstraints().addAll(
                lyhyt, kolumniLeveys, lyhyt, lyhyt, lyhyt, lyhyt, lyhyt, lyhyt);
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


        ArrayList<Mokki> mokkilista = new ArrayList<>();
        mokkilista.add(new Mokki(1,1, 34110,"Sininen mökki", "Sinitie 2",
        BigDecimal.valueOf(200), "Valoisa hirsimökki koko perheelle tai pienelle kaveriporukalle saunalla ja porealtaalla.",
        6, "Sauna, poreallas"));       //TEMP
        mokkilista.add(new Mokki(2,1, 34100,"Punainen mökki", "Sinitie 3",
                BigDecimal.valueOf(250), "Viihtyisä ja tilava hirsimökki koko perheelle tai kaveriporukalle saunalla ja porealtaalla.",
                8, "Sauna, poreallas"));       //TEMP


        int rivi = 2;
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

            mokkiTaulukko.add(mokkiID, 0, rivi);
            mokkiTaulukko.add(mokkiNimi, 1, rivi);
            mokkiTaulukko.add(mokkiAlue, 2, rivi);
            mokkiTaulukko.add(mokkiHinta, 3, rivi);
            mokkiTaulukko.add(mokkiHloMaara, 4, rivi);

            Nappula poistoNappula = new Nappula("", 150, 30);
            ImageView roskis = new ImageView(imageKuvasta("roskis.png"));
            roskis.setFitWidth(22);
            roskis.setFitHeight(22);
            poistoNappula.setGraphic(roskis);
            mokkiTaulukko.add(poistoNappula, 5, rivi);
            poistoNappula.setOnMouseClicked(e -> {
                // poistamokki();                          //TODO  poistamokki() - metodin luominen
            });
            Nappula muokkausNappula = new Nappula("", 100, 30);
            ImageView muokkaus = new ImageView(imageKuvasta("muokkaus.png"));
            muokkaus.setFitWidth(23);
            muokkaus.setFitHeight(22);
            muokkausNappula.setGraphic(muokkaus);
            mokkiTaulukko.add(muokkausNappula, 6, rivi);
            muokkausNappula.setOnMouseClicked(e -> {
                // muokkaaMokki();                          //TODO  muokkaamokki() - metodin luominen
            });
            Nappula tarkasteleNappula = new Nappula("", 170, 30);
            ImageView tarkastelu = new ImageView(imageKuvasta("tarkastelu.png"));
            tarkastelu.setFitWidth(23);
            tarkastelu.setFitHeight(22);
            tarkasteleNappula.setGraphic(tarkastelu);
            mokkiTaulukko.add(tarkasteleNappula, 7, rivi);
            tarkasteleNappula.setOnMouseClicked(e -> {
                // tarkasteleMokkia();                          //TODO  tarkasteleMokkia() - metodin luominen
            });

            rivi++;
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
        palvelunappula.setOnAction(e -> {
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

        palveluHaku.add(new Text("Lajittelu:"), 2, 0);
        ComboBox<String> palveluLajittelu = new ComboBox<>(FXCollections.observableList(Arrays.asList(
                "Tunnuksen mukaan",
                "Uusin > Vanhin",
                "Vanhin > Uusin",
                "A > Ö",
                "Alueittain"
        )));
        palveluLajittelu.setValue("Uusin > Vanhin"); // Oletuksena valittu vaihtoehto
        palveluHaku.add(palveluLajittelu, 2, 1);

        ScrollPane palveluScrollaus = new ScrollPane();
        palvelupaneeli.setCenter(palveluScrollaus);
        GridPane palveluTaulukko = new GridPane();
        palveluTaulukko.setPadding(new Insets(20,20,20,20));
        palveluTaulukko.getColumnConstraints().addAll(semi, kolumniLeveys, lyhyt, lyhyt, lyhyt, lyhyt, lyhyt);
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

        ArrayList<Palvelu> palvelulista = new ArrayList<>();
        palvelulista.add(new Palvelu(1, 1, "Moottorikelkkavuokra",
                "Välinevuokraus", "Moottorikelkan vuokraus 1 hlö 3h",
                BigDecimal.valueOf(60), 24)); // TEMP
        palvelulista.add(new Palvelu(2, 1, "Kuumakivihieronta 60 min",
                "Hieronta", "Kuumakivihieronta 60 min koulutetulla hierojalla Levin elämyshoitolassa",
                BigDecimal.valueOf(70), 24)); // TEMP


        int rivi = 2;
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
            palveluTaulukko.add(palveluID, 0, rivi);
            palveluTaulukko.add(palveluNimi, 1, rivi);
            palveluTaulukko.add(palveluAlue, 2, rivi);
            palveluTaulukko.add(palveluHinta, 3, rivi);

            //palveluNimi.setAlignment(Pos.CENTER);
            palveluNimi.setTextAlignment(TextAlignment.CENTER);

            Nappula poistoNappula = new Nappula("", 150, 30);
            ImageView roskis = new ImageView(imageKuvasta("roskis.png"));
            roskis.setFitWidth(22);
            roskis.setFitHeight(22);
            poistoNappula.setGraphic(roskis);
            palveluTaulukko.add(poistoNappula, 4, rivi);
            poistoNappula.setOnMouseClicked(e -> {
                // poistapalvelu();                          //TODO  poistapalvelu() - metodin luominen
            });

            Nappula muokkausNappula = new Nappula("", 100, 30);
            ImageView muokkaus = new ImageView(imageKuvasta("muokkaus.png"));
            muokkaus.setFitWidth(23);
            muokkaus.setFitHeight(22);
            muokkausNappula.setGraphic(muokkaus);
            palveluTaulukko.add(muokkausNappula, 5, rivi);
            muokkausNappula.setOnMouseClicked(e -> {
                // muokkaaMokki();                          //TODO  muokkaamokki() - metodin luominen
            });
            Nappula tarkasteleNappula = new Nappula("", 170, 30);
            ImageView tarkastelu = new ImageView(imageKuvasta("tarkastelu.png"));
            tarkastelu.setFitWidth(23);
            tarkastelu.setFitHeight(22);
            tarkasteleNappula.setGraphic(tarkastelu);
            palveluTaulukko.add(tarkasteleNappula, 6, rivi);
            tarkasteleNappula.setOnMouseClicked(e -> {
                // tarkasteleMokkia();                          //TODO  tarkasteleMokkia() - metodin luominen
            });

            rivi++;
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
        varausnappula.setOnAction(e -> {
            paneeli.setCenter(varauspaneeli);
            isoOtsikkoTeksti.setText("VARAUKSET");
//            for (Nappula n : nappulat) {
//                n.deselect();
//            }
//            varausnappula.select();
        });

        GridPane varausHaku = new GridPane();
        varausHaku.setPadding(new Insets(20));
        varausHaku.setHgap(20);
        varausHaku.setVgap(15);
        varauspaneeli.setTop(varausHaku);

        TextField varausHakuKentta = new TextField();
        Label varausHakuKenttaLabel = new Label("Hae varausta: ", varausHakuKentta);
        varausHakuKenttaLabel.setFont(fontti);
        varausHakuKenttaLabel.setContentDisplay(ContentDisplay.RIGHT);
        varausHaku.add(varausHakuKenttaLabel, 1, 1);
        Nappula varausHakuNappula = new Nappula("Suorita haku", 190, 30);
        varausHaku.add(varausHakuNappula, 1, 2);

        varausHaku.add(new Text("Lajittelu:"), 2, 0);
        ComboBox<String> varausLajittelu = new ComboBox<>(FXCollections.observableList(Arrays.asList(
                "Tunnuksen mukaan",
                "Uusin > Vanhin",
                "Vanhin > Uusin",
                "A > Ö",
                "Alueittain"
        )));
        varausLajittelu.setValue("Uusin > Vanhin"); // Oletuksena valittu vaihtoehto
        varausHaku.add(varausLajittelu, 2, 1);

        varausHaku.add(new Text("Suodata päivämäärän mukaan:"), 3, 0);
        varausHaku.add(new Text("Alkupäivä:"), 3, 1);
        varausHaku.add(new Text("Loppupäivä:"), 3, 2);
        DatePicker varausPvmAlku = new DatePicker();
        varausHaku.add(varausPvmAlku, 4, 1);
        DatePicker varausPvmLoppu = new DatePicker();
        varausHaku.add(varausPvmLoppu, 4, 2);

        varausPvmAlku.setOnAction(e -> { // TEMP
            System.out.println(varausPvmAlku.getValue()); // Palauttaa LocalDate-olion
        });
        varausPvmLoppu.setOnAction(e -> { // TEMP
            System.out.println(varausPvmLoppu.getValue());
        });

        ScrollPane varausScrollaus = new ScrollPane();
        varauspaneeli.setCenter(varausScrollaus);
        GridPane varausTaulukko = new GridPane();
        varausTaulukko.setPadding(new Insets(20, 20, 20, 20));
        varausTaulukko.getColumnConstraints().addAll(semi, kolumniLeveys, lyhyt, lyhyt, lyhyt, lyhyt);
        varausTaulukko.setGridLinesVisible(true);
        varausScrollaus.setContent(varausTaulukko);


        Nappula varausLisays = new Nappula("Lisää uusi varaus", 200, 30);
        varausTaulukko.add(varausLisays, 1, 0);
        varausLisays.setOnAction(e -> {
            Stage varausLisaysStage = new Stage();

            VBox varausLisaysVBoxPaneeli = new VBox();
            varausLisaysVBoxPaneeli.setPadding(new Insets(30,30,30,30));
            varausLisaysVBoxPaneeli.setSpacing(10);

            varausLisaysVBoxPaneeli.getChildren().add(new Text("Onko kyseessä uusi asiakas?"));
            RadioButton uusiAsiakas = new RadioButton("Kyllä");
            RadioButton vanhaAsiakas = new RadioButton("Ei");
            ToggleGroup asiakas = new ToggleGroup();
            uusiAsiakas.setToggleGroup(asiakas);
            vanhaAsiakas.setToggleGroup(asiakas);



            GridPane asiakasLisaysPaneeli = new GridPane();
            asiakasLisaysPaneeli.setVgap(5);
            asiakasLisaysPaneeli.add(new Text("Syötä asiakkaan tiedot."), 0, 0);
            TextField enimi = new TextField();
            TextField snimi = new TextField();
            TextField email = new TextField();
            TextField puhnro = new TextField();
            TextField lahiosoite = new TextField();
            TextField postinro = new TextField();

            Text enimiText = new Text("Etunimi");
            Text snimiText = new Text("Sukunimi");
            Text emailText = new Text("Sähköpostiosoite");
            Text puhnroText = new Text("Puhelinnumero");
            Text lahiosoiteText = new Text("Lähiosoite");
            Text postinroText = new Text("Postinumero");

            asiakasLisaysPaneeli.add(enimiText, 0,1);
            asiakasLisaysPaneeli.add(enimi, 1,1);
            asiakasLisaysPaneeli.add(snimiText, 0,2);
            asiakasLisaysPaneeli.add(snimi, 1,2);
            asiakasLisaysPaneeli.add(emailText, 0,3);
            asiakasLisaysPaneeli.add(email, 1,3);
            asiakasLisaysPaneeli.add(puhnroText, 0,4);
            asiakasLisaysPaneeli.add(puhnro, 1,4);
            asiakasLisaysPaneeli.add(lahiosoiteText, 0,5);
            asiakasLisaysPaneeli.add(lahiosoite, 1,5);
            asiakasLisaysPaneeli.add(postinroText, 0,6);
            asiakasLisaysPaneeli.add(postinro, 1,6);


            GridPane varausLisaysPaneeli = new GridPane();
            varausLisaysPaneeli.setVgap(5);
            varausLisaysPaneeli.add(new Text("Jos kyseessä on vanha asiakas, syötä asiakasID."), 0, 0);
            TextField asiakasID = new TextField();
            TextField mokkiID = new TextField();
            DatePicker aloitusPvm = new DatePicker();
            TextField aloitusAika = new TextField("16:00");
            DatePicker lopetusPvm = new DatePicker();
            TextField lopetusAika = new TextField("12:00");
            

            Text asiakasIDText = new Text("AsiakasID");
            Text mokkiIDText = new Text("MökkiID");
            Text aloitusPvmText = new Text("Aloituspäivämäärä");
            Text aloitusAikaText = new Text("ja kellonaika");
            Text lopetusPvmText = new Text("Lopetuspäivämäärä");
            Text lopetusAikaText = new Text("ja kellonaika");

            varausLisaysPaneeli.add(asiakasIDText, 0, 1);
            varausLisaysPaneeli.add(asiakasID, 1, 1);
            varausLisaysPaneeli.add(mokkiIDText, 0, 2);
            varausLisaysPaneeli.add(mokkiID, 1, 2);
            varausLisaysPaneeli.add(aloitusPvmText, 0, 3);
            varausLisaysPaneeli.add(aloitusPvm, 1, 3);
            varausLisaysPaneeli.add(aloitusAikaText, 0, 4);
            varausLisaysPaneeli.add(aloitusAika, 1, 4);
            varausLisaysPaneeli.add(lopetusPvmText, 0, 5);
            varausLisaysPaneeli.add(lopetusPvm, 1, 5);
            varausLisaysPaneeli.add(lopetusAikaText, 0, 6);
            varausLisaysPaneeli.add(lopetusAika, 1, 6);



            uusiAsiakas.setOnAction( event -> {
                asiakasLisaysPaneeli.setVisible(true);
            });
            vanhaAsiakas.setOnAction( event -> {
                asiakasLisaysPaneeli.setVisible(false);
            });

            Nappula lisaaVaraus = new Nappula("Aseta varaus");
            lisaaVaraus.setOnAction( event -> {
                if (uusiAsiakas.isSelected()) {
                    //tietokanta.insertAsiakas(Integer.parseInt(postinro.getText()), enimi);
                }
            });

            varausLisaysVBoxPaneeli.getChildren().addAll
                    (uusiAsiakas, vanhaAsiakas, asiakasLisaysPaneeli, varausLisaysPaneeli);

            //varausLisaysStage.initModality(Modality.WINDOW_MODAL);
            //varausLisaysStage.initOwner(ikkuna);
            /*
            Text text2 = new Text("Tämä on pienempi ikkuna!");
            Button button2 = new Button("Sulje ikkuna");
            VBox vb2 = new VBox(text2, button2);
            vb2.setSpacing(15);
            vb2.setAlignment(Pos.CENTER);

            // Muokataan alempaa ikkunaa
            text1.setText("Pienempi ikkuna on nyt auki");

            // Suljetaan ikkuna
            button2.setOnAction(e1 -> {
                stage2.close();
                text1.setText("Tämä on iso ikkuna!");
            });


             */
            // Luodaan uusi scene
            Scene scene2 = new Scene(varausLisaysVBoxPaneeli, 400, 650);
            varausLisaysStage.setScene(scene2);
            varausLisaysStage.setTitle("Lisää varaus");
            varausLisaysStage.show();
        });

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

//        ArrayList<Varaus> varauslista = new ArrayList<Varaus>();
//        varauslista.add(new Varaus(123, 122, 234,
//                LocalDateTime.of(2022, 9, 30, 12, 50),
//                LocalDateTime.of(2022, 9, 30, 12, 52 ),
//                LocalDateTime.of(2022, 10, 8, 15, 30),
//                LocalDateTime.of(2022, 10, 10, 12, 0 )));
//        varauslista.add(new Varaus(13, 12, 24,
//                LocalDateTime.of(2023, 1, 15, 13, 50),
//                LocalDateTime.of(2023, 1, 15, 14, 52 ),
//                LocalDateTime.of(2023, 3, 6, 15, 30),
//                LocalDateTime.of(2023, 3, 12, 12, 0 )));        //TEMP



        int rivi = 2;
        for (Varaus obj : varauslista) { // TODO nyt kun SQL-haku saattaa epäonnistua, voi tulla NullPointerException
            Text varausID = new Text(String.valueOf(obj.getVarausID()));
            varausID.setFont(fontti);
            Text varausNimi = new Text(String.valueOf(obj.getAsiakasID()));
            varausNimi.setFont(fontti);
            Text varausMokki = new Text(String.valueOf(obj.getMokkiID()));
            varausMokki.setFont(fontti);


            varausID.setTextAlignment(TextAlignment.CENTER);
            varausTaulukko.add(varausID, 0, rivi);
            varausTaulukko.add(varausNimi, 1, rivi);
            varausTaulukko.add(varausMokki, 2, rivi);

            //varausNimi.setAlignment(Pos.CENTER);
            varausNimi.setTextAlignment(TextAlignment.CENTER);

            Nappula poistoNappula = new Nappula("", 150, 30);
            ImageView roskis = new ImageView(imageKuvasta("roskis.png"));
            roskis.setFitWidth(22);
            roskis.setFitHeight(22);
            poistoNappula.setGraphic(roskis);
            varausTaulukko.add(poistoNappula, 3, rivi);
            poistoNappula.setOnMouseClicked(e -> {
                // poistavaraus();                          //TODO  poistavaraus() - metodin luominen
            });

            Nappula muokkausNappula = new Nappula("", 100, 30);
            ImageView muokkaus = new ImageView(imageKuvasta("muokkaus.png"));
            muokkaus.setFitWidth(23);
            muokkaus.setFitHeight(22);
            muokkausNappula.setGraphic(muokkaus);
            varausTaulukko.add(muokkausNappula, 4, rivi);
            muokkausNappula.setOnMouseClicked(e -> {
                // muokkaaMokki();                          //TODO  muokkaamokki() - metodin luominen
            });
            Nappula tarkasteleNappula = new Nappula("", 170, 30);
            ImageView tarkastelu = new ImageView(imageKuvasta("tarkastelu.png"));
            tarkastelu.setFitWidth(23);
            tarkastelu.setFitHeight(22);
            tarkasteleNappula.setGraphic(tarkastelu);
            varausTaulukko.add(tarkasteleNappula, 5, rivi);
            tarkasteleNappula.setOnMouseClicked(e -> {
                // tarkasteleMokkia();                          //TODO  tarkasteleMokkia() - metodin luominen
            });

            rivi++;
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
        asiakasnappula.setOnAction(e -> {
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

        asiakasHaku.add(new Text("Lajittelu:"), 2, 0);
        ComboBox<String> asiakasLajittelu = new ComboBox<>(FXCollections.observableList(Arrays.asList(
                "Tunnuksen mukaan",
                "Uusin > Vanhin",
                "Vanhin > Uusin",
                "A > Ö"
        )));
        asiakasLajittelu.setValue("Uusin > Vanhin"); // Oletuksena valittu vaihtoehto
        asiakasHaku.add(asiakasLajittelu, 2, 1);

        ScrollPane asiakasScrollaus = new ScrollPane();
        asiakaspaneeli.setCenter(asiakasScrollaus);
        GridPane asiakasTaulukko = new GridPane();
        asiakasTaulukko.setPadding(new Insets(20,20,20,20));
        asiakasTaulukko.getColumnConstraints().addAll(lyhyt, kolumniLeveys, kolumniLeveys, semi, lyhyt, lyhyt, lyhyt);
        asiakasTaulukko.setGridLinesVisible(true);
        asiakasScrollaus.setContent(asiakasTaulukko);


        Nappula asiakasLisays = new Nappula("Lisää uusi asiakas", 200, 30);
        asiakasTaulukko.add(asiakasLisays, 1,0);

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


        int rivi = 2;
        for (Asiakas obj : asiakaslista) {
            Text asiakasID = new Text(String.valueOf(obj.getAsiakasID()));
            asiakasID.setFont(fontti);
            Text asiakasNimi = new Text(obj.getEtunimi() +" "+ obj.getSukunimi());
            asiakasNimi.setFont(fontti);
            Text asiakasEmail = new Text(String.valueOf(obj.getEmail()));
            asiakasEmail.setFont(fontti);
            Text asiakasPuhNro = new Text(String.valueOf(obj.getPuhnro()));
            asiakasPuhNro.setFont(fontti);


            asiakasTaulukko.add(asiakasID, 0, rivi);
            asiakasTaulukko.add(asiakasNimi, 1, rivi);
            asiakasTaulukko.add(asiakasEmail, 2, rivi);
            asiakasTaulukko.add(asiakasPuhNro, 3, rivi);


            Nappula poistoNappula = new Nappula("", 150, 30);
            ImageView roskis = new ImageView(imageKuvasta("roskis.png"));
            roskis.setFitWidth(22);
            roskis.setFitHeight(22);
            poistoNappula.setGraphic(roskis);
            asiakasTaulukko.add(poistoNappula, 4, rivi);
            poistoNappula.setOnMouseClicked(e -> {
                // poistaasiakas();                          //TODO  poistaasiakas() - metodin luominen
            });

            Nappula muokkausNappula = new Nappula("", 100, 30);
            ImageView muokkaus = new ImageView(imageKuvasta("muokkaus.png"));
            muokkaus.setFitWidth(23);
            muokkaus.setFitHeight(22);
            muokkausNappula.setGraphic(muokkaus);
            asiakasTaulukko.add(muokkausNappula, 5, rivi);
            muokkausNappula.setOnMouseClicked(e -> {
                // muokkaaMokki();                          //TODO  muokkaamokki() - metodin luominen
            });
            Nappula tarkasteleNappula = new Nappula("", 170, 30);
            ImageView tarkastelu = new ImageView(imageKuvasta("tarkastelu.png"));
            tarkastelu.setFitWidth(23);
            tarkastelu.setFitHeight(22);
            tarkasteleNappula.setGraphic(tarkastelu);
            asiakasTaulukko.add(tarkasteleNappula, 6, rivi);
            tarkasteleNappula.setOnMouseClicked(e -> {
                // tarkasteleMokkia();                          //TODO  tarkasteleMokkia() - metodin luominen
            });

            rivi++;
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
        laskunappula.setOnAction(e -> {
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

        laskuHaku.add(new Text("Lajittelu:"), 2, 0);
        ComboBox<String> laskuLajittelu = new ComboBox<>(FXCollections.observableList(Arrays.asList(
                "Tunnuksen mukaan",
                "Uusin > Vanhin",
                "Vanhin > Uusin",
                "Varaustunnuksen mukaan"
        )));
        laskuLajittelu.setValue("Uusin > Vanhin"); // Oletuksena valittu vaihtoehto
        laskuHaku.add(laskuLajittelu, 2, 1);

        ScrollPane laskuScrollaus = new ScrollPane();
        laskupaneeli.setCenter(laskuScrollaus);
        GridPane laskuTaulukko = new GridPane();
        laskuTaulukko.setPadding(new Insets(20,20,20,20));
        laskuTaulukko.getColumnConstraints().addAll(lyhyt, kolumniLeveys, lyhyt, semi, lyhyt, lyhyt, lyhyt, lyhyt);
        laskuTaulukko.setGridLinesVisible(true);
        laskuScrollaus.setContent(laskuTaulukko);


        Nappula laskunLisays = new Nappula("Lisää uusi lasku", 200, 30);
        laskuTaulukko.add(laskunLisays, 1,0);

        Text laskuTunnusOtsikko = new Text("Laskunro.");
        laskuTunnusOtsikko.setFont(fontti);
        Text laskuVarausOtsikko = new Text("Varaus");
        laskuVarausOtsikko.setFont(fontti);
        Text laskuSummaOtsikko = new Text("Summa");
        laskuSummaOtsikko.setFont(fontti);
        Text laskuStatusOtsikko = new Text("Status");
        laskuStatusOtsikko.setFont(fontti);


        laskuTaulukko.add(laskuTunnusOtsikko, 0, 1);
        laskuTaulukko.add(laskuVarausOtsikko, 1, 1);
        laskuTaulukko.add(laskuSummaOtsikko, 2, 1);
        laskuTaulukko.add(laskuStatusOtsikko, 3, 1);

        ArrayList<Lasku> laskulista = new ArrayList<>();
        laskulista.add(new Lasku(
                24, 345, BigDecimal.valueOf(240), 14,
                "Maksettu"));
        laskulista.add(new Lasku(
                25, 346, BigDecimal.valueOf(380), 14,
                "Maksamatta"));     //TEMP


        int rivi = 2;
        for (Lasku obj : laskulista) {
            Text laskuID = new Text(String.valueOf(obj.getLaskuID()));
            laskuID.setFont(fontti);
            Text laskuVaraus = new Text(String.valueOf(obj.getVarausID()));
            laskuVaraus.setFont(fontti);
            Text laskuSumma = new Text(String.valueOf(obj.getLaskunSumma()));
            laskuSumma.setFont(fontti);
            Text laskuStatus = new Text(String.valueOf(obj.getLaskunStatus()));
            laskuStatus.setFont(fontti);


            laskuTaulukko.add(laskuID, 0, rivi);
            laskuTaulukko.add(laskuVaraus, 1, rivi);
            laskuTaulukko.add(laskuSumma, 2, rivi);
            laskuTaulukko.add(laskuStatus, 3, rivi);


            Nappula poistoNappula = new Nappula("", 150, 30);
            ImageView roskis = new ImageView(imageKuvasta("roskis.png"));
            roskis.setFitWidth(22);
            roskis.setFitHeight(22);
            poistoNappula.setGraphic(roskis);
            laskuTaulukko.add(poistoNappula, 4, rivi);
            poistoNappula.setOnMouseClicked(e -> {
                // poistalasku();                          //TODO  poistalasku() - metodin luominen
            });

            Nappula muokkausNappula = new Nappula("", 100, 30);
            ImageView muokkaus = new ImageView(imageKuvasta("muokkaus.png"));
            muokkaus.setFitWidth(23);
            muokkaus.setFitHeight(22);
            muokkausNappula.setGraphic(muokkaus);
            laskuTaulukko.add(muokkausNappula, 5, rivi);
            muokkausNappula.setOnMouseClicked(e -> {
                // muokkaaLasku();                          //TODO  muokkaamokki() - metodin luominen
            });

            Nappula tarkasteleNappula = new Nappula("", 170, 30);
            ImageView tarkastelu = new ImageView(imageKuvasta("tarkastelu.png"));
            tarkastelu.setFitWidth(23);
            tarkastelu.setFitHeight(22);
            tarkasteleNappula.setGraphic(tarkastelu);
            laskuTaulukko.add(tarkasteleNappula, 6, rivi);
            tarkasteleNappula.setOnMouseClicked(e -> {
                // tarkasteleLasku();                          //TODO  tarkasteleMokkia() - metodin luominen
            });

            Nappula luoLaskuNappula = new Nappula("", 150, 30);
            ImageView tiedostoksi = new ImageView(imageKuvasta("tiedostoksi.png"));
            tiedostoksi.setFitWidth(33);
            tiedostoksi.setFitHeight(22);
            luoLaskuNappula.setGraphic(tiedostoksi);
            laskuTaulukko.add(luoLaskuNappula, 7, rivi);
            luoLaskuNappula.setOnMouseClicked(e -> {
                // luoLasku();                          //TODO  tarkasteleMokkia() - metodin luominen
            });

            rivi++;
        }
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Avaa resources-kansiossa sijaitsevan kuvan JavaFX:n {@link Image Image}-luokan oliona.
     * @param kuva Kuvan nimi
     * @return Image
     */
    private Image imageKuvasta(String kuva) {
        try {
            return new Image(IMGPOLKU + kuva);
        } catch (Exception e) { // TODO tarkenna vain siihen tiettyyn Exceptioniin mikä tulee
            return new Image(kuva);
        }
    }
}