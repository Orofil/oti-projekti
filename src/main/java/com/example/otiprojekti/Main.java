package com.example.otiprojekti;

import com.example.otiprojekti.ilmoitukset.IlmoitusPaneeli;
import com.example.otiprojekti.ilmoitukset.IlmoitusTyyppi;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.otiprojekti.Utils.imageKuvasta;


public class Main extends Application {


    // Ikkunan mittasuhde jaettuna kahteen kenttään
    private final double SUHDE_W = 5.5;
    private final double SUHDE_H = 3;
    // Kuinka suuren osan näytön leveydestä tai korkeudesta ikkuna vie enintään
    private final double MAX_OSUUS = 0.9;
    // Ikkunan suurin sallittu koko
    private final int MAX_LEVEYS = 1600; // TODO Näiden suhde ei ole sama kuin tuo oletussuhde mikä on vähän outoa
    private final int MAX_KORKEUS = 800;

    /**
     * Fontti, jota käytetään tavalliseen tekstiin.
     */
    private static final Font fontti = Font.font(16);
    private static final int HAKU_PADDING = 20;
    private static final int HAKU_HGAP = 20;
    private static final int HAKU_VGAP = 15;

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
    private final IlmoitusPaneeli ilmoitusPaneeli = new IlmoitusPaneeli();
    private final Pane ilmoitusPaneeliPaneeli = new Pane(ilmoitusPaneeli);
    private final StackPane paneeliYlin = new StackPane(paneeli, ilmoitusPaneeliPaneeli);
    private final Text isoOtsikkoTeksti = new Text();

    // Taulukon sarakkeet
    ColumnConstraints sarakeLevea = new ColumnConstraints();
    ColumnConstraints sarakeSemi = new ColumnConstraints();
    ColumnConstraints sarakeLyhyt = new ColumnConstraints();


    // Tietokantayhteys
    private final Tietokanta tietokanta = new Tietokanta(); // TODO jos tehdään vaikka nappi uudelleen yhdistämiseen niin sitten final pitää poistaa koska olio luodaan uudelleen

    /**
     * SQL:n käyttämä muotoilu DateTime-tietotyypeissä
     */
    // TODO sama on Tietokanta.javassa, kannattaisi olla vain toisessa ehkä
    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ArrayList<Posti> postiLista = new ArrayList<>();
    ArrayList<Alue> alueLista = new ArrayList<>();
    ArrayList<Mokki> mokkiLista = new ArrayList<>();
    ArrayList<Asiakas> asiakasLista = new ArrayList<>();
    ArrayList<Palvelu> palveluLista = new ArrayList<>();
    ArrayList<Varaus> varausLista = new ArrayList<>();
    ArrayList<Lasku> laskuLista = new ArrayList<>();

    GridPane varausTaulukko = new GridPane();
    Nappula varausLisaysNappula = new Nappula(200, 30);

    @Override
    public void start(Stage ikkuna) {

        // Haetaan tiedot tietokannasta olioiksi
        try {
            // Tiedot täytyy hakea tietyssä järjestyksessä, koska osa luokista viittaa toisiinsa
            postiLista = tietokanta.haePosti();
            alueLista = tietokanta.haeAlue();
            asiakasLista = tietokanta.haeAsiakas(postiLista);
            mokkiLista = tietokanta.haeMokki(alueLista, postiLista);
            palveluLista = tietokanta.haePalvelu(alueLista);
            varausLista = tietokanta.haeVaraus(asiakasLista, mokkiLista);
            laskuLista = tietokanta.haeLasku(varausLista);
        } catch (SQLException e) {
            ilmoitusPaneeli.lisaaIlmoitus(IlmoitusTyyppi.VAROITUS, String.valueOf(e));
            throw new RuntimeException(e); // TEMP
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
        logo.setPreserveRatio(true);
        logo.setFitWidth(100);

        Pane ylapalkkipaneeli = new Pane();
        ylapalkkipaneeli.getChildren().add(logo);
        ylapalkkipaneeli.setBackground
                (new Background(new BackgroundFill(Color.DARKSEAGREEN, null, null)));
        logo.setX(63);
        logo.setY(10);
        ylapalkkipaneeli.setMinHeight(110);
        paneeli.setTop(ylapalkkipaneeli);

        isoOtsikkoTeksti.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));
        isoOtsikkoTeksti.setFill(Color.DARKGREEN);

        ylapalkkipaneeli.getChildren().add(isoOtsikkoTeksti);
        isoOtsikkoTeksti.setX(318);
        isoOtsikkoTeksti.setY(85);

        // Ilmoituspaneeli
        ilmoitusPaneeliPaneeli.setMouseTransparent(true); // TODO tämä pitää tehdä jos haluaa käyttää ilmoitusten alla olevaa ohjelmaa, mutta nyt ilmoitukset eivät jää pidemmäksi aikaa jos kursoria pitää päällä
        // TODO ilmoituspaneeli pitäisi saada oikeaan yläkulmaan
        ilmoitusPaneeli.lisaaIlmoitus(IlmoitusTyyppi.ILMOITUS, "Tämä on testi!"); // TEMP


        //aluepaneeli.setTop(new Nappula("Paina tästä!")); // TEMP

        // Sarakkeet taulukkoihin
        sarakeLevea.setHalignment(HPos.CENTER);
        sarakeLevea.setPrefWidth(200);
        sarakeSemi.setHalignment(HPos.CENTER);
        sarakeSemi.setPrefWidth(120);
        sarakeLyhyt.setHalignment(HPos.CENTER);
        sarakeLyhyt.setPrefWidth(80);

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

        Scene kehys = new Scene(paneeliYlin, W, H);
        ikkuna.setScene(kehys);
        ikkuna.setMaxWidth(MAX_LEVEYS);
        ikkuna.setMaxHeight(MAX_KORKEUS);
        ikkuna.show();
    }

    public void luoAluenakyma() {
        BorderPane aluepaneeli = new BorderPane();
        aluenappula.setOnAction(e -> {
            paneeli.setCenter(aluepaneeli);
            isoOtsikkoTeksti.setText("ALUEET");
            ilmoitusPaneeli.lisaaIlmoitus(IlmoitusTyyppi.ILMOITUS,
                    "Alueet valittu! Vähän lisää tekstiä tähän vielä ihan testiksi."); // TEMP
        });

        GridPane alueHaku = new GridPane();
        alueHaku.setPadding(new Insets(HAKU_PADDING));
        alueHaku.setHgap(HAKU_HGAP);
        alueHaku.setVgap(HAKU_VGAP);
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
        alueLajittelu.setValue("Uusin > Vanhin"); // Oletuksena valittu vaihtoehto
        alueHaku.add(alueLajittelu, 2, 1);

        // Lajittelu
        alueLajittelu.setOnAction(e -> {
            switch (alueLajittelu.getValue()) {
                case "Tunnuksen mukaan" ->
                    alueLista.sort(Comparator.comparing(Alue::getID));
                case "Uusin > Vanhin" -> {} // TODO miten tämä toimii?
                case "Vanhin > Uusin" -> {} // TODO miten tämä toimii?
                case "A > Ö" ->
                    alueLista.sort(Comparator.comparing(Alue::getNimi));
            }
            // TODO päivitä näkymä
        });

        ScrollPane alueScrollaus = new ScrollPane();
        aluepaneeli.setCenter(alueScrollaus);
        GridPane alueTaulukko = new GridPane();
        alueTaulukko.setPadding(new Insets(20));
        alueTaulukko.getColumnConstraints().addAll(sarakeLevea, sarakeLevea, sarakeLyhyt);
        alueTaulukko.setGridLinesVisible(true);
        alueScrollaus.setContent(alueTaulukko);


        Nappula alueenLisaysNappula = new Nappula(200, 30);
        ImageView alueenLisays = new ImageView(imageKuvasta("lisays.png"));
        alueenLisays.setFitWidth(23);
        alueenLisays.setFitHeight(22);
        alueenLisaysNappula.setGraphic(alueenLisays);
        alueTaulukko.add(alueenLisaysNappula, 1,0);

        Text aluetunnusOtsikko = new Text("Aluetunnus");
        aluetunnusOtsikko.setFont(fontti);
        Text alueennimiOtsikko = new Text("Alueen nimi");
        alueennimiOtsikko.setFont(fontti);
        alueTaulukko.add(aluetunnusOtsikko, 0, 1);
        alueTaulukko.add(alueennimiOtsikko, 1, 1);


        alueLista.add(new Alue(1, "Ylläs"));       //TEMP
        alueLista.add(new Alue(2, "Levi"));        //TEMP


        int rivi = 2;
        for (Alue obj : alueLista) {
            Text alueID = new Text(String.valueOf(obj.getID()));
            alueID.setFont(fontti);
            Text alueNimi = new Text(String.valueOf(obj.getNimi()));
            alueNimi.setFont(fontti);
            alueTaulukko.add(alueID, 0, rivi);

            alueID.setTextAlignment(TextAlignment.CENTER);
            alueTaulukko.add(alueNimi, 1, rivi);
            //alueNimi.setAlignment(Pos.CENTER);
            alueNimi.setTextAlignment(TextAlignment.CENTER);

            Nappula poistoNappula = new Nappula(150, 30);
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
        BorderPane mokkipaneeli = new BorderPane();
        mokkinappula.setOnAction(e -> {
            paneeli.setCenter(mokkipaneeli);
            isoOtsikkoTeksti.setText("MÖKIT");
        });

        GridPane mokkiHaku = new GridPane();
        mokkiHaku.setPadding(new Insets(HAKU_PADDING));
        mokkiHaku.setHgap(HAKU_HGAP);
        mokkiHaku.setVgap(HAKU_VGAP);
        mokkipaneeli.setTop(mokkiHaku);

        TextField mokkiHakuKentta = new TextField();
        Label mokkiHakuKenttaLabel = new Label("Hae mökkiä: ", mokkiHakuKentta);
        mokkiHakuKenttaLabel.setFont(fontti);
        mokkiHakuKenttaLabel.setContentDisplay(ContentDisplay.RIGHT);
        mokkiHaku.add(mokkiHakuKenttaLabel, 1, 1);
        Nappula mokkiHakuNappula = new Nappula("Suorita haku", 190, 30);
        // TODO päivitä näkymä
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

        // Lajittelu
        mokkiLajittelu.setOnAction(e -> {
            switch (mokkiLajittelu.getValue()) {
                case "Tunnuksen mukaan" ->
                        mokkiLista.sort(Comparator.comparing(Mokki::getID));
                case "Edullisin > Kallein" -> 
                        mokkiLista.sort(Comparator.comparing(Mokki::getHinta));
                case "Kallein > Edullisin" -> 
                        mokkiLista.sort(Comparator.comparing(Mokki::getHinta).reversed());
                case "A > Ö" ->
                        mokkiLista.sort(Comparator.comparing(Mokki::getNimi));
                case "Henkilömäärän mukaan" -> 
                        mokkiLista.sort(Comparator.comparing(Mokki::getHloMaara));
                case "Alueittain" -> 
                        mokkiLista.sort(Comparator.comparing(Mokki -> Mokki.getAlue().getID())); // TODO onko alueen ID:n vai nimen mukaan
            }
        });

        ScrollPane mokkiScrollaus = new ScrollPane();
        mokkipaneeli.setCenter(mokkiScrollaus);
        GridPane mokkiTaulukko = new GridPane();
        mokkiTaulukko.setPadding(new Insets(20));
        mokkiTaulukko.getColumnConstraints().addAll(
                sarakeLyhyt, sarakeLevea, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt);
        mokkiTaulukko.setGridLinesVisible(true);
        mokkiScrollaus.setContent(mokkiTaulukko);


        Nappula mokkienLisaysNappula = new Nappula(200, 30);
        ImageView mokkienLisays = new ImageView(imageKuvasta("lisays.png"));
        mokkienLisays.setFitWidth(23);
        mokkienLisays.setFitHeight(22);
        mokkienLisaysNappula.setGraphic(mokkienLisays);
        mokkiTaulukko.add(mokkienLisaysNappula, 1,0);

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


        int rivi = 2;
        for (Mokki obj : mokkiLista) {
            Text mokkiID = new Text(String.valueOf(obj.getID()));
            mokkiID.setFont(fontti);
            Text mokkiNimi = new Text(String.valueOf(obj.getNimi()));
            mokkiNimi.setFont(fontti);
            Text mokkiAlue = new Text(String.valueOf(obj.getAlue().getNimi()));
            mokkiAlue.setFont(fontti);
            Text mokkiHinta = new Text(obj.getHinta() + " €"); // TODO lisätäänkö €
            mokkiHinta.setFont(fontti);
            Text mokkiHloMaara = new Text(String.valueOf(obj.getHloMaara()));
            mokkiHloMaara.setFont(fontti);

            mokkiTaulukko.add(mokkiID, 0, rivi);
            mokkiTaulukko.add(mokkiNimi, 1, rivi);
            mokkiTaulukko.add(mokkiAlue, 2, rivi);
            mokkiTaulukko.add(mokkiHinta, 3, rivi);
            mokkiTaulukko.add(mokkiHloMaara, 4, rivi);

            Nappula poistoNappula = new Nappula(150, 30);
            ImageView roskis = new ImageView(imageKuvasta("roskis.png"));
            roskis.setFitWidth(22);
            roskis.setFitHeight(22);
            poistoNappula.setGraphic(roskis);
            mokkiTaulukko.add(poistoNappula, 5, rivi);
            poistoNappula.setOnMouseClicked(e -> {
                // poistamokki();                          //TODO  poistamokki() - metodin luominen
            });
            Nappula muokkausNappula = new Nappula(100, 30);
            ImageView muokkaus = new ImageView(imageKuvasta("muokkaus.png"));
            muokkaus.setFitWidth(23);
            muokkaus.setFitHeight(22);
            muokkausNappula.setGraphic(muokkaus);
            mokkiTaulukko.add(muokkausNappula, 6, rivi);
            muokkausNappula.setOnMouseClicked(e -> {
                // muokkaaMokki();                          //TODO  muokkaamokki() - metodin luominen
            });
            Nappula tarkasteleNappula = new Nappula(170, 30);
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
        BorderPane palvelupaneeli = new BorderPane();
        palvelunappula.setOnAction(e -> {
            paneeli.setCenter(palvelupaneeli);
            isoOtsikkoTeksti.setText("PALVELUT");
        });

        GridPane palveluHaku = new GridPane();
        palveluHaku.setPadding(new Insets(HAKU_PADDING));
        palveluHaku.setHgap(HAKU_HGAP);
        palveluHaku.setVgap(HAKU_VGAP);
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

        // Lajittelu
        palveluLajittelu.setOnAction(e -> {
            switch (palveluLajittelu.getValue()) {
                case "Tunnuksen mukaan" ->
                        palveluLista.sort(Comparator.comparing(Palvelu::getID));
                case "Uusin > Vanhin" -> {} // TODO miten tämä toimii?
                case "Vanhin > Uusin" -> {} // TODO miten tämä toimii?
                case "A > Ö" ->
                        palveluLista.sort(Comparator.comparing(Palvelu::getNimi));
                case "Alueittain" ->
                        palveluLista.sort(Comparator.comparing(Palvelu -> Palvelu.getAlue().getID())); // TODO onko alueen ID:n vai nimen mukaan
            }
            // TODO päivitä näkymä
        });

        ScrollPane palveluScrollaus = new ScrollPane();
        palvelupaneeli.setCenter(palveluScrollaus);
        GridPane palveluTaulukko = new GridPane();
        palveluTaulukko.setPadding(new Insets(20));
        palveluTaulukko.getColumnConstraints().addAll(sarakeSemi, sarakeLevea, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt);
        palveluTaulukko.setGridLinesVisible(true);
        palveluScrollaus.setContent(palveluTaulukko);


        
        Nappula palvelunLisaysNappula = new Nappula(200, 30);
        ImageView palvelunLisays = new ImageView(imageKuvasta("lisays.png"));
        palvelunLisays.setFitWidth(23);
        palvelunLisays.setFitHeight(22);
        palvelunLisaysNappula.setGraphic(palvelunLisays);
        palveluTaulukko.add(palvelunLisaysNappula, 1,0);

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


        int rivi = 2;
        for (Palvelu obj : palveluLista) {
            Text palveluID = new Text(String.valueOf(obj.getID()));
            palveluID.setFont(fontti);
            Text palveluNimi = new Text(String.valueOf(obj.getNimi()));
            palveluNimi.setFont(fontti);
            Text palveluAlue = new Text(String.valueOf(obj.getAlue().getNimi()));
            palveluAlue.setFont(fontti);
            Text palveluHinta = new Text(obj.getHinta() + " €");
            palveluHinta.setFont(fontti);


            palveluID.setTextAlignment(TextAlignment.CENTER);
            palveluTaulukko.add(palveluID, 0, rivi);
            palveluTaulukko.add(palveluNimi, 1, rivi);
            palveluTaulukko.add(palveluAlue, 2, rivi);
            palveluTaulukko.add(palveluHinta, 3, rivi);

            //palveluNimi.setAlignment(Pos.CENTER);
            palveluNimi.setTextAlignment(TextAlignment.CENTER);

            Nappula poistoNappula = new Nappula(150, 30);
            ImageView roskis = new ImageView(imageKuvasta("roskis.png"));
            roskis.setFitWidth(22);
            roskis.setFitHeight(22);
            poistoNappula.setGraphic(roskis);
            palveluTaulukko.add(poistoNappula, 4, rivi);
            poistoNappula.setOnMouseClicked(e -> {
                // poistapalvelu();                          //TODO  poistapalvelu() - metodin luominen
            });

            Nappula muokkausNappula = new Nappula(100, 30);
            ImageView muokkaus = new ImageView(imageKuvasta("muokkaus.png"));
            muokkaus.setFitWidth(23);
            muokkaus.setFitHeight(22);
            muokkausNappula.setGraphic(muokkaus);
            palveluTaulukko.add(muokkausNappula, 5, rivi);
            muokkausNappula.setOnMouseClicked(e -> {
                // muokkaaMokki();                          //TODO  muokkaamokki() - metodin luominen
            });
            Nappula tarkasteleNappula = new Nappula(170, 30);
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
        BorderPane varauspaneeli = new BorderPane();
        varausnappula.setOnAction(e -> {
            paneeli.setCenter(varauspaneeli);
            isoOtsikkoTeksti.setText("VARAUKSET");
        });

        GridPane varausHaku = new GridPane();
        varausHaku.setPadding(new Insets(HAKU_PADDING));
        varausHaku.setHgap(HAKU_HGAP);
        varausHaku.setVgap(HAKU_VGAP);
        varauspaneeli.setTop(varausHaku);

        TextField varausHakuKentta = new TextField();
        Label varausHakuKenttaLabel = new Label("Hae varausta: ", varausHakuKentta);
        varausHakuKenttaLabel.setFont(fontti);
        varausHakuKenttaLabel.setContentDisplay(ContentDisplay.RIGHT);
        varausHaku.add(varausHakuKenttaLabel, 1, 1);
        Nappula varausHakuNappula = new Nappula("Suorita haku", 190, 30);
        varausHaku.add(varausHakuNappula, 1, 2);
        varausHakuNappula.setOnAction( e -> {
            paivitaVarausTaulukko();
        });

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

        // Lajittelu
        varausLajittelu.setOnAction(e -> {
            switch (varausLajittelu.getValue()) {
                case "Tunnuksen mukaan" ->
                        varausLista.sort(Comparator.comparing(Varaus::getID));
                case "Uusin > Vanhin" ->
                        varausLista.sort(Comparator.comparing(Varaus::getVarausAlkuPvm).reversed());
                case "Vanhin > Uusin" ->
                        varausLista.sort(Comparator.comparing(Varaus::getVarausAlkuPvm));
                case "A > Ö" -> {} // TODO miten tämä toimii?
                case "Alueittain" -> varausLista.sort(Comparator.comparing(Varaus -> Varaus.getMokki().getAlue().getID())); // TODO lajitellaanko ID:n vai nimen mukaan
            }
        });

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

        varausScrollaus.setContent(varausTaulukko);



        ImageView varausLisays = new ImageView(imageKuvasta("lisays.png"));
        varausLisays.setFitWidth(23);
        varausLisays.setFitHeight(22);
        varausLisaysNappula.setGraphic(varausLisays);

        varausLisaysNappula.setOnAction(e -> {
            Stage varausLisaysStage = new Stage();

            VBox varausLisaysVBoxPaneeli = new VBox();
            varausLisaysVBoxPaneeli.setPadding(new Insets(30));
            varausLisaysVBoxPaneeli.setSpacing(10);

            varausLisaysVBoxPaneeli.getChildren().add(new Text("Onko kyseessä uusi asiakas?"));
            RadioButton uusiAsiakas = new RadioButton("Kyllä");
            RadioButton vanhaAsiakas = new RadioButton("Ei");
            ToggleGroup asiakas = new ToggleGroup();
            uusiAsiakas.setToggleGroup(asiakas);
            vanhaAsiakas.setToggleGroup(asiakas);
            uusiAsiakas.fire();



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
            Text vanhaAsiakasTeksti = new Text("Jos kyseessä on vanha asiakas, syötä asiakasID.");
            varausLisaysPaneeli.add(vanhaAsiakasTeksti, 0, 0);
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

            vanhaAsiakasTeksti.setVisible(false);
            asiakasIDText.setVisible(false);
            asiakasID.setVisible(false);


            uusiAsiakas.setOnAction( event -> {
                asiakasLisaysPaneeli.setVisible(true);
                vanhaAsiakasTeksti.setVisible(false);
                asiakasIDText.setVisible(false);
                asiakasID.setVisible(false);
            });
            vanhaAsiakas.setOnAction( event -> {
                asiakasLisaysPaneeli.setVisible(false);
                vanhaAsiakasTeksti.setVisible(true);
                asiakasIDText.setVisible(true);
                asiakasID.setVisible(true);
            });

            Nappula lisaaVaraus = new Nappula("Aseta varaus");
            lisaaVaraus.setOnAction( event -> {
                String varausAlkuAika = aloitusPvm.getValue() + " " + aloitusAika.getText(); // TODO muotoilun tarkistus, aka virheiden käsittely
                String varausLoppuAika = lopetusPvm.getValue() + " " + lopetusAika.getText();
                try {
                    int asiakasIDInsert;
                    if (uusiAsiakas.isSelected()) {
                        asiakasLista.add(tietokanta.insertAsiakas(
                                postinro.getText(),
                                enimi.getText(),
                                snimi.getText(),
                                lahiosoite.getText(),
                                email.getText(),
                                puhnro.getText(),
                                postiLista));
                        asiakasIDInsert = asiakasLista.get(asiakasLista.size()-1).getID();
                    } else {
                        asiakasIDInsert = Integer.parseInt(asiakasID.getText()); // TODO virheiden käsittely, näytetään virheteksti ikkunassa
                    }
                    varausLista.add(tietokanta.insertVaraus( // TODO tuleeko kenttään varattu_pvm tämänhetkinen aika?
                            asiakasIDInsert,
                            Integer.parseInt(mokkiID.getText()),
                            LocalDateTime.now().format(dateTimeFormat),
                            null,
                            varausAlkuAika,
                            varausLoppuAika,
                            asiakasLista,
                            mokkiLista));
                    varausLisaysStage.close();
                } catch (SQLException ex) {
                    ilmoitusPaneeli.lisaaIlmoitus(IlmoitusTyyppi.VAROITUS, String.valueOf(ex));
                    throw new RuntimeException(ex); // TEMP
                }
            });

            varausLisaysVBoxPaneeli.getChildren().addAll
                    (uusiAsiakas, vanhaAsiakas, asiakasLisaysPaneeli, varausLisaysPaneeli, lisaaVaraus);

            // Luodaan uusi scene
            Scene scene2 = new Scene(varausLisaysVBoxPaneeli, 400, 650);
            varausLisaysStage.setScene(scene2);
            varausLisaysStage.setTitle("Lisää varaus");
            varausLisaysStage.show();
        });

        paivitaVarausTaulukko();

    }

    public void paivitaVarausTaulukko() {


        varausTaulukko.setGridLinesVisible(false);
        varausTaulukko.getColumnConstraints().clear();
        varausTaulukko.getChildren().clear();
        varausTaulukko.getColumnConstraints().addAll
                (sarakeSemi, sarakeLevea, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt);
        varausTaulukko.setGridLinesVisible(true);

        varausTaulukko.setPadding(new Insets(20));


        varausTaulukko.add(varausLisaysNappula, 1, 0);

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

        int rivi = 2;
        for (Varaus obj : varausLista) { // TODO nyt kun SQL-haku saattaa epäonnistua, voi tulla NullPointerException
            Text varausID = new Text(String.valueOf(obj.getID()));
            varausID.setFont(fontti);
            Text varausNimi = new Text(String.valueOf(obj.getAsiakas().getNimi(false)));
            varausNimi.setFont(fontti);
            Text varausMokki = new Text(String.valueOf(obj.getMokki().getNimi())); // TODO mökin nimi vai ID
            varausMokki.setFont(fontti);


            varausID.setTextAlignment(TextAlignment.CENTER);
            varausTaulukko.add(varausID, 0, rivi);
            varausTaulukko.add(varausNimi, 1, rivi);
            varausTaulukko.add(varausMokki, 2, rivi);

            varausNimi.setTextAlignment(TextAlignment.CENTER);

            Nappula poistoNappula = new Nappula(150, 30);
            ImageView roskis = new ImageView(imageKuvasta("roskis.png"));
            roskis.setFitWidth(22);
            roskis.setFitHeight(22);
            poistoNappula.setGraphic(roskis);
            varausTaulukko.add(poistoNappula, 3, rivi);
            poistoNappula.setOnMouseClicked(e -> {
                PoistoIkkuna poistoIkkuna = new PoistoIkkuna("varaus", "varauksen");

                poistoIkkuna.getPoistoNappula().setOnAction( event -> {
                    try {
                        tietokanta.poistaVaraus(obj.getID());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    poistoIkkuna.getIkkuna().close();
                    paivitaVarausTaulukko();
                });
            });

            Nappula muokkausNappula = new Nappula(100, 30);
            ImageView muokkaus = new ImageView(imageKuvasta("muokkaus.png"));
            muokkaus.setFitWidth(23);
            muokkaus.setFitHeight(22);
            muokkausNappula.setGraphic(muokkaus);
            varausTaulukko.add(muokkausNappula, 4, rivi);
            muokkausNappula.setOnMouseClicked(e -> {
                // muokkaaVaraus();                          //TODO  muokkaavaraus() - metodin luominen
                Stage varausMuokkausIkkuna = new Stage();

                VBox varausMuokkausPaneeli = new VBox(10);
                varausMuokkausPaneeli.setPadding(new Insets(25));

                GridPane varausMuokkausGridPaneeli = new GridPane();
                varausMuokkausGridPaneeli.setVgap(5);
                varausMuokkausGridPaneeli.add(new Text("Muokkaa varauksen tietoja:"), 0, 0);
                TextField asiakasID = new TextField(String.valueOf(obj.getAsiakas()));
                TextField mokkiID = new TextField(String.valueOf(obj.getMokki()));
                DatePicker aloitusPvm = new DatePicker(obj.getVarausAlkuPvm().toLocalDate());
                // Laittaa tekstiksi varausAlkuPvm:n ajan jos se ei ole null, muuten 16:00, TODO tuleeko sekunnit mukaan varausAlkuPvm:stä
                TextField aloitusAika = new TextField(String.valueOf(Objects.requireNonNullElse(
                        obj.getVarausAlkuPvm().toLocalTime(), "16:00")));
                DatePicker lopetusPvm = new DatePicker(obj.getVarausLoppuPvm().toLocalDate());
                TextField lopetusAika = new TextField(String.valueOf(Objects.requireNonNullElse(
                        obj.getVarausLoppuPvm().toLocalTime(), "12:00")));


                Text asiakasIDText = new Text("AsiakasID");
                Text mokkiIDText = new Text("MökkiID");
                Text aloitusPvmText = new Text("Aloituspäivämäärä");
                Text aloitusAikaText = new Text("ja kellonaika");
                Text lopetusPvmText = new Text("Lopetuspäivämäärä");
                Text lopetusAikaText = new Text("ja kellonaika");

                varausMuokkausGridPaneeli.add(asiakasIDText, 0, 1);
                varausMuokkausGridPaneeli.add(asiakasID, 1, 1);
                varausMuokkausGridPaneeli.add(mokkiIDText, 0, 2);
                varausMuokkausGridPaneeli.add(mokkiID, 1, 2);
                varausMuokkausGridPaneeli.add(aloitusPvmText, 0, 3);
                varausMuokkausGridPaneeli.add(aloitusPvm, 1, 3);
                varausMuokkausGridPaneeli.add(aloitusAikaText, 0, 4);
                varausMuokkausGridPaneeli.add(aloitusAika, 1, 4);
                varausMuokkausGridPaneeli.add(lopetusPvmText, 0, 5);
                varausMuokkausGridPaneeli.add(lopetusPvm, 1, 5);
                varausMuokkausGridPaneeli.add(lopetusAikaText, 0, 6);
                varausMuokkausGridPaneeli.add(lopetusAika, 1, 6);


                Nappula tallennaVarausMuutokset = new Nappula("Tallenna muutokset");
                tallennaVarausMuutokset.setOnAction( event -> {
                    //TÄHÄN DROP IF EXISTS tai UPDATE
                    //(tietokanta.insertVaraus();)
                });

                varausMuokkausPaneeli.getChildren().addAll
                        (varausMuokkausGridPaneeli, tallennaVarausMuutokset);

                Scene varausMuokkausKehys = new Scene(varausMuokkausPaneeli, 400, 350);
                varausMuokkausIkkuna.setScene(varausMuokkausKehys);
                varausMuokkausIkkuna.setTitle("Muokkaa varausta");
                varausMuokkausIkkuna.show();
            });


            Nappula tarkasteleNappula = new Nappula(170, 30);
            ImageView tarkastelu = new ImageView(imageKuvasta("tarkastelu.png"));
            tarkastelu.setFitWidth(23);
            tarkastelu.setFitHeight(22);
            tarkasteleNappula.setGraphic(tarkastelu);
            varausTaulukko.add(tarkasteleNappula, 5, rivi);
            tarkasteleNappula.setOnMouseClicked( e -> {
                // tarkasteleMokkia();                          //TODO  tarkasteleMokkia() - metodin luominen
                Stage tarkasteleVarausIkkuna = new Stage();
                tarkasteleVarausIkkuna.show();
                GridPane tarkasteleVarausPaneeli = new GridPane();
                tarkasteleVarausPaneeli.setPadding(new Insets(25));
                tarkasteleVarausPaneeli.setVgap(15);
                tarkasteleVarausPaneeli.setHgap(15);
                Scene tarkasteleVarausKehys = new Scene(tarkasteleVarausPaneeli, 400, 300);
                tarkasteleVarausIkkuna.setScene(tarkasteleVarausKehys);

                tarkasteleVarausPaneeli.add(new Text("Varauksen tiedot"),0,0);
                tarkasteleVarausPaneeli.add(new Text("VarausID: "),0,1);
                tarkasteleVarausPaneeli.add(new Text("AsiakasID: "),0,2);
                tarkasteleVarausPaneeli.add(new Text("MökkiID: "),0,3);
                tarkasteleVarausPaneeli.add(new Text("Varattu: "),0,4);
                tarkasteleVarausPaneeli.add(new Text("Vahvistettu: "),0,5);
                tarkasteleVarausPaneeli.add(new Text("Varauksen alkupvm: "),0,6);
                tarkasteleVarausPaneeli.add(new Text("Varauksen loppupvm: "),0,7);

                tarkasteleVarausPaneeli.add(new Text(String.valueOf(obj.getID())),1,1);
                tarkasteleVarausPaneeli.add(new Text(obj.getAsiakas().getNimi(false)),1,2);
                tarkasteleVarausPaneeli.add(new Text(obj.getMokki().getNimi()),1,3); // TODO mökin nimi vai ID vai molemmat
                tarkasteleVarausPaneeli.add(new Text(dateTimeFormat.format(obj.getVarattuPvm())),1,4);
                tarkasteleVarausPaneeli.add(new Text(dateTimeFormat.format(obj.getVahvistusPvm())),1,5);
                tarkasteleVarausPaneeli.add(new Text(dateTimeFormat.format(obj.getVarausAlkuPvm())),1,6);
                tarkasteleVarausPaneeli.add(new Text(dateTimeFormat.format(obj.getVarausLoppuPvm())),1,7);

            });

            rivi++;
        }
    }

    public void luoAsiakasnakyma() {
        BorderPane asiakaspaneeli = new BorderPane();
        asiakasnappula.setOnAction(e -> {
            paneeli.setCenter(asiakaspaneeli);
            isoOtsikkoTeksti.setText("ASIAKKAAT");
        });

        GridPane asiakasHaku = new GridPane();
        asiakasHaku.setPadding(new Insets(HAKU_PADDING));
        asiakasHaku.setHgap(HAKU_HGAP);
        asiakasHaku.setVgap(HAKU_VGAP);
        asiakaspaneeli.setTop(asiakasHaku);

        TextField asiakasHakuKentta = new TextField();
        Label asiakasHakuKenttaLabel = new Label("Hae asiakasta: ", asiakasHakuKentta);
        asiakasHakuKenttaLabel.setFont(fontti);
        asiakasHakuKenttaLabel.setContentDisplay(ContentDisplay.RIGHT);
        asiakasHaku.add(asiakasHakuKenttaLabel, 1, 1);
        Nappula asiakasHakuNappula = new Nappula("Suorita haku", 190, 30);
        // TODO päivitä näkymä
        asiakasHaku.add(asiakasHakuNappula, 1, 2);

        asiakasHaku.add(new Text("Lajittelu:"), 2, 0);
        ComboBox<String> asiakasLajittelu = new ComboBox<>(FXCollections.observableList(Arrays.asList(
                "Tunnuksen mukaan",
                "Uusin > Vanhin",
                "Vanhin > Uusin",
                "A > Ö"
        )));
        asiakasLajittelu.setValue("Uusin > Vanhin"); // Oletuksena valittu vaihtoehto
        asiakasHaku.add(asiakasLajittelu, 2, 1);

        // Lajittelu
        asiakasLajittelu.setOnAction(e -> {
            switch (asiakasLajittelu.getValue()) {
                case "Tunnuksen mukaan" ->
                        asiakasLista.sort(Comparator.comparing(Asiakas::getID));
                case "Uusin > Vanhin" -> {} // TODO miten tämä toimii?
                case "Vanhin > Uusin" -> {} // TODO miten tämä toimii?
                case "A > Ö" ->
                        asiakasLista.sort(Comparator.comparing(Asiakas::getSukunimi));
            }
        });

        ScrollPane asiakasScrollaus = new ScrollPane();
        asiakaspaneeli.setCenter(asiakasScrollaus);
        GridPane asiakasTaulukko = new GridPane();
        asiakasTaulukko.setPadding(new Insets(20));
        asiakasTaulukko.getColumnConstraints().addAll(sarakeLyhyt, sarakeLevea, sarakeLevea, sarakeLevea, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt);
        asiakasTaulukko.setGridLinesVisible(true);
        asiakasScrollaus.setContent(asiakasTaulukko);


        Nappula asiakasLisaysNappula = new Nappula(200, 30);
        ImageView asiakasLisays = new ImageView(imageKuvasta("lisays.png"));
        asiakasLisays.setFitWidth(23);
        asiakasLisays.setFitHeight(22);
        asiakasLisaysNappula.setGraphic(asiakasLisays);
        asiakasTaulukko.add(asiakasLisaysNappula, 1,0);
        asiakasLisaysNappula.setOnAction( e -> {
            Stage asiakasMuokkausIkkuna = new Stage();

            VBox asiakasMuokkausPaneeli = new VBox(10);
            asiakasMuokkausPaneeli.setPadding(new Insets(25));

            GridPane asiakasMuokkausGridPaneeli = new GridPane();
            asiakasMuokkausGridPaneeli.setVgap(5);
            asiakasMuokkausGridPaneeli.add(new Text("Syötä asiakkaan tiedot."), 0, 0);
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

            asiakasMuokkausGridPaneeli.add(enimiText, 0,1);
            asiakasMuokkausGridPaneeli.add(enimi, 1,1);
            asiakasMuokkausGridPaneeli.add(snimiText, 0,2);
            asiakasMuokkausGridPaneeli.add(snimi, 1,2);
            asiakasMuokkausGridPaneeli.add(emailText, 0,3);
            asiakasMuokkausGridPaneeli.add(email, 1,3);
            asiakasMuokkausGridPaneeli.add(puhnroText, 0,4);
            asiakasMuokkausGridPaneeli.add(puhnro, 1,4);
            asiakasMuokkausGridPaneeli.add(lahiosoiteText, 0,5);
            asiakasMuokkausGridPaneeli.add(lahiosoite, 1,5);
            asiakasMuokkausGridPaneeli.add(postinroText, 0,6);
            asiakasMuokkausGridPaneeli.add(postinro, 1,6);


            Nappula tallennaAsiakasMuutokset = new Nappula("Lisää asiakas");
            tallennaAsiakasMuutokset.setOnAction( event -> {
                // TODO päivitys
            });

            asiakasMuokkausPaneeli.getChildren().addAll
                    (asiakasMuokkausGridPaneeli, tallennaAsiakasMuutokset);

            Scene asiakasMuokkausKehys = new Scene(asiakasMuokkausPaneeli, 400, 350);
            asiakasMuokkausIkkuna.setScene(asiakasMuokkausKehys);
            asiakasMuokkausIkkuna.setTitle("Lisää asiakas");
            asiakasMuokkausIkkuna.show();
        });

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


        int rivi = 2;
        for (Asiakas obj : asiakasLista) {
            Text asiakasID = new Text(String.valueOf(obj.getID()));
            asiakasID.setFont(fontti);
            Text asiakasNimi = new Text(obj.getNimi(false));
            asiakasNimi.setFont(fontti);
            Text asiakasEmail = new Text(String.valueOf(obj.getEmail()));
            asiakasEmail.setFont(fontti);
            Text asiakasPuhNro = new Text(String.valueOf(obj.getPuhelinNro()));
            asiakasPuhNro.setFont(fontti);


            asiakasTaulukko.add(asiakasID, 0, rivi);
            asiakasTaulukko.add(asiakasNimi, 1, rivi);
            asiakasTaulukko.add(asiakasEmail, 2, rivi);
            asiakasTaulukko.add(asiakasPuhNro, 3, rivi);


            Nappula poistoNappula = new Nappula(150, 30);
            ImageView roskis = new ImageView(imageKuvasta("roskis.png"));
            roskis.setFitWidth(22);
            roskis.setFitHeight(22);
            poistoNappula.setGraphic(roskis);
            asiakasTaulukko.add(poistoNappula, 4, rivi);
            poistoNappula.setOnMouseClicked(e -> {
                PoistoIkkuna poistoIkkuna = new PoistoIkkuna("asiakas", "asiakkaan",
                        "Kaikki asiakkaan varaukset ja laskut poistuvat samalla järjestelmästä.");

                poistoIkkuna.getPoistoNappula().setOnAction( event -> {
                    try {
                        tietokanta.poistaAsiakas(obj.getID());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    poistoIkkuna.getIkkuna().close();
                    //TODO listan päivitys!!!
                });
            });

            Nappula muokkausNappula = new Nappula(100, 30);
            ImageView muokkaus = new ImageView(imageKuvasta("muokkaus.png"));
            muokkaus.setFitWidth(23);
            muokkaus.setFitHeight(22);
            muokkausNappula.setGraphic(muokkaus);
            asiakasTaulukko.add(muokkausNappula, 5, rivi);
            muokkausNappula.setOnMouseClicked(e -> {
                // muokkaaAsiakas();                          //TODO  muokkaaAsiakas() - metodin luominen

                Stage asiakasMuokkausIkkuna = new Stage();

                VBox asiakasMuokkausPaneeli = new VBox(10);
                asiakasMuokkausPaneeli.setPadding(new Insets(25));

                GridPane asiakasMuokkausGridPaneeli = new GridPane();
                asiakasMuokkausGridPaneeli.setVgap(5);
                asiakasMuokkausGridPaneeli.add(new Text("Muokkaa asiakkaan tietoja."), 0, 0);
                TextField enimi = new TextField();
                TextField snimi = new TextField();
                TextField email = new TextField();
                TextField puhnro = new TextField();
                TextField lahiosoite = new TextField();
                TextField postinro = new TextField();

                enimi.setText(obj.getEtunimi());
                snimi.setText(obj.getSukunimi());
                email.setText(obj.getEmail());
                puhnro.setText(obj.getPuhelinNro());
                lahiosoite.setText(obj.getLahiosoite());
                postinro.setText(obj.getPostiNro().getPostiNro());

                Text enimiText = new Text("Etunimi");
                Text snimiText = new Text("Sukunimi");
                Text emailText = new Text("Sähköpostiosoite");
                Text puhnroText = new Text("Puhelinnumero");
                Text lahiosoiteText = new Text("Lähiosoite");
                Text postinroText = new Text("Postinumero");

                asiakasMuokkausGridPaneeli.add(enimiText, 0,1);
                asiakasMuokkausGridPaneeli.add(enimi, 1,1);
                asiakasMuokkausGridPaneeli.add(snimiText, 0,2);
                asiakasMuokkausGridPaneeli.add(snimi, 1,2);
                asiakasMuokkausGridPaneeli.add(emailText, 0,3);
                asiakasMuokkausGridPaneeli.add(email, 1,3);
                asiakasMuokkausGridPaneeli.add(puhnroText, 0,4);
                asiakasMuokkausGridPaneeli.add(puhnro, 1,4);
                asiakasMuokkausGridPaneeli.add(lahiosoiteText, 0,5);
                asiakasMuokkausGridPaneeli.add(lahiosoite, 1,5);
                asiakasMuokkausGridPaneeli.add(postinroText, 0,6);
                asiakasMuokkausGridPaneeli.add(postinro, 1,6);


                Nappula tallennaAsiakasMuutokset = new Nappula("Tallenna muutokset");
                tallennaAsiakasMuutokset.setOnAction( event -> {
                    // TODO muokkausmetodi
                });

                asiakasMuokkausPaneeli.getChildren().addAll
                        (asiakasMuokkausGridPaneeli, tallennaAsiakasMuutokset);

                Scene asiakasMuokkausKehys = new Scene(asiakasMuokkausPaneeli, 400, 350);
                asiakasMuokkausIkkuna.setScene(asiakasMuokkausKehys);
                asiakasMuokkausIkkuna.setTitle("Muokkaa asiakkaan tietoja");
                asiakasMuokkausIkkuna.show();
            });


            Nappula tarkasteleNappula = new Nappula(170, 30);
            ImageView tarkastelu = new ImageView(imageKuvasta("tarkastelu.png"));
            tarkastelu.setFitWidth(23);
            tarkastelu.setFitHeight(22);
            tarkasteleNappula.setGraphic(tarkastelu);
            asiakasTaulukko.add(tarkasteleNappula, 6, rivi);
            tarkasteleNappula.setOnMouseClicked(e -> {
                // tarkasteleMokkia();                          //TODO  tarkasteleMokkia() - metodin luominen
                Stage tarkasteleAsiakasIkkuna = new Stage();
                tarkasteleAsiakasIkkuna.show();
                GridPane tarkasteleAsiakasPaneeli = new GridPane();
                tarkasteleAsiakasPaneeli.setPadding(new Insets(25));
                tarkasteleAsiakasPaneeli.setVgap(15);
                tarkasteleAsiakasPaneeli.setHgap(15);
                Scene tarkasteleAsiakasKehys = new Scene(tarkasteleAsiakasPaneeli, 400, 300);
                tarkasteleAsiakasIkkuna.setScene(tarkasteleAsiakasKehys);

                tarkasteleAsiakasPaneeli.add(new Text("Asiakkaan tiedot"),0,0);
                tarkasteleAsiakasPaneeli.add(new Text("AsiakasID: "),0,1);
                tarkasteleAsiakasPaneeli.add(new Text("Nimi: "),0,2);
                tarkasteleAsiakasPaneeli.add(new Text("Email: "),0,3);
                tarkasteleAsiakasPaneeli.add(new Text("Puhelinnumero: "),0,4);
                tarkasteleAsiakasPaneeli.add(new Text("Lähiosoite: "),0,5);
                tarkasteleAsiakasPaneeli.add(new Text("Postinumero: "),0,6);

                tarkasteleAsiakasPaneeli.add(new Text(String.valueOf(obj.getID())),1,1);
                tarkasteleAsiakasPaneeli.add(new Text(obj.getNimi(false)),1,2);
                tarkasteleAsiakasPaneeli.add(new Text(obj.getEmail()),1,3);
                tarkasteleAsiakasPaneeli.add(new Text(obj.getPuhelinNro()),1,4);
                tarkasteleAsiakasPaneeli.add(new Text(obj.getLahiosoite()),1,5);
                tarkasteleAsiakasPaneeli.add(new Text(obj.getPostiNro().getPostiNro()),1,6);
            });

            rivi++;
        }
    }

    public void luoLaskunakyma() {
        BorderPane laskupaneeli = new BorderPane();
        laskunappula.setOnAction(e -> {
            paneeli.setCenter(laskupaneeli);
            isoOtsikkoTeksti.setText("LASKUT");
        });

        GridPane laskuHaku = new GridPane();
        laskuHaku.setPadding(new Insets(HAKU_PADDING));
        laskuHaku.setHgap(HAKU_HGAP);
        laskuHaku.setVgap(HAKU_VGAP);
        laskupaneeli.setTop(laskuHaku);

        TextField laskuHakuKentta = new TextField();
        Label laskuHakuKenttaLabel = new Label("Hae laskuja: ", laskuHakuKentta);
        laskuHakuKenttaLabel.setFont(fontti);
        laskuHakuKenttaLabel.setContentDisplay(ContentDisplay.RIGHT);
        laskuHaku.add(laskuHakuKenttaLabel, 1, 1);
        Nappula laskuHakuNappula = new Nappula("Suorita haku", 190, 30);
        // TODO päivitä näkymä
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

        // Lajittelu
        laskuLajittelu.setOnAction(e -> {
            switch (laskuLajittelu.getValue()) {
                case "Tunnuksen mukaan" ->
                        laskuLista.sort(Comparator.comparing(Lasku::getID));
                case "Uusin > Vanhin" ->
                        laskuLista.sort(Comparator.comparing(Lasku -> Lasku.getVaraus().getVahvistusPvm())); // TODO tehdäänkö vahvistuspvm:n mukaan
                case "Vanhin > Uusin" ->
                        laskuLista.sort(Comparator.comparing(Lasku -> Lasku.getVaraus().getVahvistusPvm())); // TODO tämä ei jostain syystä toimi jos siihen laittaa reversed perään
                case "Varaustunnuksen mukaan" ->
                        laskuLista.sort(Comparator.comparing(Lasku -> Lasku.getVaraus().getID()));
            }
        });

        ScrollPane laskuScrollaus = new ScrollPane();
        laskupaneeli.setCenter(laskuScrollaus);
        GridPane laskuTaulukko = new GridPane();
        laskuTaulukko.setPadding(new Insets(20));
        laskuTaulukko.getColumnConstraints().addAll(sarakeLyhyt, sarakeLevea, sarakeLyhyt, sarakeSemi, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt);
        laskuTaulukko.setGridLinesVisible(true);
        laskuScrollaus.setContent(laskuTaulukko);


        Nappula laskunLisaysNappula = new Nappula(200, 30);
        ImageView laskunLisays = new ImageView(imageKuvasta("lisays.png"));
        laskunLisays.setFitWidth(23);
        laskunLisays.setFitHeight(22);
        laskunLisaysNappula.setGraphic(laskunLisays);
        laskuTaulukko.add(laskunLisaysNappula, 1,0);

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


        int rivi = 2;
        for (Lasku obj : laskuLista) {
            Text laskuID = new Text(String.valueOf(obj.getID()));
            laskuID.setFont(fontti);
            Text laskuVaraus = new Text(String.valueOf(obj.getVaraus().getID()));
            laskuVaraus.setFont(fontti);
            Text laskuSumma = new Text(obj.getSumma() + " €");
            laskuSumma.setFont(fontti);
            Text laskuStatus = new Text(String.valueOf(obj.getStatus()));
            laskuStatus.setFont(fontti);


            laskuTaulukko.add(laskuID, 0, rivi);
            laskuTaulukko.add(laskuVaraus, 1, rivi);
            laskuTaulukko.add(laskuSumma, 2, rivi);
            laskuTaulukko.add(laskuStatus, 3, rivi);


            Nappula poistoNappula = new Nappula(150, 30);
            ImageView roskis = new ImageView(imageKuvasta("roskis.png"));
            roskis.setFitWidth(22);
            roskis.setFitHeight(22);
            poistoNappula.setGraphic(roskis);
            laskuTaulukko.add(poistoNappula, 4, rivi);
            poistoNappula.setOnMouseClicked(e -> {
                PoistoIkkuna poistoIkkuna = new PoistoIkkuna("lasku", "laskun");

                poistoIkkuna.getPoistoNappula().setOnAction( event -> {
                    try {
                        tietokanta.poistaLasku(obj.getID());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    poistoIkkuna.getIkkuna().close();
                    //TODO listan päivitys!!!
                });
            });

            Nappula muokkausNappula = new Nappula(100, 30);
            ImageView muokkaus = new ImageView(imageKuvasta("muokkaus.png"));
            muokkaus.setFitWidth(23);
            muokkaus.setFitHeight(22);
            muokkausNappula.setGraphic(muokkaus);
            laskuTaulukko.add(muokkausNappula, 5, rivi);
            muokkausNappula.setOnMouseClicked(e -> {
                // muokkaaLasku();                          //TODO  muokkaaLasku() - metodin luominen
            });

            Nappula tarkasteleNappula = new Nappula(170, 30);
            ImageView tarkastelu = new ImageView(imageKuvasta("tarkastelu.png"));
            tarkastelu.setFitWidth(23);
            tarkastelu.setFitHeight(22);
            tarkasteleNappula.setGraphic(tarkastelu);
            laskuTaulukko.add(tarkasteleNappula, 6, rivi);
            tarkasteleNappula.setOnMouseClicked(e -> {
                // tarkasteleLasku();                          //TODO  tarkasteleLasku() - metodin luominen
            });

            Nappula luoLaskuNappula = new Nappula(150, 30);
            ImageView tiedostoksi = new ImageView(imageKuvasta("tiedostoksi.png"));
            tiedostoksi.setFitWidth(33);
            tiedostoksi.setFitHeight(22);
            luoLaskuNappula.setGraphic(tiedostoksi);
            laskuTaulukko.add(luoLaskuNappula, 7, rivi);
            luoLaskuNappula.setOnMouseClicked(e -> {
                // luoLasku();                          //TODO  luoLasku() - metodin luominen
            });

            rivi++;
        }
    }

    public static void main(String[] args) {
        launch();
    }


}