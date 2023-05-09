package com.example.otiprojekti;

import com.example.otiprojekti.ilmoitukset.IlmoitusPaneeli;
import com.example.otiprojekti.ilmoitukset.IlmoitusTyyppi;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.example.otiprojekti.Tietokanta.dateTimeFormat;
import static com.example.otiprojekti.Utils.*;

public class Main extends Application {
    // Ikkunan mittasuhde jaettuna kahteen kenttään
    public final double SUHDE_W = 2;
    public final double SUHDE_H = 1;
    // Kuinka suuren osan näytön leveydestä tai korkeudesta ikkuna vie enintään
    public final double MAX_OSUUS = 0.9;
    // Ikkunan suurin sallittu koko
    public final int MAX_LEVEYS = 1600;
    public final int MAX_KORKEUS = 800;
    
    public static final Font fonttiIsompi = Font.font(16);
    public static final Font fonttiPaksu = Font.font("", FontWeight.BOLD, 14);
    public static final Font fonttiKursiivi = Font.font("", FontPosture.ITALIC, 12);
    public static final int HAKU_PADDING = 20;
    public static final int HAKU_HGAP = 20;
    public static final int HAKU_VGAP = 15;

    ToggleNappula aluenappula = new ToggleNappula("Alueet");
    ToggleNappula mokkinappula = new ToggleNappula("Mökit");
    ToggleNappula palvelunappula = new ToggleNappula("Palvelut");
    ToggleNappula varausnappula = new ToggleNappula("Varaukset");
    ToggleNappula asiakasnappula = new ToggleNappula("Asiakkaat");
    ToggleNappula laskunappula = new ToggleNappula("Laskut");
    ToggleNappula[] nappulat = new ToggleNappula[] {
            aluenappula, mokkinappula, palvelunappula, varausnappula, asiakasnappula, laskunappula};
    ToggleGroup tgSivuvalikko = new ToggleGroup();

    BorderPane paneeli = new BorderPane();
    IlmoitusPaneeli ilmoitusPaneeli = new IlmoitusPaneeli();
    BorderPane ilmoitusPaneeliPaneeli = new BorderPane();
    StackPane paneeliYlin = new StackPane(paneeli, ilmoitusPaneeliPaneeli);
    Text isoOtsikkoTeksti = new Text();

    // Taulukon sarakkeet
    ColumnConstraints sarakeLevea = new ColumnConstraints();
    ColumnConstraints sarakeSemi = new ColumnConstraints();
    ColumnConstraints sarakeLyhyt = new ColumnConstraints();


    // Tietokantayhteys
    Tietokanta tietokanta = new Tietokanta();

    ArrayList<Posti> postiLista = new ArrayList<>();
    ArrayList<Alue> alueLista = new ArrayList<>();
    ArrayList<Mokki> mokkiLista = new ArrayList<>();
    ArrayList<Asiakas> asiakasLista = new ArrayList<>();
    ArrayList<Palvelu> palveluLista = new ArrayList<>();
    ArrayList<Varaus> varausLista = new ArrayList<>();
    ArrayList<Lasku> laskuLista = new ArrayList<>();

    GridPane varausTaulukko = new GridPane();
    GridPane asiakasTaulukko = new GridPane();
    GridPane laskuTaulukko = new GridPane();
    GridPane alueTaulukko = new GridPane();
    GridPane mokkiTaulukko = new GridPane();
    GridPane palveluTaulukko = new GridPane();

    final int LISAYS_NAPPULA_LEVEYS = 200;
    final int LISAYS_NAPPULA_KORKEUS = 30;
    Nappula varausLisaysNappula = new Nappula(LISAYS_NAPPULA_LEVEYS, LISAYS_NAPPULA_KORKEUS);
    Nappula asiakasLisaysNappula = new Nappula(LISAYS_NAPPULA_LEVEYS, LISAYS_NAPPULA_KORKEUS);
    Nappula laskunLisaysNappula = new Nappula(LISAYS_NAPPULA_LEVEYS, LISAYS_NAPPULA_KORKEUS);
    Nappula alueenLisaysNappula = new Nappula(LISAYS_NAPPULA_LEVEYS, LISAYS_NAPPULA_KORKEUS);
    Nappula mokkienLisaysNappula = new Nappula(LISAYS_NAPPULA_LEVEYS, LISAYS_NAPPULA_KORKEUS);
    Nappula palvelunLisaysNappula = new Nappula(LISAYS_NAPPULA_LEVEYS, LISAYS_NAPPULA_KORKEUS);

    @Override
    public void start(Stage ikkuna) {

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
        ilmoitusPaneeliPaneeli.setRight(ilmoitusPaneeli);
        ilmoitusPaneeliPaneeli.setMouseTransparent(true); // Mahdollistaa ilmoituspaneelin alla olevan ohjelman käytön, mutta ilmoitukset eivät tottele hiirtä

        // Sarakkeet taulukkoihin
        sarakeLevea.setHalignment(HPos.CENTER);
        sarakeLevea.setPrefWidth(200);
        sarakeSemi.setHalignment(HPos.CENTER);
        sarakeSemi.setPrefWidth(120);
        sarakeLyhyt.setHalignment(HPos.CENTER);
        sarakeLyhyt.setPrefWidth(80);

        // Haetaan tiedot tietokannasta
        haeKaikkiTiedot();

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
            paivitaAlueTaulukko();
        });

        GridPane alueHaku = new GridPane();
        alueHaku.setPadding(new Insets(HAKU_PADDING));
        alueHaku.setHgap(HAKU_HGAP);
        alueHaku.setVgap(HAKU_VGAP);
        aluepaneeli.setTop(alueHaku);

        TextField alueHakuKentta = new TextField();
        Label alueHakuKenttaLabel = new Label("Hae aluetta: ", alueHakuKentta);
        alueHakuKenttaLabel.setFont(fonttiIsompi);
        alueHakuKenttaLabel.setContentDisplay(ContentDisplay.RIGHT);
        alueHaku.add(alueHakuKenttaLabel, 1, 1);
        Nappula alueHakuNappula = new Nappula("Suorita haku", 190, 30);
        alueHaku.add(alueHakuNappula, 1, 2);

        Text alueLajitteluTeksti = new Text("Lajittelu");
        alueLajitteluTeksti.setFont(fonttiPaksu);
        alueHaku.add(alueLajitteluTeksti, 2, 0);
        ComboBox<String> alueLajittelu = new ComboBox<>(FXCollections.observableList(Arrays.asList(
                "Tunnuksen mukaan",
                "Uusin > Vanhin",
                "Vanhin > Uusin",
                "A > Ö"
        )));
        alueLajittelu.setValue("Uusin > Vanhin"); // Oletuksena valittu vaihtoehto
        alueHaku.add(alueLajittelu, 2, 1);

        alueHakuNappula.setOnAction( e -> {
            // Suodatus
            // TODO

            // Lajittelu
            switch (alueLajittelu.getValue()) {
                case "Tunnuksen mukaan" ->
                        alueLista.sort(Comparator.comparing(Alue::getID));
                case "A > Ö" ->
                        alueLista.sort(Comparator.comparing(Alue::getNimi));
            }

            paivitaAlueTaulukko();
        });

        ScrollPane alueScrollaus = new ScrollPane();
        aluepaneeli.setCenter(alueScrollaus);
        
        alueScrollaus.setContent(alueTaulukko);


        
        ImageView alueenLisays = new ImageView(imageKuvasta("lisays.png"));
        alueenLisays.setFitWidth(23);
        alueenLisays.setFitHeight(22);
        alueenLisaysNappula.setGraphic(alueenLisays);

        // Ikkuna alueen lisäämiseen
        alueenLisaysNappula.setOnAction( e -> {
            Stage alueLisaysIkkuna = new Stage();
            alueLisaysIkkuna.show();
            alueLisaysIkkuna.setTitle("Lisää alue");
            VBox alueLisaysPaneeli = new VBox();
            alueLisaysPaneeli.setPadding(new Insets(25));
            alueLisaysPaneeli.setSpacing(15);

            GridPane alueLisaysGridPaneeli = new GridPane();
            alueLisaysGridPaneeli.setVgap(15);
            alueLisaysGridPaneeli.setHgap(15);

            Text alueLisaysTeksti = new Text("Syötä alueen tiedot.");
            alueLisaysGridPaneeli.add(alueLisaysTeksti, 0, 0);

            Text alueNimiTeksti = new Text("Alueen nimi: ");
            TextField alueNimi = new TextField();

            alueLisaysGridPaneeli.add(alueNimiTeksti, 0, 1);
            alueLisaysGridPaneeli.add(alueNimi, 1, 1);

            Nappula lisaaAlueNappula = new Nappula("Lisää alue");

            lisaaAlueNappula.setOnAction( event -> {
                try {
                    tietokanta.insertAlue(alueNimi.getText());
                    alueLisaysIkkuna.close();
                    haeKaikkiTiedot();
                    paivitaAlueTaulukko();
                } catch (SQLException ex) {
                    alueLisaysTeksti.setText("Alueen lisääminen ei onnistunut. \n Yritä uudelleen.");
                    alueLisaysTeksti.setFill(Color.RED);
                }
            });

            alueLisaysPaneeli.getChildren().addAll(alueLisaysGridPaneeli, lisaaAlueNappula);

            Scene alueLisaysKehys = new Scene(alueLisaysPaneeli, 400, 300);
            alueLisaysIkkuna.setScene(alueLisaysKehys);
        });
    }

    public void paivitaAlueTaulukko() {

        alueTaulukko.setGridLinesVisible(false);
        alueTaulukko.getColumnConstraints().clear();
        alueTaulukko.getChildren().clear();
        alueTaulukko.getColumnConstraints().addAll(
                sarakeLyhyt, sarakeLevea, sarakeLyhyt, sarakeLyhyt);
        alueTaulukko.setGridLinesVisible(true);

        alueTaulukko.setPadding(new Insets(20));
        
        alueTaulukko.add(alueenLisaysNappula, 1,0);

        Text aluetunnusOtsikko = new Text("Tunnus");
        Text alueennimiOtsikko = new Text("Alueen nimi");
        aluetunnusOtsikko.setFont(fonttiIsompi);
        alueennimiOtsikko.setFont(fonttiIsompi);
        alueTaulukko.add(aluetunnusOtsikko, 0, 1);
        alueTaulukko.add(alueennimiOtsikko, 1, 1);


        int rivi = 2;
        for (Alue obj : alueLista) {
            Text alueID = new Text(String.valueOf(obj.getID()));
            alueID.setFont(fonttiIsompi);
            Text alueNimi = new Text(String.valueOf(obj.getNimi()));
            alueNimi.setFont(fonttiIsompi);
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
            // Ikkuna alueen poistamiseen
            poistoNappula.setOnMouseClicked(e -> {
                PoistoIkkuna poistaAlueIkkuna = new PoistoIkkuna("alue", "alueen",
                        "Kaikki alueeseen kuuluvat mökit ja \n palvelut poistetaan samalla.");

                poistaAlueIkkuna.getPoistoNappula().setOnAction( event -> {
                    try {
                        tietokanta.poistaAlue(obj.getID());
                        haeKaikkiTiedot();
                        poistaAlueIkkuna.getIkkuna().close();
                        paivitaAlueTaulukko();
                    } catch (SQLException ex) {
                        ilmoitusPaneeli.lisaaIlmoitus(IlmoitusTyyppi.VAROITUS, "Virhe alueen poistamisessa.");
                        throw new RuntimeException(ex); // TEMP
                    }
                });
            });

            Nappula muokkausNappula = new Nappula(150, 30);
            ImageView muokkaus = new ImageView(imageKuvasta("muokkaus.png"));
            muokkaus.setFitWidth(22);
            muokkaus.setFitHeight(22);
            muokkausNappula.setGraphic(muokkaus);
            alueTaulukko.add(muokkausNappula, 3, rivi);
            // Ikkuna alueen muokkaamiseen
            muokkausNappula.setOnAction(e -> {
                Stage alueMuokkausIkkuna = new Stage();
                alueMuokkausIkkuna.show();
                alueMuokkausIkkuna.setTitle("Muokkaa alueen tietoja");
                VBox alueMuokkausPaneeli = new VBox();
                alueMuokkausPaneeli.setPadding(new Insets(25));
                alueMuokkausPaneeli.setSpacing(15);

                GridPane alueMuokkausGridPaneeli = new GridPane();
                alueMuokkausGridPaneeli.setVgap(15);
                alueMuokkausGridPaneeli.setHgap(15);

                Text alueMuokkausTeksti = new Text("Muokkaa alueen tietoja.");
                alueMuokkausGridPaneeli.add(alueMuokkausTeksti, 0, 0);

                Text alueNimiTeksti = new Text("Alueen nimi: ");
                TextField alueenNimi = new TextField();
                alueenNimi.setText(obj.getNimi());

                alueMuokkausGridPaneeli.add(alueNimiTeksti, 0, 1);
                alueMuokkausGridPaneeli.add(alueenNimi, 1, 1);

                Nappula muokkaaAlueNappula = new Nappula("Tallenna muutokset");

                muokkaaAlueNappula.setOnAction( event -> {
                    try {
                        tietokanta.muokkaaAlue(obj.getID(), alueenNimi.getText());
                        alueMuokkausIkkuna.close();
                        haeKaikkiTiedot();
                        paivitaAlueTaulukko();
                    } catch (SQLException ex) {
                        alueMuokkausTeksti.setText("Alueen tietojen muokkaaminen ei onnistunut. \n Yritä uudelleen.");
                        alueMuokkausTeksti.setFill(Color.RED);
                    }
                });

                alueMuokkausPaneeli.getChildren().addAll(alueMuokkausGridPaneeli, muokkaaAlueNappula);

                Scene alueMuokkausKehys = new Scene(alueMuokkausPaneeli, 400, 300);
                alueMuokkausIkkuna.setScene(alueMuokkausKehys);
            });

            rivi++;
        }
    }
    
    public void luoMokkinakyma() {
        BorderPane mokkipaneeli = new BorderPane();
        mokkinappula.setOnAction(e -> {
            paneeli.setCenter(mokkipaneeli);
            isoOtsikkoTeksti.setText("MÖKIT");
            paivitaMokkiTaulukko(null, null);
        });

        GridPane mokkiHaku = new GridPane();
        mokkiHaku.setPadding(new Insets(HAKU_PADDING));
        mokkiHaku.setHgap(HAKU_HGAP);
        mokkiHaku.setVgap(HAKU_VGAP);
        mokkipaneeli.setTop(mokkiHaku);

        TextField mokkiHakuKentta = new TextField();
        Label mokkiHakuKenttaLabel = new Label("Hae mökkiä: ", mokkiHakuKentta);
        mokkiHakuKenttaLabel.setFont(fonttiIsompi);
        mokkiHakuKenttaLabel.setContentDisplay(ContentDisplay.RIGHT);
        mokkiHaku.add(mokkiHakuKenttaLabel, 1, 1);
        Nappula mokkiHakuNappula = new Nappula("Suorita haku", 190, 30);
        mokkiHaku.add(mokkiHakuNappula, 1, 2);

        Text mokkiLajitteluTeksti = new Text("Lajittelu");
        mokkiLajitteluTeksti.setFont(fonttiPaksu);
        mokkiHaku.add(mokkiLajitteluTeksti, 2, 0);
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

        mokkiHaku.add(new Text("Onko varattu päivinä"), 3, 0);
        DatePicker varausPaivaAlku = new DatePicker();
        DatePicker varausPaivaLoppu = new DatePicker();
        Label varausPaivaAlkuLabel = new Label("Alku", varausPaivaAlku);
        Label varausPaivaLoppuLabel = new Label("Loppu", varausPaivaLoppu);
        mokkiHaku.add(varausPaivaAlkuLabel, 3, 1);
        mokkiHaku.add(varausPaivaLoppuLabel, 3, 2);

        mokkiHakuNappula.setOnAction( e -> {
            // Suodatus
            // TODO

            // Lajittelu
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

            // Valittu päivämäärä
            LocalDate alkuPvm = varausPaivaAlku.getValue();
            LocalDate loppuPvm = varausPaivaLoppu.getValue();

            paivitaMokkiTaulukko(alkuPvm, loppuPvm);
        });

        ScrollPane mokkiScrollaus = new ScrollPane();
        mokkipaneeli.setCenter(mokkiScrollaus);

        mokkiScrollaus.setContent(mokkiTaulukko);

        ImageView mokkienLisays = new ImageView(imageKuvasta("lisays.png"));
        mokkienLisays.setFitWidth(23);
        mokkienLisays.setFitHeight(22);
        mokkienLisaysNappula.setGraphic(mokkienLisays);

        // Ikkuna mökkien lisäämiseen
        mokkienLisaysNappula.setOnAction( e -> {
            Stage mokkiLisaysIkkuna = new Stage();
            mokkiLisaysIkkuna.show();
            mokkiLisaysIkkuna.setTitle("Lisää mökki");
            VBox mokkiLisaysPaneeli = new VBox();
            mokkiLisaysPaneeli.setPadding(new Insets(25));
            mokkiLisaysPaneeli.setSpacing(15);

            GridPane mokkiLisaysGridPaneeli = new GridPane();
            mokkiLisaysGridPaneeli.setVgap(15);
            mokkiLisaysGridPaneeli.setHgap(15);

            Text mokkiLisaysTeksti = new Text("Syötä mökin tiedot.");
            mokkiLisaysGridPaneeli.add(mokkiLisaysTeksti, 0, 0);

            Text mokkiNimiTeksti = new Text("Mökin nimi: ");
            TextField mokkiNimi = new TextField();
            Text mokkiAlueTeksti = new Text("AlueID: ");
            TextField mokkiAlue = new TextField();
            Text mokkiOsoiteTeksti = new Text("Katuosoite: ");
            TextField mokkiOsoite = new TextField();
            Text mokkiPostinroTeksti = new Text("Postinro: ");
            TextField mokkiPostinro = new TextField();
            Text mokkiPostitoimipaikkaTeksti = new Text("Postitoimipaikka: ");
            TextField mokkiPostitoimipaikka = new TextField();
            Text mokkiHintaTeksti = new Text("Hinta/vrk(€): ");
            TextField mokkiHinta = new TextField();
            Text mokkiKuvausTeksti = new Text("Kuvaus: ");
            TextArea mokkiKuvaus = new TextArea();
            mokkiKuvaus.setWrapText(true);
            Text mokkiHlomaaraTeksti = new Text("Henkilömäärä: ");
            TextField mokkiHlomaara = new TextField();
            Text mokkiVarusteluTeksti = new Text("Varustelu: ");
            TextField mokkiVarustelu = new TextField();

            mokkiLisaysGridPaneeli.add(mokkiNimiTeksti, 0, 1);
            mokkiLisaysGridPaneeli.add(mokkiNimi, 1, 1);
            mokkiLisaysGridPaneeli.add(mokkiAlueTeksti, 0, 2);
            mokkiLisaysGridPaneeli.add(mokkiAlue, 1, 2);
            mokkiLisaysGridPaneeli.add(mokkiOsoiteTeksti, 0, 3);
            mokkiLisaysGridPaneeli.add(mokkiOsoite, 1, 3);
            mokkiLisaysGridPaneeli.add(mokkiPostinroTeksti, 0, 4);
            mokkiLisaysGridPaneeli.add(mokkiPostinro, 1, 4);
            mokkiLisaysGridPaneeli.add(mokkiPostitoimipaikkaTeksti, 0, 5);
            mokkiLisaysGridPaneeli.add(mokkiPostitoimipaikka, 1, 5);
            mokkiLisaysGridPaneeli.add(mokkiHintaTeksti, 0, 6);
            mokkiLisaysGridPaneeli.add(mokkiHinta, 1, 6);
            mokkiLisaysGridPaneeli.add(mokkiKuvausTeksti, 0, 7);
            mokkiLisaysGridPaneeli.add(mokkiKuvaus, 1, 7);
            mokkiLisaysGridPaneeli.add(mokkiHlomaaraTeksti, 0, 8);
            mokkiLisaysGridPaneeli.add(mokkiHlomaara, 1, 8);
            mokkiLisaysGridPaneeli.add(mokkiVarusteluTeksti, 0, 9);
            mokkiLisaysGridPaneeli.add(mokkiVarustelu, 1, 9);

            Nappula lisaaMokkiNappula = new Nappula("Lisää mökki");

            lisaaMokkiNappula.setOnAction( event -> {
                try {
                    if (etsiPostiNro(postiLista, mokkiPostinro.getText()) == null) {
                        tietokanta.insertPosti(mokkiPostinro.getText(), mokkiPostitoimipaikka.getText());
                    }

                    tietokanta.insertMokki(
                            Integer.parseInt(mokkiAlue.getText()),
                            mokkiPostinro.getText(),
                            mokkiNimi.getText(),
                            mokkiOsoite.getText(),
                            BigDecimal.valueOf(Double.parseDouble(mokkiHinta.getText())),
                            mokkiKuvaus.getText(),
                            Integer.parseInt(mokkiHlomaara.getText()),
                            mokkiVarustelu.getText()
                            );
                    mokkiLisaysIkkuna.close();
                    haeKaikkiTiedot();
                    paivitaMokkiTaulukko(null, null);
                } catch (SQLException ex) {
                    mokkiLisaysTeksti.setText("Mökin lisääminen ei onnistunut. \n " +
                            "Tarkista syötteet ja yritä uudelleen.");
                    mokkiLisaysTeksti.setFill(Color.RED);
                }
            });

            mokkiLisaysPaneeli.getChildren().addAll(mokkiLisaysGridPaneeli, lisaaMokkiNappula);

            Scene mokkiLisaysKehys = new Scene(mokkiLisaysPaneeli, 400, 550);
            mokkiLisaysIkkuna.setScene(mokkiLisaysKehys);
        });
    }

    public void paivitaMokkiTaulukko(LocalDate paivaAlku, LocalDate paivaLoppu) {

        mokkiTaulukko.setGridLinesVisible(false);
        mokkiTaulukko.getColumnConstraints().clear();
        mokkiTaulukko.getChildren().clear();
        mokkiTaulukko.getColumnConstraints().addAll(
                sarakeLyhyt, sarakeLevea, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt);
        mokkiTaulukko.setGridLinesVisible(true);

        mokkiTaulukko.setPadding(new Insets(20));

        mokkiTaulukko.add(mokkienLisaysNappula, 1,0);

        Text mokkitunnusOtsikko = new Text("Tunnus");
        Text mokinnimiOtsikko = new Text("Mökki");
        Text mokinAlueOtsikko = new Text("Alue");
        Text mokinHintaOtsikko = new Text("Hinta/vrk");
        Text mokinHloMaaraOtsikko = new Text("Hlö.määrä");
        mokkitunnusOtsikko.setFont(fonttiIsompi);
        mokinnimiOtsikko.setFont(fonttiIsompi);
        mokinAlueOtsikko.setFont(fonttiIsompi);
        mokinHintaOtsikko.setFont(fonttiIsompi);
        mokinHloMaaraOtsikko.setFont(fonttiIsompi);

        mokkiTaulukko.add(mokkitunnusOtsikko, 0, 1);
        mokkiTaulukko.add(mokinnimiOtsikko, 1, 1);
        mokkiTaulukko.add(mokinAlueOtsikko, 2, 1);
        mokkiTaulukko.add(mokinHintaOtsikko, 3, 1);
        mokkiTaulukko.add(mokinHloMaaraOtsikko, 4, 1);


        int rivi = 2;
        for (Mokki obj : mokkiLista) {
            Text mokkiID = new Text(String.valueOf(obj.getID()));
            mokkiID.setFont(fonttiIsompi);
            Text mokkiNimi = new Text(String.valueOf(obj.getNimi()));
            mokkiNimi.setFont(fonttiIsompi);
            Text mokkiAlue = new Text(String.valueOf(obj.getAlue().getNimi()));
            mokkiAlue.setFont(fonttiIsompi);
            Text mokkiHinta = new Text(obj.getHinta() + " €");
            mokkiHinta.setFont(fonttiIsompi);
            Text mokkiHloMaara = new Text(String.valueOf(obj.getHloMaara()));
            mokkiHloMaara.setFont(fonttiIsompi);

            mokkiTaulukko.add(mokkiID, 0, rivi);
            mokkiTaulukko.add(mokkiNimi, 1, rivi);
            mokkiTaulukko.add(mokkiAlue, 2, rivi);
            mokkiTaulukko.add(mokkiHinta, 3, rivi);
            mokkiTaulukko.add(mokkiHloMaara, 4, rivi);

            // Onko varattu valitulla välillä
            if (!(paivaAlku == null || paivaLoppu == null)) {
                for (Varaus v : varausLista) {
                    if (v.varattuValilla(obj, paivaAlku, paivaLoppu)) {
                        mokkiNimi.setFill(Color.RED);
                    }
                }
            }

            Nappula poistoNappula = new Nappula(150, 30);
            ImageView roskis = new ImageView(imageKuvasta("roskis.png"));
            roskis.setFitWidth(22);
            roskis.setFitHeight(22);
            poistoNappula.setGraphic(roskis);
            mokkiTaulukko.add(poistoNappula, 5, rivi);
            // Ikkuna mökin poistamiseen
            poistoNappula.setOnMouseClicked(e -> {
                PoistoIkkuna poistaMokkiIkkuna = new PoistoIkkuna("mökki", "mökin",
                        "Kaikki mökkeihin kuuluvat varaukset \npoistetaan samalla.");

                poistaMokkiIkkuna.getPoistoNappula().setOnAction( event -> {
                    try {
                        tietokanta.poistaMokki(obj.getID());
                        haeKaikkiTiedot();
                        poistaMokkiIkkuna.getIkkuna().close();
                        paivitaMokkiTaulukko(paivaAlku, paivaLoppu);
                    } catch (SQLException ex) {
                        ilmoitusPaneeli.lisaaIlmoitus(IlmoitusTyyppi.VAROITUS, "Virhe mökin poistamisessa.");
                        throw new RuntimeException(ex); // TEMP
                    }
                });
            });

            Nappula muokkausNappula = new Nappula(100, 30);
            ImageView muokkaus = new ImageView(imageKuvasta("muokkaus.png"));
            muokkaus.setFitWidth(23);
            muokkaus.setFitHeight(22);
            muokkausNappula.setGraphic(muokkaus);
            mokkiTaulukko.add(muokkausNappula, 6, rivi);

            // Ikkuna mökin muokkaamiseen
            muokkausNappula.setOnMouseClicked(e -> {
                Stage mokkiMuokkausIkkuna = new Stage();
                mokkiMuokkausIkkuna.show();
                mokkiMuokkausIkkuna.setTitle("Muokkaa mökkiä");
                VBox mokkiMuokkausPaneeli = new VBox();
                mokkiMuokkausPaneeli.setPadding(new Insets(25));
                mokkiMuokkausPaneeli.setSpacing(15);

                GridPane mokkiMuokkausGridPaneeli = new GridPane();
                mokkiMuokkausGridPaneeli.setVgap(15);
                mokkiMuokkausGridPaneeli.setHgap(15);

                Text mokkiMuokkausTeksti = new Text("Syötä mökin tiedot.");
                mokkiMuokkausGridPaneeli.add(mokkiMuokkausTeksti, 0, 0);

                Text mokkiNimiTeksti = new Text("Mökin nimi: ");
                TextField mokinNimi = new TextField(obj.getNimi());
                Text mokkiAlueTeksti = new Text("AlueID: ");
                TextField mokinAlue = new TextField(String.valueOf(obj.getAlue().getID()));
                Text mokkiOsoiteTeksti = new Text("Katuosoite: ");
                TextField mokkiOsoite = new TextField(obj.getKatuosoite());
                Text mokkiPostinroTeksti = new Text("Postinro: ");
                TextField mokkiPostinro = new TextField(obj.getPostiNro().getPostiNro());
                Text mokkiPostitoimipaikkaTeksti = new Text("Postitoimipaikka: ");
                TextField mokkiPostitoimipaikka = new TextField(obj.getPostiNro().getToimipaikka());
                Text mokkiHintaTeksti = new Text("Hinta/vrk(€): ");
                TextField mokinHinta = new TextField(String.valueOf(obj.getHinta()));
                Text mokkiKuvausTeksti = new Text("Kuvaus: ");
                TextArea mokkiKuvaus = new TextArea(obj.getKuvaus());
                mokkiKuvaus.setWrapText(true);
                Text mokkiHlomaaraTeksti = new Text("Henkilömäärä: ");
                TextField mokkiHlomaara = new TextField(String.valueOf(obj.getHloMaara()));
                Text mokkiVarusteluTeksti = new Text("Varustelu: ");
                TextField mokkiVarustelu = new TextField(obj.getVarustelu());

                mokkiMuokkausGridPaneeli.add(mokkiNimiTeksti, 0, 1);
                mokkiMuokkausGridPaneeli.add(mokinNimi, 1, 1);
                mokkiMuokkausGridPaneeli.add(mokkiAlueTeksti, 0, 2);
                mokkiMuokkausGridPaneeli.add(mokinAlue, 1, 2);
                mokkiMuokkausGridPaneeli.add(mokkiOsoiteTeksti, 0, 3);
                mokkiMuokkausGridPaneeli.add(mokkiOsoite, 1, 3);
                mokkiMuokkausGridPaneeli.add(mokkiPostinroTeksti, 0, 4);
                mokkiMuokkausGridPaneeli.add(mokkiPostinro, 1, 4);
                mokkiMuokkausGridPaneeli.add(mokkiPostitoimipaikkaTeksti, 0, 5);
                mokkiMuokkausGridPaneeli.add(mokkiPostitoimipaikka, 1, 5);
                mokkiMuokkausGridPaneeli.add(mokkiHintaTeksti, 0, 6);
                mokkiMuokkausGridPaneeli.add(mokinHinta, 1, 6);
                mokkiMuokkausGridPaneeli.add(mokkiKuvausTeksti, 0, 7);
                mokkiMuokkausGridPaneeli.add(mokkiKuvaus, 1, 7);
                mokkiMuokkausGridPaneeli.add(mokkiHlomaaraTeksti, 0, 8);
                mokkiMuokkausGridPaneeli.add(mokkiHlomaara, 1, 8);
                mokkiMuokkausGridPaneeli.add(mokkiVarusteluTeksti, 0, 9);
                mokkiMuokkausGridPaneeli.add(mokkiVarustelu, 1, 9);

                Nappula muokkaaMokkiNappula = new Nappula("Tallenna muutokset");

                muokkaaMokkiNappula.setOnAction( event -> {
                    try {
                        if (etsiPostiNro(postiLista, mokkiPostinro.getText()) == null) {
                            tietokanta.insertPosti(mokkiPostinro.getText(), mokkiPostitoimipaikka.getText());
                        }

                        tietokanta.muokkaaMokki(
                                obj.getID(),
                                Integer.parseInt(mokinAlue.getText()),
                                mokkiPostinro.getText(),
                                mokinNimi.getText(),
                                mokkiOsoite.getText(),
                                BigDecimal.valueOf(Double.parseDouble(String.valueOf(mokinHinta.getText()))),
                                mokkiKuvaus.getText(),
                                Integer.parseInt(mokkiHlomaara.getText()),
                                mokkiVarustelu.getText()
                        );
                        mokkiMuokkausIkkuna.close();
                        haeKaikkiTiedot();
                        paivitaMokkiTaulukko(paivaAlku, paivaLoppu);
                    } catch (SQLException ex) {

                        throw new RuntimeException(ex); // TEMP
                        //mokkiMuokkausTeksti.setText("Mökin muokkaaminen ei onnistunut. \n " +
                        //        "Tarkista syötteet ja yritä uudelleen.");
                        //mokkiMuokkausTeksti.setFill(Color.RED);

                    }
                });

                mokkiMuokkausPaneeli.getChildren().addAll(mokkiMuokkausGridPaneeli, muokkaaMokkiNappula);

                Scene mokkiMuokkausKehys = new Scene(mokkiMuokkausPaneeli, 400, 550);
                mokkiMuokkausIkkuna.setScene(mokkiMuokkausKehys);
            });

            Nappula tarkasteleNappula = new Nappula(170, 30);
            ImageView tarkastelu = new ImageView(imageKuvasta("tarkastelu.png"));
            tarkastelu.setFitWidth(23);
            tarkastelu.setFitHeight(22);
            tarkasteleNappula.setGraphic(tarkastelu);
            mokkiTaulukko.add(tarkasteleNappula, 7, rivi);

            // Ikkuna mökin tarkasteluun
            tarkasteleNappula.setOnMouseClicked(e -> {
                Stage tarkasteleMokkiIkkuna = new Stage();
                tarkasteleMokkiIkkuna.show();
                GridPane tarkasteleMokkiPaneeli = new GridPane();
                tarkasteleMokkiPaneeli.setPadding(new Insets(25));
                tarkasteleMokkiPaneeli.setVgap(15);
                tarkasteleMokkiPaneeli.setHgap(15);
                Scene tarkasteleMokkiKehys = new Scene(tarkasteleMokkiPaneeli, 400, 400);
                tarkasteleMokkiIkkuna.setScene(tarkasteleMokkiKehys);

                Text mokinTiedot = new Text("Mökin tiedot");
                mokinTiedot.setFont(fonttiPaksu);
                tarkasteleMokkiPaneeli.add(mokinTiedot,0,0);
                tarkasteleMokkiPaneeli.add(new Text("MökkiID: "),0,1);
                tarkasteleMokkiPaneeli.add(new Text("Mökin nimi: "),0,2);
                tarkasteleMokkiPaneeli.add(new Text("Alue: "),0,3);
                tarkasteleMokkiPaneeli.add(new Text("Katuosoite: "),0,4);
                tarkasteleMokkiPaneeli.add(new Text("Postinumero: "),0,5);
                tarkasteleMokkiPaneeli.add(new Text("Hinta/vrk(€): "),0,6);
                tarkasteleMokkiPaneeli.add(new Text("Kuvaus: "),0,7);
                tarkasteleMokkiPaneeli.add(new Text("Henkilömäärä: "),0,8);
                tarkasteleMokkiPaneeli.add(new Text("Varustelu: "),0,9);

                tarkasteleMokkiPaneeli.add(new Text(String.valueOf(obj.getID())),1,1);
                tarkasteleMokkiPaneeli.add(new Text(obj.getNimi()),1,2);
                tarkasteleMokkiPaneeli.add(new Text(obj.getAlue().getNimi()),1,3);
                tarkasteleMokkiPaneeli.add(new Text(obj.getKatuosoite()),1,4);
                tarkasteleMokkiPaneeli.add(new Text(obj.getPostiNro().getPostiNro()),1,5);
                tarkasteleMokkiPaneeli.add(new Text(String.valueOf(obj.getHinta())),1,6);
                tarkasteleMokkiPaneeli.add(new Text(obj.getKuvaus()),1,7);
                tarkasteleMokkiPaneeli.add(new Text(String.valueOf(obj.getHloMaara())),1,8);
                tarkasteleMokkiPaneeli.add(new Text(obj.getVarustelu()),1,9);
            });

            rivi++;
        }
    }
    
    public void luoPalvelunakyma() {
        BorderPane palvelupaneeli = new BorderPane();
        palvelunappula.setOnAction(e -> {
            paneeli.setCenter(palvelupaneeli);
            isoOtsikkoTeksti.setText("PALVELUT");
            paivitaPalveluTaulukko();
        });

        GridPane palveluHaku = new GridPane();
        palveluHaku.setPadding(new Insets(HAKU_PADDING));
        palveluHaku.setHgap(HAKU_HGAP);
        palveluHaku.setVgap(HAKU_VGAP);
        palvelupaneeli.setTop(palveluHaku);

        TextField palveluHakuKentta = new TextField();
        Label palveluHakuKenttaLabel = new Label("Hae palveluita: ", palveluHakuKentta);
        palveluHakuKenttaLabel.setFont(fonttiIsompi);
        palveluHakuKenttaLabel.setContentDisplay(ContentDisplay.RIGHT);
        palveluHaku.add(palveluHakuKenttaLabel, 1, 1);
        Nappula palveluHakuNappula = new Nappula("Suorita haku", 190, 30);
        palveluHaku.add(palveluHakuNappula, 1, 2);

        Text palveluLajitteluTeksti = new Text("Lajittelu");
        palveluLajitteluTeksti.setFont(fonttiPaksu);
        palveluHaku.add(palveluLajitteluTeksti, 2, 0);
        ComboBox<String> palveluLajittelu = new ComboBox<>(FXCollections.observableList(Arrays.asList(
                "Tunnuksen mukaan",
                "Uusin > Vanhin",
                "Vanhin > Uusin",
                "A > Ö",
                "Alueittain"
        )));
        palveluLajittelu.setValue("Tunnuksen mukaan"); // Oletuksena valittu vaihtoehto
        palveluHaku.add(palveluLajittelu, 2, 1);

        palveluHakuNappula.setOnAction( e -> {
            // Suodatus
            // TODO

            // Lajittelu
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

            paivitaPalveluTaulukko();
        });

        ScrollPane palveluScrollaus = new ScrollPane();
        palvelupaneeli.setCenter(palveluScrollaus);
        palveluScrollaus.setContent(palveluTaulukko);


        ImageView palvelunLisays = new ImageView(imageKuvasta("lisays.png"));
        palvelunLisays.setFitWidth(23);
        palvelunLisays.setFitHeight(22);
        palvelunLisaysNappula.setGraphic(palvelunLisays);
        // Ikkuna palvelun lisäämiseen
        palvelunLisaysNappula.setOnAction( e -> {

            Stage palveluLisaysIkkuna = new Stage();
            palveluLisaysIkkuna.show();
            palveluLisaysIkkuna.setTitle("Lisää palvelu");
            VBox palveluLisaysPaneeli = new VBox();
            palveluLisaysPaneeli.setPadding(new Insets(25));
            palveluLisaysPaneeli.setSpacing(15);

            GridPane palveluLisaysGridPaneeli = new GridPane();
            palveluLisaysGridPaneeli.setVgap(15);
            palveluLisaysGridPaneeli.setHgap(15);

            Text palveluLisaysTeksti = new Text("Syötä palvelun tiedot.");
            palveluLisaysGridPaneeli.add(palveluLisaysTeksti, 0, 0);

            Text palveluAlueTeksti = new Text("AlueID: ");
            TextField palveluAlue = new TextField();
            Text palveluNimiTeksti = new Text("Palvelun nimi: ");
            TextField palveluNimi = new TextField();
            Text palveluTyyppiTeksti = new Text("Palvelun tyyppi: ");
            TextField palveluTyyppi = new TextField();
            Text palveluKuvausTeksti = new Text("Kuvaus: ");
            TextArea palveluKuvaus = new TextArea();
            palveluKuvaus.setWrapText(true);
            Text palveluHintaTeksti = new Text("Hinta(€): ");
            TextField palveluHinta = new TextField();
            Text palveluAlvTeksti = new Text("Alv(%): ");
            TextField palveluAlv = new TextField();

            palveluLisaysGridPaneeli.add(palveluAlueTeksti, 0, 1);
            palveluLisaysGridPaneeli.add(palveluAlue, 1, 1);
            palveluLisaysGridPaneeli.add(palveluNimiTeksti, 0, 2);
            palveluLisaysGridPaneeli.add(palveluNimi, 1, 2);
            palveluLisaysGridPaneeli.add(palveluTyyppiTeksti, 0, 3);
            palveluLisaysGridPaneeli.add(palveluTyyppi, 1, 3);
            palveluLisaysGridPaneeli.add(palveluKuvausTeksti, 0, 4);
            palveluLisaysGridPaneeli.add(palveluKuvaus, 1, 4);
            palveluLisaysGridPaneeli.add(palveluHintaTeksti, 0, 5);
            palveluLisaysGridPaneeli.add(palveluHinta, 1, 5);
            palveluLisaysGridPaneeli.add(palveluAlvTeksti, 0, 6);
            palveluLisaysGridPaneeli.add(palveluAlv, 1, 6);

            Nappula lisaaPalveluNappula = new Nappula("Lisää palvelu");

            lisaaPalveluNappula.setOnAction( event -> {
                try {
                    tietokanta.insertPalvelu(
                            Integer.parseInt(palveluAlue.getText()),
                            palveluNimi.getText(),
                            Integer.parseInt(palveluTyyppi.getText()),
                            palveluKuvaus.getText(),
                            BigDecimal.valueOf(Double.parseDouble(palveluHinta.getText())),
                            Integer.parseInt(palveluAlv.getText())
                    );
                    palveluLisaysIkkuna.close();
                    haeKaikkiTiedot();
                    paivitaPalveluTaulukko();
                } catch (SQLException ex) {
                    palveluLisaysTeksti.setText("Palvelun lisääminen ei onnistunut. \n " +
                            "Tarkista syötteet ja yritä uudelleen.");
                    palveluLisaysTeksti.setFill(Color.RED);
                }
            });

            palveluLisaysPaneeli.getChildren().addAll(palveluLisaysGridPaneeli, lisaaPalveluNappula);

            Scene palveluLisaysKehys = new Scene(palveluLisaysPaneeli, 400, 400);
            palveluLisaysIkkuna.setScene(palveluLisaysKehys);
            
        });
    }

    public void paivitaPalveluTaulukko() {

        palveluTaulukko.setGridLinesVisible(false);
        palveluTaulukko.getColumnConstraints().clear();
        palveluTaulukko.getChildren().clear();
        palveluTaulukko.getColumnConstraints().addAll(
                sarakeLyhyt, sarakeLevea, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt);
        palveluTaulukko.setGridLinesVisible(true);

        palveluTaulukko.setPadding(new Insets(20));

        palveluTaulukko.add(palvelunLisaysNappula, 1,0);

        Text palvelutunnusOtsikko = new Text("Tunnus");
        Text palvelunnimiOtsikko = new Text("Palvelu");
        Text palveluAlueOtsikko = new Text("Alue");
        Text palvelunHintaOtsikko = new Text("Hinta");
        palvelutunnusOtsikko.setFont(fonttiIsompi);
        palvelunnimiOtsikko.setFont(fonttiIsompi);
        palveluAlueOtsikko.setFont(fonttiIsompi);
        palvelunHintaOtsikko.setFont(fonttiIsompi);

        palveluTaulukko.add(palvelutunnusOtsikko, 0, 1);
        palveluTaulukko.add(palvelunnimiOtsikko, 1, 1);
        palveluTaulukko.add(palveluAlueOtsikko, 2, 1);
        palveluTaulukko.add(palvelunHintaOtsikko, 3, 1);


        int rivi = 2;
        for (Palvelu obj : palveluLista) {
            Text palveluID = new Text(String.valueOf(obj.getID()));
            Text palveluNimi = new Text(String.valueOf(obj.getNimi()));
            Text palveluAlue = new Text(String.valueOf(obj.getAlue().getNimi()));
            Text palveluHinta = new Text(obj.getHinta() + " €");
            palveluID.setFont(fonttiIsompi);
            palveluNimi.setFont(fonttiIsompi);
            palveluAlue.setFont(fonttiIsompi);
            palveluHinta.setFont(fonttiIsompi);
            palveluID.setTextAlignment(TextAlignment.CENTER);
            palveluNimi.setTextAlignment(TextAlignment.CENTER);

            palveluTaulukko.add(palveluID, 0, rivi);
            palveluTaulukko.add(palveluNimi, 1, rivi);
            palveluTaulukko.add(palveluAlue, 2, rivi);
            palveluTaulukko.add(palveluHinta, 3, rivi);


            Nappula poistoNappula = new Nappula(150, 30);
            ImageView roskis = new ImageView(imageKuvasta("roskis.png"));
            roskis.setFitWidth(22);
            roskis.setFitHeight(22);
            poistoNappula.setGraphic(roskis);
            palveluTaulukko.add(poistoNappula, 4, rivi);

            // Ikkuna palvelun poistamiseen
            poistoNappula.setOnMouseClicked(e -> {
                PoistoIkkuna poistaPalveluIkkuna = new PoistoIkkuna("palvelu", "palvelun",
                        "Kaikki palvelun varaustiedot poistetaan samalla.");

                poistaPalveluIkkuna.getPoistoNappula().setOnAction( event -> {
                    try {
                        tietokanta.poistaPalvelu(obj.getID());
                        haeKaikkiTiedot();
                        poistaPalveluIkkuna.getIkkuna().close();
                        paivitaPalveluTaulukko();
                    } catch (SQLException ex) {
                        ilmoitusPaneeli.lisaaIlmoitus(IlmoitusTyyppi.VAROITUS, "Virhe palvelun poistamisessa");
                        throw new RuntimeException(ex); // TEMP
                    }

                });            
            });

            Nappula muokkausNappula = new Nappula(100, 30);
            ImageView muokkaus = new ImageView(imageKuvasta("muokkaus.png"));
            muokkaus.setFitWidth(23);
            muokkaus.setFitHeight(22);
            muokkausNappula.setGraphic(muokkaus);
            palveluTaulukko.add(muokkausNappula, 5, rivi);

            // Ikkuna palvelun muokkaamiseen
            muokkausNappula.setOnMouseClicked(e -> {
                Stage palveluMuokkausIkkuna = new Stage();
                palveluMuokkausIkkuna.show();
                palveluMuokkausIkkuna.setTitle("Muokkaa palvelun tietoja");
                VBox palveluMuokkausPaneeli = new VBox();
                palveluMuokkausPaneeli.setPadding(new Insets(25));
                palveluMuokkausPaneeli.setSpacing(15);

                GridPane palveluMuokkausGridPaneeli = new GridPane();
                palveluMuokkausGridPaneeli.setVgap(15);
                palveluMuokkausGridPaneeli.setHgap(15);

                Text palveluMuokkausTeksti = new Text("Muokkaa palvelun tietoja.");
                palveluMuokkausGridPaneeli.add(palveluMuokkausTeksti, 0, 0);


                Text palveluAlueTeksti = new Text("AlueID: ");
                TextField palvelunAlue = new TextField(String.valueOf(obj.getAlue().getID()));
                Text palveluNimiTeksti = new Text("Palvelun nimi: ");
                TextField palvelunNimi = new TextField(obj.getNimi());
                Text palveluTyyppiTeksti = new Text("Palvelun tyyppi: ");
                TextField palveluTyyppi = new TextField(String.valueOf(obj.getTyyppi()));
                Text palveluKuvausTeksti = new Text("Kuvaus: ");
                TextArea palveluKuvaus = new TextArea(obj.getKuvaus());
                palveluKuvaus.setWrapText(true);
                Text palveluHintaTeksti = new Text("Hinta(€): ");
                TextField palvelunHinta = new TextField(String.valueOf(obj.getHinta()));
                Text palveluAlvTeksti = new Text("Alv(%): ");
                TextField palveluAlv = new TextField(String.valueOf(obj.getAlv()));

                palveluMuokkausGridPaneeli.add(palveluAlueTeksti, 0, 1);
                palveluMuokkausGridPaneeli.add(palvelunAlue, 1, 1);
                palveluMuokkausGridPaneeli.add(palveluNimiTeksti, 0, 2);
                palveluMuokkausGridPaneeli.add(palvelunNimi, 1, 2);
                palveluMuokkausGridPaneeli.add(palveluTyyppiTeksti, 0, 3);
                palveluMuokkausGridPaneeli.add(palveluTyyppi, 1, 3);
                palveluMuokkausGridPaneeli.add(palveluKuvausTeksti, 0, 4);
                palveluMuokkausGridPaneeli.add(palveluKuvaus, 1, 4);
                palveluMuokkausGridPaneeli.add(palveluHintaTeksti, 0, 5);
                palveluMuokkausGridPaneeli.add(palvelunHinta, 1, 5);
                palveluMuokkausGridPaneeli.add(palveluAlvTeksti, 0, 6);
                palveluMuokkausGridPaneeli.add(palveluAlv, 1, 6);

                Nappula lisaaPalveluNappula = new Nappula("Tallenna muutokset");

                lisaaPalveluNappula.setOnAction( event -> {
                    try {
                        tietokanta.muokkaaPalvelu(
                                obj.getID(),
                                Integer.parseInt(palvelunAlue.getText()),
                                palvelunNimi.getText(),
                                Integer.parseInt(palveluTyyppi.getText()),
                                palveluKuvaus.getText(),
                                BigDecimal.valueOf(Double.parseDouble(palvelunHinta.getText())),
                                Integer.parseInt(palveluAlv.getText())
                        );
                        palveluMuokkausIkkuna.close();
                        haeKaikkiTiedot();
                        paivitaPalveluTaulukko();
                    } catch (SQLException ex) {
                        palveluMuokkausTeksti.setText("Muutosten tallentaminen ei onnistunut. \n " +
                                "Tarkista syötteet ja yritä uudelleen.");
                        palveluMuokkausTeksti.setFill(Color.RED);
                    }
                });

                palveluMuokkausPaneeli.getChildren().addAll(palveluMuokkausGridPaneeli, lisaaPalveluNappula);

                Scene palveluMuokkausKehys = new Scene(palveluMuokkausPaneeli, 400, 400);
                palveluMuokkausIkkuna.setScene(palveluMuokkausKehys);
            });
            
            Nappula tarkasteleNappula = new Nappula(170, 30);
            ImageView tarkastelu = new ImageView(imageKuvasta("tarkastelu.png"));
            tarkastelu.setFitWidth(23);
            tarkastelu.setFitHeight(22);
            tarkasteleNappula.setGraphic(tarkastelu);
            palveluTaulukko.add(tarkasteleNappula, 6, rivi);

            // Ikkuna palvelun tarkasteluun
            tarkasteleNappula.setOnMouseClicked(e -> {
                
                Stage tarkastelePalveluIkkuna = new Stage();
                tarkastelePalveluIkkuna.show();
                GridPane tarkastelePalveluPaneeli = new GridPane();
                tarkastelePalveluPaneeli.setPadding(new Insets(25));
                tarkastelePalveluPaneeli.setVgap(15);
                tarkastelePalveluPaneeli.setHgap(15);
                Scene tarkastelePalveluKehys = new Scene(tarkastelePalveluPaneeli, 400, 300);
                tarkastelePalveluIkkuna.setScene(tarkastelePalveluKehys);

                Text palvelunTiedot = new Text("Palvelun tiedot");
                palvelunTiedot.setFont(fonttiPaksu);
                tarkastelePalveluPaneeli.add(palvelunTiedot,0,0);
                tarkastelePalveluPaneeli.add(new Text("PalveluID: "),0,1);
                tarkastelePalveluPaneeli.add(new Text("Palvelun nimi: "),0,2);
                tarkastelePalveluPaneeli.add(new Text("Alue: "),0,3);
                tarkastelePalveluPaneeli.add(new Text("Tyyppi: "),0,4);
                tarkastelePalveluPaneeli.add(new Text("Kuvaus: "),0,5);
                tarkastelePalveluPaneeli.add(new Text("Hinta(€): "),0,6);
                tarkastelePalveluPaneeli.add(new Text("Alv: "),0,7);

                tarkastelePalveluPaneeli.add(new Text(String.valueOf(obj.getID())),1,1);
                tarkastelePalveluPaneeli.add(new Text(obj.getNimi()),1,2);
                tarkastelePalveluPaneeli.add(new Text(obj.getAlue().getNimi()),1,3);
                tarkastelePalveluPaneeli.add(new Text(String.valueOf(obj.getTyyppi())),1,4);
                tarkastelePalveluPaneeli.add(new Text(obj.getKuvaus()),1,5);
                tarkastelePalveluPaneeli.add(new Text(String.valueOf(obj.getHinta())),1,6);
                tarkastelePalveluPaneeli.add(new Text(String.valueOf(obj.getAlv())),1,7);
            });

            rivi++;
        }
    }

    public void luoVarausnakyma() {
        BorderPane varauspaneeli = new BorderPane();
        varausnappula.setOnAction(e -> {
            paneeli.setCenter(varauspaneeli);
            isoOtsikkoTeksti.setText("VARAUKSET");
            paivitaVarausTaulukko(varausLista);
        });

        GridPane varausHaku = new GridPane();
        varausHaku.setPadding(new Insets(HAKU_PADDING));
        varausHaku.setHgap(HAKU_HGAP);
        varausHaku.setVgap(HAKU_VGAP);
        varauspaneeli.setTop(varausHaku);

        TextField varausHakuKentta = new TextField();
        Label varausHakuKenttaLabel = new Label("Hae varausta: ", varausHakuKentta); // TODO tarvitaanko tätä?
        varausHakuKenttaLabel.setFont(fonttiIsompi);
        varausHakuKenttaLabel.setContentDisplay(ContentDisplay.RIGHT);
        varausHaku.add(varausHakuKenttaLabel, 0, 1);
        Nappula varausHakuNappula = new Nappula("Suorita haku", 190, 30);
        varausHaku.add(varausHakuNappula, 0, 2);


        Nappula varausRaporttiNappula= new Nappula("Luo varausraportti", 190, 30);
        varausHaku.add(varausRaporttiNappula, 6, 2);

        varausRaporttiNappula.setOnAction( event -> {
            String tiedostonNimi = "Varausraportti.pdf"; // PDF-tiedoston nimi
            Document dokumentti = new Document();

            try {
                // Luodaan PdfWriter osoittamaan tiedostoon
                PdfWriter.getInstance(dokumentti, new FileOutputStream(tiedostonNimi));

                // Avataan dokumentti
                dokumentti.open();

                PdfPTable varausTaulukko = new PdfPTable(6);
                varausTaulukko.setWidths(new int[]{4,4,4,4,4,4});

                // Lisää taulukon otsikkorivi

                varausTaulukko.addCell(new PdfPCell(new Paragraph("Varaus ID")));
                varausTaulukko.addCell(new PdfPCell(new Paragraph("Asiakkaan nimi")));
                varausTaulukko.addCell(new PdfPCell(new Paragraph("Mökin nimi")));
                varausTaulukko.addCell(new PdfPCell(new Paragraph("Varauksen alku")));
                varausTaulukko.addCell(new PdfPCell(new Paragraph("Varauksen loppu")));
                varausTaulukko.addCell(new PdfPCell(new Paragraph("Varauksen alue")));
                //varausTaulukko.addCell(new PdfPCell(new Paragraph("Varauksen palvelun nimi")));
                //varausTaulukko.addCell(new PdfPCell(new Paragraph("Varauksen palvelujen lukumäärä")));





                // Lisää ArrayListin tiedot taulukkoon
                for (Varaus v : varausLista) { //Tämä ei toimi vielä

                    varausTaulukko.addCell(new PdfPCell(new Paragraph(String.valueOf(v.getID()))));
                    varausTaulukko.addCell(new PdfPCell(new Paragraph(String.valueOf(v.getAsiakas().getNimi(false)))));
                    varausTaulukko.addCell(new PdfPCell(new Paragraph(String.valueOf(v.getMokki().getNimi()))));
                    varausTaulukko.addCell(new PdfPCell(new Paragraph(String.valueOf(v.getVarausAlkuPvm()))));
                    varausTaulukko.addCell(new PdfPCell(new Paragraph(String.valueOf(v.getVarausLoppuPvm()))));
                    varausTaulukko.addCell(new PdfPCell(new Paragraph(String.valueOf(v.getMokki().getAlue()))));
                    //varausTaulukko.addCell(new PdfPCell(new Paragraph(String.valueOf(v.getPalvelut().get()))));

                }

                dokumentti.add(varausTaulukko);

                // Suljetaan dokumentti
                dokumentti.close();

                System.out.println("Raportti luotu onnistuneesti!");

            } catch (DocumentException | FileNotFoundException i) {
                System.out.println("Virhe raportin generoimisessa: " + i.getMessage());
            }
            try {
                // Avataan dokumentti oletusohjelmalla
                Desktop.getDesktop().open(new File(tiedostonNimi));
            } catch (IOException i) {
                System.out.println("Virhe tiedoston avaamisessa: " + i.getMessage());
            }
        });

        Text varausLajitteluTeksti = new Text("Lajittelu");
        varausLajitteluTeksti.setFont(fonttiPaksu);
        varausHaku.add(varausLajitteluTeksti, 1, 0);
        ComboBox<String> varausLajittelu = new ComboBox<>(FXCollections.observableList(Arrays.asList(
                "Tunnuksen mukaan",
                "Uusin > Vanhin",
                "Vanhin > Uusin",
                "A > Ö",
                "Alueittain"
        )));
        varausLajittelu.setValue("Tunnuksen mukaan"); // Oletuksena valittu vaihtoehto
        varausHaku.add(varausLajittelu, 1, 1);

        Text pvmSuodatusTeksti = new Text("Varauksen ajankohta");
        pvmSuodatusTeksti.setFont(fonttiPaksu);
        varausHaku.add(pvmSuodatusTeksti, 2, 0);

        DatePicker varausPvmAlku = new DatePicker();
        DatePicker varausPvmLoppu = new DatePicker();
        varausPvmAlku.setPrefWidth(120);
        varausPvmLoppu.setPrefWidth(120);
        Label pvmSuodatusAlkuLabel = new Label("Alku");
        Label pvmSuodatusLoppuLabel = new Label("Loppu");
        pvmSuodatusAlkuLabel.setTextAlignment(TextAlignment.RIGHT);
        pvmSuodatusLoppuLabel.setTextAlignment(TextAlignment.RIGHT);
        varausHaku.add(pvmSuodatusAlkuLabel, 2, 1);
        varausHaku.add(pvmSuodatusLoppuLabel, 2, 2);
        varausHaku.add(varausPvmAlku, 3, 1);
        varausHaku.add(varausPvmLoppu, 3, 2);
        GridPane.setColumnSpan(pvmSuodatusTeksti, 2);

        TextField varausAikaAlku = new TextField();
        TextField varausAikaLoppu = new TextField();
        varausAikaAlku.setPrefWidth(100);
        varausAikaLoppu.setPrefWidth(100);
        Label varausAikaAlkuLabel = new Label("klo", varausAikaAlku);
        Label varausAikaLoppuLabel = new Label("klo", varausAikaLoppu);
        varausAikaAlkuLabel.setContentDisplay(ContentDisplay.RIGHT);
        varausAikaLoppuLabel.setContentDisplay(ContentDisplay.RIGHT);
        varausHaku.add(varausAikaAlkuLabel, 4, 1);
        varausHaku.add(varausAikaLoppuLabel, 4, 2);

        Text alueSuodatusTeksti = new Text("Varauksen alue");
        alueSuodatusTeksti.setFont(fonttiPaksu);
        ComboBox<Alue> alueSuodatus = new ComboBox<>(FXCollections.observableArrayList(alueLista)); // TODO nämä tekstit jotenkin paremmin ja alueiden päivittyminen jos niitä muutetaan
        varausHaku.add(alueSuodatusTeksti, 5, 0);
        varausHaku.add(alueSuodatus, 5, 1);

        // Virheiden käsittely. Jos virheitä on, hakunappula on poistettu käytöstä.
        EventHandler<ActionEvent> tarkistaSyotteet = event -> {
            try {
                LocalDateTime.parse(varausPvmAlku.getValue() + " " + varausAikaAlku.getText(), dateTimeFormat);
                LocalDateTime.parse(varausPvmLoppu.getValue() + " " + varausAikaLoppu.getText(), dateTimeFormat);
                varausHakuNappula.setDisable(false);
            } catch (DateTimeParseException ex) {
                varausHakuNappula.setDisable(true);
            }
        };
        varausPvmAlku.setOnAction(tarkistaSyotteet);
        varausPvmLoppu.setOnAction(tarkistaSyotteet);
        varausAikaAlku.setOnAction(tarkistaSyotteet);
        varausAikaLoppu.setOnAction(tarkistaSyotteet);

        varausHakuNappula.setOnAction( e -> {
            LocalDateTime valittuAikaAlku = LocalDateTime.parse(
                    varausPvmAlku.getValue() + " " + varausAikaAlku.getText(), dateTimeFormat);
            LocalDateTime valittuAikaLoppu = LocalDateTime.parse(
                    varausPvmLoppu.getValue() + " " + varausAikaLoppu.getText(), dateTimeFormat);

            // Suodatus
            // TODO jos ei ole mitään valittuna niin kaikki tulokset tulee
            ArrayList<Varaus> varausTulokset = new ArrayList<>();
            for (Varaus v : varausLista) {
                LocalDateTime vAlkuAika = v.getVarausAlkuPvm();
                if ((vAlkuAika.isAfter(valittuAikaAlku) || vAlkuAika.isEqual(valittuAikaAlku)) &&
                        (vAlkuAika.isBefore(valittuAikaLoppu) || vAlkuAika.isEqual(valittuAikaAlku)) && // TODO onko näin tämä
                        v.getMokki().getAlue().equals(alueSuodatus.getValue())) {
                    varausTulokset.add(v);
                }
            }



            // Lajittelu
            switch (varausLajittelu.getValue()) {
                case "Tunnuksen mukaan" ->
                        varausTulokset.sort(Comparator.comparing(Varaus::getID));
                case "Uusin > Vanhin" ->
                        varausTulokset.sort(Comparator.comparing(Varaus::getVarausAlkuPvm).reversed());
                case "Vanhin > Uusin" ->
                        varausTulokset.sort(Comparator.comparing(Varaus::getVarausAlkuPvm));
                case "A > Ö" -> {} // TODO miten tämä toimii?
                case "Alueittain" -> varausTulokset.sort(Comparator.comparing(Varaus -> Varaus.getMokki().getAlue().getID())); // TODO lajitellaanko ID:n vai nimen mukaan
            }
            paivitaVarausTaulukko(varausTulokset);
        });

        ScrollPane varausScrollaus = new ScrollPane();
        varauspaneeli.setCenter(varausScrollaus);

        varausScrollaus.setContent(varausTaulukko);

        ImageView varausLisays = new ImageView(imageKuvasta("lisays.png"));
        varausLisays.setFitWidth(23);
        varausLisays.setFitHeight(22);
        varausLisaysNappula.setGraphic(varausLisays);

        // Ikkuna varauksen lisäämiseen
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
                    HashMap<Palvelu, Integer> varauksenPalvelut = new HashMap<>(); // TEMP varaukseen liittyviä palveluita varten
                    varauksenPalvelut.put(palveluLista.get(0), 1); // TEMP
                    varausLista.add(tietokanta.insertVaraus( // TODO tuleeko kenttään varattu_pvm tämänhetkinen aika?
                            asiakasIDInsert,
                            Integer.parseInt(mokkiID.getText()),
                            varauksenPalvelut, // TODO varauksen palvelut
                            LocalDateTime.now().format(dateTimeFormat),
                            null,
                            varausAlkuAika,
                            varausLoppuAika,
                            asiakasLista,
                            mokkiLista,
                            palveluLista));
                    paivitaVarausTaulukko(varausLista);
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
    }

    public void paivitaVarausTaulukko(ArrayList<Varaus> varausTulokset) {
        varausTaulukko.setGridLinesVisible(false);
        varausTaulukko.getColumnConstraints().clear();
        varausTaulukko.getChildren().clear();
        varausTaulukko.getColumnConstraints().addAll(
                sarakeLyhyt, sarakeLevea, sarakeLevea, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt);
        varausTaulukko.setGridLinesVisible(true);

        varausTaulukko.setPadding(new Insets(20));


        varausTaulukko.add(varausLisaysNappula, 1, 0);

        Text varaustunnusOtsikko = new Text("Tunnus");
        Text varausAsiakasOtsikko = new Text("Asiakas");
        Text varausAlueOtsikko = new Text("Alue");
        Text varausSummaOtsikko = new Text("Summa");
        Text varausMokkiOtsikko = new Text("Mökki");
        Text varausPalvelutOtsikko = new Text("Palvelut");
        varaustunnusOtsikko.setFont(fonttiIsompi);
        varausAsiakasOtsikko.setFont(fonttiIsompi);
        varausAlueOtsikko.setFont(fonttiIsompi);
        varausSummaOtsikko.setFont(fonttiIsompi);
        varausMokkiOtsikko.setFont(fonttiIsompi);
        varausPalvelutOtsikko.setFont(fonttiIsompi);

        varausTaulukko.add(varaustunnusOtsikko, 0, 1);
        varausTaulukko.add(varausAsiakasOtsikko, 1, 1);
        varausTaulukko.add(varausMokkiOtsikko, 2, 1);

        int rivi = 2;
        for (Varaus obj : varausTulokset) {
            Text varausID = new Text(String.valueOf(obj.getID()));
            Text varausNimi = new Text(String.valueOf(obj.getAsiakas().getNimi(false)));
            Text varausMokki = new Text(String.valueOf(obj.getMokki().getNimi()));
            varausID.setFont(fonttiIsompi);
            varausNimi.setFont(fonttiIsompi);
            varausMokki.setFont(fonttiIsompi);
            varausID.setTextAlignment(TextAlignment.CENTER);
            varausNimi.setTextAlignment(TextAlignment.CENTER);

            varausTaulukko.add(varausID, 0, rivi);
            varausTaulukko.add(varausNimi, 1, rivi);
            varausTaulukko.add(varausMokki, 2, rivi);


            Nappula poistoNappula = new Nappula(150, 30);
            ImageView roskis = new ImageView(imageKuvasta("roskis.png"));
            roskis.setFitWidth(22);
            roskis.setFitHeight(22);
            poistoNappula.setGraphic(roskis);
            varausTaulukko.add(poistoNappula, 3, rivi);

            // Ikkuna varauksen poistamiseen
            poistoNappula.setOnMouseClicked(e -> {
                PoistoIkkuna poistoIkkuna = new PoistoIkkuna("varaus", "varauksen");

                poistoIkkuna.getPoistoNappula().setOnAction( event -> {
                    try {
                        tietokanta.poistaVaraus(obj.getID());
                        haeKaikkiTiedot();
                        poistoIkkuna.getIkkuna().close();
                        paivitaVarausTaulukko(varausLista); // TODO päivitetäänkö varausTulokset vai tehdäänkö vaan aina niin että hakuvalinnat menee pois kun tekee tällaisen päivityksen
                    } catch (SQLException ex) {
                        ilmoitusPaneeli.lisaaIlmoitus(IlmoitusTyyppi.VAROITUS, "Virhe varauksen poistamisessa.");
                        throw new RuntimeException(ex); // TEMP
                    }
                });
            });



            Nappula muokkausNappula = new Nappula(100, 30);
            ImageView muokkaus = new ImageView(imageKuvasta("muokkaus.png"));
            muokkaus.setFitWidth(23);
            muokkaus.setFitHeight(22);
            muokkausNappula.setGraphic(muokkaus);
            varausTaulukko.add(muokkausNappula, 4, rivi);

            // Ikkuna varauksen muokkaamiseen
            muokkausNappula.setOnMouseClicked(e -> {
                Stage varausMuokkausIkkuna = new Stage();

                VBox varausMuokkausPaneeli = new VBox(10);
                varausMuokkausPaneeli.setPadding(new Insets(25));

                GridPane varausMuokkausGridPaneeli = new GridPane();
                varausMuokkausGridPaneeli.setVgap(5);
                varausMuokkausGridPaneeli.add(new Text("Muokkaa varauksen tietoja:"), 0, 0);
                TextField asiakasID = new TextField(String.valueOf(obj.getAsiakas().getID()));
                TextField mokkiID = new TextField(String.valueOf(obj.getMokki().getID()));
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
                    String varausAlkuAika = aloitusPvm.getValue() + " " + aloitusAika.getText(); // TODO muotoilun tarkistus, aka virheiden käsittely
                    String varausLoppuAika = lopetusPvm.getValue() + " " + lopetusAika.getText();
                    HashMap<Palvelu, Integer> varauksenPalvelut = new HashMap<>(); // TEMP varaukseen liittyviä palveluita varten
                    varauksenPalvelut.put(palveluLista.get(0), 1); // TEMP
                    try {
                        tietokanta.muokkaaVaraus( // TODO  asiakasLista.add()
                                obj.getID(),
                                Integer.parseInt(asiakasID.getText()),
                                Integer.parseInt(mokkiID.getText()),
                                varauksenPalvelut, // TODO varauksen palvelut
                                LocalDateTime.now().format(dateTimeFormat),
                                null,
                                varausAlkuAika,
                                varausLoppuAika);
                        varausMuokkausIkkuna.close();
                        haeKaikkiTiedot();
                        paivitaVarausTaulukko(varausLista);
                    } catch (SQLException ex) {
                        ilmoitusPaneeli.lisaaIlmoitus(IlmoitusTyyppi.VAROITUS, String.valueOf(ex));
                        throw new RuntimeException(ex); // TEMP
                    }
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

            // Ikkuna varauksen tarkasteluun
            tarkasteleNappula.setOnMouseClicked( e -> {
                Stage tarkasteleVarausIkkuna = new Stage();
                tarkasteleVarausIkkuna.show();
                GridPane tarkasteleVarausPaneeli = new GridPane();
                tarkasteleVarausPaneeli.setPadding(new Insets(25));
                tarkasteleVarausPaneeli.setVgap(15);
                tarkasteleVarausPaneeli.setHgap(15);
                ScrollPane tarkasteleVarausScroll = new ScrollPane(tarkasteleVarausPaneeli);
                Scene tarkasteleVarausKehys = new Scene(tarkasteleVarausScroll, 400, 400);
                tarkasteleVarausIkkuna.setScene(tarkasteleVarausKehys);

                Text varauksenTiedot = new Text("Varauksen tiedot");
                varauksenTiedot.setFont(fonttiPaksu);
                tarkasteleVarausPaneeli.add(varauksenTiedot,0,0);
                tarkasteleVarausPaneeli.add(new Text("VarausID: "),0,1);
                tarkasteleVarausPaneeli.add(new Text("Asiakkaan nimi: "),0,2);
                tarkasteleVarausPaneeli.add(new Text("AsiakasID: "),0,3);
                tarkasteleVarausPaneeli.add(new Text("Mökki: "),0,4);
                tarkasteleVarausPaneeli.add(new Text("Varattu: "),0,5);
                tarkasteleVarausPaneeli.add(new Text("Vahvistettu: "),0,6);
                tarkasteleVarausPaneeli.add(new Text("Varauksen alkupvm: "),0,7);
                tarkasteleVarausPaneeli.add(new Text("Varauksen loppupvm: "),0,8);
                tarkasteleVarausPaneeli.add(new Text("Varatut palvelut: "), 0, 9);
                Text varauksenPalvelut = new Text("Palvelu");
                Text varauksenPalvelutLkm = new Text("Lukumäärä");
                varauksenPalvelut.setFont(fonttiKursiivi);
                varauksenPalvelutLkm.setFont(fonttiKursiivi);
                tarkasteleVarausPaneeli.add(varauksenPalvelut, 0, 10);
                tarkasteleVarausPaneeli.add(varauksenPalvelutLkm, 1, 10);

                tarkasteleVarausPaneeli.add(new Text(String.valueOf(obj.getID())),1,1);
                tarkasteleVarausPaneeli.add(new Text(obj.getAsiakas().getNimi(false)),1,2);
                tarkasteleVarausPaneeli.add(new Text(String.valueOf(obj.getAsiakas().getID())),1,3);
                tarkasteleVarausPaneeli.add(new Text(obj.getMokki().getNimi()),1,4);
                tarkasteleVarausPaneeli.add(new Text(dateTimeFormat.format(obj.getVarattuPvm())),1,5);
                try {
                    tarkasteleVarausPaneeli.add(new Text(dateTimeFormat.format(obj.getVahvistusPvm())),1,6);
                } catch (NullPointerException ex) {
                    tarkasteleVarausPaneeli.add(new Text("Varausta ei ole vielä vahvistettu. "),1,6);
                }
                tarkasteleVarausPaneeli.add(new Text(dateTimeFormat.format(obj.getVarausAlkuPvm())),1,7);
                tarkasteleVarausPaneeli.add(new Text(dateTimeFormat.format(obj.getVarausLoppuPvm())),1,8);

                // Varaukseen liittyvät palvelut
                int riviVp = 11;
                for (Map.Entry<Palvelu, Integer> vp : obj.getPalvelut().entrySet()) {
                    tarkasteleVarausPaneeli.add(new Text(vp.getKey().getKuvaus()), 0, riviVp);
                    tarkasteleVarausPaneeli.add(new Text(String.valueOf(vp.getValue())), 1, riviVp);
                    riviVp++;
                }
            });

            rivi++;
        }
    }

    public void luoAsiakasnakyma() {
        BorderPane asiakaspaneeli = new BorderPane();
        asiakasnappula.setOnAction(e -> {
            paneeli.setCenter(asiakaspaneeli);
            isoOtsikkoTeksti.setText("ASIAKKAAT");
            paivitaAsiakasTaulukko(asiakasLista);
        });

        GridPane asiakasHaku = new GridPane();
        asiakasHaku.setPadding(new Insets(HAKU_PADDING));
        asiakasHaku.setHgap(HAKU_HGAP);
        asiakasHaku.setVgap(HAKU_VGAP);
        asiakaspaneeli.setTop(asiakasHaku);

        TextField asiakasHakuKentta = new TextField();
        Label asiakasHakuKenttaLabel = new Label("Hae asiakasta: ", asiakasHakuKentta);
        asiakasHakuKenttaLabel.setFont(fonttiIsompi);
        asiakasHakuKenttaLabel.setContentDisplay(ContentDisplay.RIGHT);
        asiakasHaku.add(asiakasHakuKenttaLabel, 1, 1);
        Nappula asiakasHakuNappula = new Nappula("Suorita haku", 190, 30);
        asiakasHaku.add(asiakasHakuNappula, 1, 2);

        Text asiakasLajitteluTeksti = new Text("Lajittelu");
        asiakasLajitteluTeksti.setFont(fonttiPaksu);
        asiakasHaku.add(asiakasLajitteluTeksti, 2, 0);
        ComboBox<String> asiakasLajittelu = new ComboBox<>(FXCollections.observableList(Arrays.asList(
                "Tunnuksen mukaan",
                "Uusin > Vanhin",
                "Vanhin > Uusin",
                "A > Ö"
        )));
        asiakasLajittelu.setValue("Tunnuksen mukaan"); // Oletuksena valittu vaihtoehto
        asiakasHaku.add(asiakasLajittelu, 2, 1);

        asiakasHakuNappula.setOnAction( e -> {
            ArrayList<Asiakas> asiakasTulokset = new ArrayList<>();

            // Suodatus
            // TODO

            // Lajittelu
            switch (asiakasLajittelu.getValue()) {
                case "Tunnuksen mukaan" ->
                        asiakasTulokset.sort(Comparator.comparing(Asiakas::getID));
                case "Uusin > Vanhin" -> {} // TODO miten tämä toimii?
                case "Vanhin > Uusin" -> {} // TODO miten tämä toimii?
                case "A > Ö" ->
                        asiakasTulokset.sort(Comparator.comparing(Asiakas::getSukunimi));
            }

            paivitaAsiakasTaulukko(asiakasTulokset);
        });

        ScrollPane asiakasScrollaus = new ScrollPane();
        asiakaspaneeli.setCenter(asiakasScrollaus);
        asiakasScrollaus.setContent(asiakasTaulukko);


        ImageView asiakasLisays = new ImageView(imageKuvasta("lisays.png"));
        asiakasLisays.setFitWidth(23);
        asiakasLisays.setFitHeight(22);
        asiakasLisaysNappula.setGraphic(asiakasLisays);

        // Ikkuna asiakkaan lisäämiseen
        asiakasLisaysNappula.setOnAction( e -> {
            Stage asiakasLisaysIkkuna = new Stage();

            VBox asiakasLisaysPaneeli = new VBox(10);
            asiakasLisaysPaneeli.setPadding(new Insets(25));

            GridPane asiakasLisaysGridPaneeli = new GridPane();
            asiakasLisaysGridPaneeli.setVgap(5);
            Text asiakasLisaysTeksti = new Text("Syötä asiakkaan tiedot.");
            asiakasLisaysGridPaneeli.add(asiakasLisaysTeksti, 0, 0);
            TextField enimi = new TextField();
            TextField snimi = new TextField();
            TextField email = new TextField();
            TextField puhnro = new TextField();
            TextField lahiosoite = new TextField();
            TextField postinro = new TextField();
            TextField postitoimipaikka = new TextField();

            Text enimiText = new Text("Etunimi");
            Text snimiText = new Text("Sukunimi");
            Text emailText = new Text("Sähköpostiosoite");
            Text puhnroText = new Text("Puhelinnumero");
            Text lahiosoiteText = new Text("Lähiosoite");
            Text postinroText = new Text("Postinumero");
            Text postitoimipaikkaText = new Text("Postitoimipaikka");

            asiakasLisaysGridPaneeli.add(enimiText, 0,1);
            asiakasLisaysGridPaneeli.add(enimi, 1,1);
            asiakasLisaysGridPaneeli.add(snimiText, 0,2);
            asiakasLisaysGridPaneeli.add(snimi, 1,2);
            asiakasLisaysGridPaneeli.add(emailText, 0,3);
            asiakasLisaysGridPaneeli.add(email, 1,3);
            asiakasLisaysGridPaneeli.add(puhnroText, 0,4);
            asiakasLisaysGridPaneeli.add(puhnro, 1,4);
            asiakasLisaysGridPaneeli.add(lahiosoiteText, 0,5);
            asiakasLisaysGridPaneeli.add(lahiosoite, 1,5);
            asiakasLisaysGridPaneeli.add(postinroText, 0,6);
            asiakasLisaysGridPaneeli.add(postinro, 1,6);
            asiakasLisaysGridPaneeli.add(postitoimipaikkaText, 0,7);
            asiakasLisaysGridPaneeli.add(postitoimipaikka, 1,7);


            Nappula tallennaAsiakas = new Nappula("Lisää asiakas");
            tallennaAsiakas.setOnAction( event -> {
                try {
                    if (etsiPostiNro(postiLista, postinro.getText()) == null) {
                        tietokanta.insertPosti(postinro.getText(), postitoimipaikka.getText());
                    }

                    asiakasLista.add(tietokanta.insertAsiakas(postinro.getText(), enimi.getText(), snimi.getText(), lahiosoite.getText(),
                            email.getText(), puhnro.getText(), postiLista));
                    haeKaikkiTiedot();
                    asiakasLisaysIkkuna.close();
                    paivitaAsiakasTaulukko(asiakasLista); // TODO tämä ei huomioi sitä jos asiakkaita on suodatettu haulla, mutta se olisi ehkä vaikea tehdäkin niin
                } catch (SQLException ex) {
                    asiakasLisaysTeksti.setText("Tarkista, että syöttämäsi arvot ovat \n " +
                            "oikeaa muotoa ja yritä uudelleen.");
                    asiakasLisaysTeksti.setFill(Color.RED);
                }
            });

            asiakasLisaysPaneeli.getChildren().addAll
                    (asiakasLisaysGridPaneeli, tallennaAsiakas);

            Scene asiakasLisaysKehys = new Scene(asiakasLisaysPaneeli, 400, 350);
            asiakasLisaysIkkuna.setScene(asiakasLisaysKehys);
            asiakasLisaysIkkuna.setTitle("Lisää asiakas");
            asiakasLisaysIkkuna.show();
        });
    }

    public void paivitaAsiakasTaulukko(ArrayList<Asiakas> asiakasTulokset) {
        asiakasTaulukko.setGridLinesVisible(false);
        asiakasTaulukko.getColumnConstraints().clear();
        asiakasTaulukko.getChildren().clear();
        asiakasTaulukko.getColumnConstraints().addAll(
                sarakeLyhyt, sarakeLevea, sarakeLevea, sarakeLevea, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt);
        asiakasTaulukko.setGridLinesVisible(true);

        asiakasTaulukko.setPadding(new Insets(20));

        asiakasTaulukko.add(asiakasLisaysNappula, 1,0);

        Text asiakastunnusOtsikko = new Text("Tunnus");
        Text asiakasNimiOtsikko = new Text("Nimi");
        Text asiakasEmailOtsikko = new Text("Sähköposti");
        Text asiakasPuhNroOtsikko = new Text("Puh.nro.");
        asiakastunnusOtsikko.setFont(fonttiIsompi);
        asiakasNimiOtsikko.setFont(fonttiIsompi);
        asiakasEmailOtsikko.setFont(fonttiIsompi);
        asiakasPuhNroOtsikko.setFont(fonttiIsompi);

        asiakasTaulukko.add(asiakastunnusOtsikko, 0, 1);
        asiakasTaulukko.add(asiakasNimiOtsikko, 1, 1);
        asiakasTaulukko.add(asiakasEmailOtsikko, 2, 1);
        asiakasTaulukko.add(asiakasPuhNroOtsikko, 3, 1);


        int rivi = 2;
        for (Asiakas obj : asiakasTulokset) {
            Text asiakasID = new Text(String.valueOf(obj.getID()));
            Text asiakasNimi = new Text(obj.getNimi(false));
            Text asiakasEmail = new Text(String.valueOf(obj.getEmail()));
            Text asiakasPuhNro = new Text(String.valueOf(obj.getPuhelinNro()));
            asiakasID.setFont(fonttiIsompi);
            asiakasNimi.setFont(fonttiIsompi);
            asiakasEmail.setFont(fonttiIsompi);
            asiakasPuhNro.setFont(fonttiIsompi);

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

            // Ikkuna asiakkaan poistamiseen
            poistoNappula.setOnMouseClicked(e -> {
                PoistoIkkuna poistoIkkuna = new PoistoIkkuna("asiakas", "asiakkaan",
                        "Kaikki asiakkaan varaukset ja laskut poistuvat samalla järjestelmästä.");

                poistoIkkuna.getPoistoNappula().setOnAction( event -> {
                    try {
                        tietokanta.poistaAsiakas(obj.getID());
                        haeKaikkiTiedot();
                    } catch (SQLException ex) {
                        ilmoitusPaneeli.lisaaIlmoitus(IlmoitusTyyppi.VAROITUS, "Virhe asiakkaan poistamisessa.");
                        throw new RuntimeException(ex); // TEMP
                    }
                    poistoIkkuna.getIkkuna().close();
                    paivitaAsiakasTaulukko(asiakasLista);
                });
            });

            Nappula muokkausNappula = new Nappula(100, 30);
            ImageView muokkaus = new ImageView(imageKuvasta("muokkaus.png"));
            muokkaus.setFitWidth(23);
            muokkaus.setFitHeight(22);
            muokkausNappula.setGraphic(muokkaus);
            asiakasTaulukko.add(muokkausNappula, 5, rivi);

            // Ikkuna asiakkaan muokkaamiseen
            muokkausNappula.setOnMouseClicked(e -> {
                Stage asiakasMuokkausIkkuna = new Stage();

                VBox asiakasMuokkausPaneeli = new VBox(10);
                asiakasMuokkausPaneeli.setPadding(new Insets(25));

                GridPane asiakasMuokkausGridPaneeli = new GridPane();
                asiakasMuokkausGridPaneeli.setVgap(5);
                Text asiakasMuokkausTeksti = new Text("Muokkaa asiakkaan tietoja.");
                asiakasMuokkausGridPaneeli.add(asiakasMuokkausTeksti, 0, 0);
                TextField enimi = new TextField();
                TextField snimi = new TextField();
                TextField email = new TextField();
                TextField puhnro = new TextField();
                TextField lahiosoite = new TextField();
                TextField postinro = new TextField();
                TextField postitoimipaikka = new TextField();

                enimi.setText(obj.getEtunimi());
                snimi.setText(obj.getSukunimi());
                email.setText(obj.getEmail());
                puhnro.setText(obj.getPuhelinNro());
                lahiosoite.setText(obj.getLahiosoite());
                postinro.setText(obj.getPostiNro().getPostiNro());
                postitoimipaikka.setText(obj.getPostiNro().getToimipaikka());

                Text enimiText = new Text("Etunimi");
                Text snimiText = new Text("Sukunimi");
                Text emailText = new Text("Sähköpostiosoite");
                Text puhnroText = new Text("Puhelinnumero");
                Text lahiosoiteText = new Text("Lähiosoite");
                Text postinroText = new Text("Postinumero");
                Text postitoimipaikkaText = new Text("Postitoimipaikka");

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
                asiakasMuokkausGridPaneeli.add(postitoimipaikkaText, 0,7);
                asiakasMuokkausGridPaneeli.add(postitoimipaikka, 1,7);


                Nappula tallennaAsiakasMuutokset = new Nappula("Tallenna muutokset");
                tallennaAsiakasMuutokset.setOnAction( event -> {
                    try {
                        if (etsiPostiNro(postiLista, postinro.getText()) == null) {
                            tietokanta.insertPosti(postinro.getText(), postitoimipaikka.getText());
                        }

                        tietokanta.muokkaaAsiakas
                                (obj.getID(), postinro.getText(), snimi.getText(), enimi.getText(),
                                        email.getText(), lahiosoite.getText(), puhnro.getText()); // TODO tämän voi korvata Asiakas-oliolla, sitä vain pitää muokata ensin
                        haeKaikkiTiedot();
                        asiakasMuokkausIkkuna.close();
                        paivitaAsiakasTaulukko(asiakasLista);
                    } catch (SQLException ex) {
                        asiakasMuokkausTeksti.setText("Varmista, että tiedot ovat oikeaa\n " +
                                "tietotyyppiä ja yritä uudelleen.");
                        asiakasMuokkausTeksti.setFill(Color.RED);
                    }
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

            // Ikkuna asiakkaan tarkasteluun
            tarkasteleNappula.setOnMouseClicked(e -> {
                Stage tarkasteleAsiakasIkkuna = new Stage();
                tarkasteleAsiakasIkkuna.show();
                GridPane tarkasteleAsiakasPaneeli = new GridPane();
                tarkasteleAsiakasPaneeli.setPadding(new Insets(25));
                tarkasteleAsiakasPaneeli.setVgap(15);
                tarkasteleAsiakasPaneeli.setHgap(15);
                Scene tarkasteleAsiakasKehys = new Scene(tarkasteleAsiakasPaneeli, 400, 300);
                tarkasteleAsiakasIkkuna.setScene(tarkasteleAsiakasKehys);

                Text asiakkaanTiedot = new Text("Asiakkaan tiedot");
                asiakkaanTiedot.setFont(fonttiPaksu);
                tarkasteleAsiakasPaneeli.add(asiakkaanTiedot,0,0);
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
            paivitaLaskuTaulukko();
        });

        GridPane laskuHaku = new GridPane();
        laskuHaku.setPadding(new Insets(HAKU_PADDING));
        laskuHaku.setHgap(HAKU_HGAP);
        laskuHaku.setVgap(HAKU_VGAP);
        laskupaneeli.setTop(laskuHaku);

        TextField laskuHakuKentta = new TextField();
        Label laskuHakuKenttaLabel = new Label("Hae laskuja: ", laskuHakuKentta);
        laskuHakuKenttaLabel.setFont(fonttiIsompi);
        laskuHakuKenttaLabel.setContentDisplay(ContentDisplay.RIGHT);
        laskuHaku.add(laskuHakuKenttaLabel, 1, 1);
        Nappula laskuHakuNappula = new Nappula("Suorita haku", 190, 30);
        laskuHaku.add(laskuHakuNappula, 1, 2);

        Text laskuLajitteluTeksti = new Text("Lajittelu:");
        laskuLajitteluTeksti.setFont(fonttiPaksu);
        laskuHaku.add(laskuLajitteluTeksti, 2, 0);
        ComboBox<String> laskuLajittelu = new ComboBox<>(FXCollections.observableList(Arrays.asList(
                "Tunnuksen mukaan",
                "Uusin > Vanhin",
                "Vanhin > Uusin",
                "Varaustunnuksen mukaan"
        )));
        laskuLajittelu.setValue("Tunnuksen mukaan"); // Oletuksena valittu vaihtoehto
        laskuHaku.add(laskuLajittelu, 2, 1);

        laskuHakuNappula.setOnAction(e -> {
            // Suodatus
            // TODO

            // Lajittelu
            switch (laskuLajittelu.getValue()) {
                case "Tunnuksen mukaan" ->
                        laskuLista.sort(Comparator.comparing(Lasku::getID));
                case "Uusin > Vanhin" ->
                        laskuLista.sort(Comparator.comparing(Lasku -> Lasku.getVaraus().getVahvistusPvm()));
                case "Vanhin > Uusin" ->
                        laskuLista.sort(Comparator.comparing(Lasku -> Lasku.getVaraus().getVahvistusPvm())); // TODO tämä ei jostain syystä toimi jos siihen laittaa reversed perään
                case "Varaustunnuksen mukaan" ->
                        laskuLista.sort(Comparator.comparing(Lasku -> Lasku.getVaraus().getID()));
            }

            paivitaLaskuTaulukko();
        });

        ScrollPane laskuScrollaus = new ScrollPane();
        laskupaneeli.setCenter(laskuScrollaus);
        laskuScrollaus.setContent(laskuTaulukko);
        
        ImageView laskunLisays = new ImageView(imageKuvasta("lisays.png"));
        laskunLisays.setFitWidth(23);
        laskunLisays.setFitHeight(22);
        laskunLisaysNappula.setGraphic(laskunLisays);

        // Ikkuna laskun lisäämiseen
        laskunLisaysNappula.setOnAction( e -> {
            Stage laskuLisaysIkkuna = new Stage();

            VBox laskuLisaysPaneeli = new VBox(10);
            laskuLisaysPaneeli.setPadding(new Insets(25));

            Text laskuLisaysTeksti = new Text("Syötä sen varauksen ID, josta haluat muodostaa laskun.");
            TextField varausID = new TextField();
            Nappula haeLaskulleTiedotNappula = new Nappula("Hae varauksen tiedot laskulle", 250,40);

            GridPane laskunLisaysGridPaneeli = new GridPane();
            laskunLisaysGridPaneeli.setPadding(new Insets(25));
            laskunLisaysGridPaneeli.setHgap(15);
            laskunLisaysGridPaneeli.setVgap(15);

            Text asiakkaanNimiText = new Text("Asiakkaan nimi");
            Text mokkiText = new Text("Mökki");
            Text varausAlkuPvmText = new Text("Varauksen alkamispvm.");
            Text varausLoppuPvmText = new Text("Varauksen loppumispvm.");
            Text varattujaPaiviaYhteensaText = new Text("Varattuja päiviä yhteensä");
            Text mokkiHintaText = new Text("Mökin hinta/vrk (€)");
            Text palvelutHintaText = new Text("Lisäpalveluiden yhteishinta (€)");
            Text alvText = new Text("Alv(%)");

            TextField asiakkaanNimi = new TextField();
            TextField mokki = new TextField();
            TextField varausAlkuPvm = new TextField();
            TextField varausLoppuPvm = new TextField();
            TextField varattujaPaiviaYhteensa = new TextField();
            TextField mokkiHinta = new TextField();
            TextField palvelutHinta = new TextField("0");
            TextField alv = new TextField("14");

            laskunLisaysGridPaneeli.add( asiakkaanNimiText, 0, 0);
            laskunLisaysGridPaneeli.add( asiakkaanNimi, 1, 0);
            laskunLisaysGridPaneeli.add( mokkiText, 0, 1);
            laskunLisaysGridPaneeli.add( mokki, 1, 1);
            laskunLisaysGridPaneeli.add( varausAlkuPvmText, 0, 2);
            laskunLisaysGridPaneeli.add( varausAlkuPvm, 1, 2);
            laskunLisaysGridPaneeli.add( varausLoppuPvmText, 0, 3);
            laskunLisaysGridPaneeli.add( varausLoppuPvm, 1, 3);
            laskunLisaysGridPaneeli.add( varattujaPaiviaYhteensaText, 0, 4);
            laskunLisaysGridPaneeli.add( varattujaPaiviaYhteensa, 1, 4);
            laskunLisaysGridPaneeli.add( mokkiHintaText, 0, 5);
            laskunLisaysGridPaneeli.add( mokkiHinta, 1, 5);
            laskunLisaysGridPaneeli.add( palvelutHintaText, 0, 6);
            laskunLisaysGridPaneeli.add( palvelutHinta, 1, 6);
            laskunLisaysGridPaneeli.add( alvText, 0, 7);
            laskunLisaysGridPaneeli.add( alv, 1, 7);
            

            haeLaskulleTiedotNappula.setOnAction( event -> {
                Varaus varaus = etsiVarausID(varausLista, Integer.parseInt(varausID.getText()));

                LocalDateTime pvm1 = varaus.getVarausAlkuPvm();
                LocalDateTime pvm2 = varaus.getVarausLoppuPvm();
                int daysBetween = (int) Duration.between(pvm1, pvm2).toDays();

                asiakkaanNimi.setText(varaus.getAsiakas().getNimi(false));
                mokki.setText(varaus.getMokki().getNimi());
                varausAlkuPvm.setText(String.valueOf(varaus.getVarausAlkuPvm()));
                varausLoppuPvm.setText(String.valueOf(varaus.getVarausLoppuPvm()));
                varattujaPaiviaYhteensa.setText(String.valueOf(daysBetween));
                mokkiHinta.setText(String.valueOf(varaus.getMokki().getHinta()));
                //palvelutHinta.setText();   TODO tähän pitäisi hakea varaukseen liittyvät palvelut ja niiden yhteishinta
            });

            Nappula laskunLisaysNappula = new Nappula("Lisää lasku");
            laskunLisaysNappula.setOnAction( event -> {

                double summa =
                        (Double.parseDouble(varattujaPaiviaYhteensa.getText()) *
                        Double.parseDouble(mokkiHinta.getText()) +
                        Double.parseDouble(palvelutHinta.getText())) *
                                ((Double.parseDouble(alv.getText()) / 100)+1);
                try {
                    tietokanta.insertLasku(
                            Integer.parseInt(varausID.getText()),
                            BigDecimal.valueOf(summa),
                            Integer.parseInt(alv.getText()),
                            LaskuStatus.EI_LAHETETTY.id
                    );
                    haeKaikkiTiedot();
                    laskuLisaysIkkuna.close();
                    paivitaLaskuTaulukko();
                } catch (SQLException ex) {
                    laskuLisaysTeksti.setFill(Color.RED);
                    laskuLisaysTeksti.setText("Laskun lisääminen ei onnistunut. \n" +
                            "Tarkista syötteet ja yritä uudelleen.");
                }
            });

            laskuLisaysPaneeli.getChildren().addAll(
                    laskuLisaysTeksti,
                    varausID,
                    haeLaskulleTiedotNappula,
                    laskunLisaysGridPaneeli,
                    laskunLisaysNappula);
            Scene laskuLisaysKehys = new Scene(laskuLisaysPaneeli, 400, 570);
            laskuLisaysIkkuna.setScene(laskuLisaysKehys);
            laskuLisaysIkkuna.setTitle("Luo lasku");
            laskuLisaysIkkuna.show();

        });
    }
    
    public void paivitaLaskuTaulukko() {
        laskuTaulukko.setGridLinesVisible(false);
        laskuTaulukko.getColumnConstraints().clear();
        laskuTaulukko.getChildren().clear();
        laskuTaulukko.getColumnConstraints().addAll(
                sarakeLyhyt, sarakeLevea, sarakeLyhyt, sarakeSemi, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt, sarakeLyhyt);
        laskuTaulukko.setGridLinesVisible(true);

        laskuTaulukko.setPadding(new Insets(20));
        
        laskuTaulukko.add(laskunLisaysNappula, 1,0);

        Text laskuTunnusOtsikko = new Text("Tunnus");
        Text laskuVarausOtsikko = new Text("Varaus");
        Text laskuSummaOtsikko = new Text("Summa");
        Text laskuStatusOtsikko = new Text("Status");
        laskuTunnusOtsikko.setFont(fonttiIsompi);
        laskuVarausOtsikko.setFont(fonttiIsompi);
        laskuSummaOtsikko.setFont(fonttiIsompi);
        laskuStatusOtsikko.setFont(fonttiIsompi);
        
        laskuTaulukko.add(laskuTunnusOtsikko, 0, 1);
        laskuTaulukko.add(laskuVarausOtsikko, 1, 1);
        laskuTaulukko.add(laskuSummaOtsikko, 2, 1);
        laskuTaulukko.add(laskuStatusOtsikko, 3, 1);


        int rivi = 2;
        for (Lasku obj : laskuLista) {
            Text laskuID = new Text(String.valueOf(obj.getID()));
            Text laskuVaraus = new Text(String.valueOf(obj.getVaraus().getID()));
            Text laskuSumma = new Text(obj.getSumma() + " €");
            Text laskuStatus = new Text(String.valueOf(obj.getStatus()));
            laskuID.setFont(fonttiIsompi);
            laskuVaraus.setFont(fonttiIsompi);
            laskuSumma.setFont(fonttiIsompi);
            laskuStatus.setFont(fonttiIsompi);

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

            // Ikkuna laskun poistamiseen
            poistoNappula.setOnMouseClicked(e -> {
                PoistoIkkuna poistoIkkuna = new PoistoIkkuna("lasku", "laskun");

                poistoIkkuna.getPoistoNappula().setOnAction( event -> {
                    try {
                        tietokanta.poistaLasku(obj.getID());
                        haeKaikkiTiedot();
                        poistoIkkuna.getIkkuna().close();
                        paivitaLaskuTaulukko();
                    } catch (SQLException ex) {
                        ilmoitusPaneeli.lisaaIlmoitus(IlmoitusTyyppi.VAROITUS, "Virhe laskun poistamisessa.");
                        throw new RuntimeException(ex); // TEMP
                    }
                });
            });

            Nappula muokkausNappula = new Nappula(100, 30);
            ImageView muokkaus = new ImageView(imageKuvasta("muokkaus.png"));
            muokkaus.setFitWidth(23);
            muokkaus.setFitHeight(22);
            muokkausNappula.setGraphic(muokkaus);
            laskuTaulukko.add(muokkausNappula, 5, rivi);

            // Ikkuna laskun muokkaamiseen
            muokkausNappula.setOnMouseClicked(e -> {
                Stage laskuMuokkausIkkuna = new Stage();

                VBox laskuMuokkausPaneeli = new VBox(10);
                laskuMuokkausPaneeli.setPadding(new Insets(25));

                Text laskuMuokkausTeksti = new Text("Muokkaa laskun tietoja:");

                GridPane laskunMuokkausGridPaneeli = new GridPane();
                laskunMuokkausGridPaneeli.setPadding(new Insets(25));
                laskunMuokkausGridPaneeli.setHgap(15);
                laskunMuokkausGridPaneeli.setVgap(15);

                Text asiakkaanNimiText = new Text("Asiakkaan nimi");
                Text mokkiText = new Text("Mökki");
                Text varausAlkuPvmText = new Text("Varauksen alkamispvm.");
                Text varausLoppuPvmText = new Text("Varauksen loppumispvm.");
                Text varattujaPaiviaYhteensaText = new Text("Varattuja päiviä yhteensä");
                Text mokkiHintaText = new Text("Mökin hinta/vrk (€)");
                Text palvelutHintaText = new Text("Lisäpalveluiden yhteishinta (€)");
                Text alvText = new Text("Alv(%)");
                Text statusText = new Text("Laskun tila \n" +
                        "'0': Ei lähetetty \n" +
                        "'1': Lähetetty \n" +
                        "'2': Maksettu ");

                TextField asiakkaanNimi = new TextField();
                TextField mokki = new TextField();
                TextField varausAlkuPvm = new TextField();
                TextField varausLoppuPvm = new TextField();
                TextField varattujaPaiviaYhteensa = new TextField();
                TextField mokkiHinta = new TextField(); // TODO ei mökin hintaa tai palveluiden hintaa pitäisi pystyä muokkaamaan tässä
                TextField palvelutHinta = new TextField("0");
                TextField alv = new TextField("14");
                TextField status = new TextField();

                laskunMuokkausGridPaneeli.add( asiakkaanNimiText, 0, 0);
                laskunMuokkausGridPaneeli.add( asiakkaanNimi, 1, 0);
                laskunMuokkausGridPaneeli.add( mokkiText, 0, 1);
                laskunMuokkausGridPaneeli.add( mokki, 1, 1);
                laskunMuokkausGridPaneeli.add( varausAlkuPvmText, 0, 2);
                laskunMuokkausGridPaneeli.add( varausAlkuPvm, 1, 2);
                laskunMuokkausGridPaneeli.add( varausLoppuPvmText, 0, 3);
                laskunMuokkausGridPaneeli.add( varausLoppuPvm, 1, 3);
                laskunMuokkausGridPaneeli.add( varattujaPaiviaYhteensaText, 0, 4);
                laskunMuokkausGridPaneeli.add( varattujaPaiviaYhteensa, 1, 4);
                laskunMuokkausGridPaneeli.add( mokkiHintaText, 0, 5);
                laskunMuokkausGridPaneeli.add( mokkiHinta, 1, 5);
                laskunMuokkausGridPaneeli.add( palvelutHintaText, 0, 6);
                laskunMuokkausGridPaneeli.add( palvelutHinta, 1, 6);
                laskunMuokkausGridPaneeli.add( alvText, 0, 7);
                laskunMuokkausGridPaneeli.add( alv, 1, 7);
                laskunMuokkausGridPaneeli.add( statusText, 0, 8);
                laskunMuokkausGridPaneeli.add( status, 1, 8);


                Varaus varaus = etsiVarausID(varausLista, obj.getVaraus().getID());

                LocalDateTime pvm1 = varaus.getVarausAlkuPvm();
                LocalDateTime pvm2 = varaus.getVarausLoppuPvm();
                int daysBetween = (int) Duration.between(pvm1, pvm2).toDays();

                asiakkaanNimi.setText(varaus.getAsiakas().getNimi(false));
                mokki.setText(varaus.getMokki().getNimi());
                varausAlkuPvm.setText(String.valueOf(varaus.getVarausAlkuPvm()));
                varausLoppuPvm.setText(String.valueOf(varaus.getVarausLoppuPvm()));
                varattujaPaiviaYhteensa.setText(String.valueOf(daysBetween));
                mokkiHinta.setText(String.valueOf(varaus.getMokki().getHinta()));
                //palvelutHinta.setText();   TODO tähän pitäisi hakea varaukseen liittyvät palvelut ja niiden yhteishinta


                Nappula laskunMuokkausNappula = new Nappula("Tallenna muutokset");
                laskunMuokkausNappula.setOnAction( event -> {

                    double summa =
                            (Double.parseDouble(varattujaPaiviaYhteensa.getText()) *
                                    Double.parseDouble(mokkiHinta.getText()) +
                                    Double.parseDouble(palvelutHinta.getText())) *
                                    ((Double.parseDouble(alv.getText()) / 100)+1);
                    try {
                        tietokanta.muokkaaLasku(
                                obj.getID(),
                                obj.getVaraus().getID(),
                                BigDecimal.valueOf(summa),
                                Integer.parseInt(alv.getText()),
                                Integer.parseInt(status.getText()) // TODO statuksen muuttaminen
                        );
                        haeKaikkiTiedot();
                        laskuMuokkausIkkuna.close();
                        paivitaLaskuTaulukko();
                        
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex); // TEMP
                        //laskuMuokkausTeksti.setFill(Color.RED);
                        //laskuMuokkausTeksti.setText("Muutosten tallentaminen ei onnistunut. \n" +
                        //        "Tarkista syötteet ja yritä uudelleen.");
                    }
                });

                laskuMuokkausPaneeli.getChildren().addAll(
                        laskuMuokkausTeksti,
                        laskunMuokkausGridPaneeli,
                        laskunMuokkausNappula);
                Scene laskuMuokkausKehys = new Scene(laskuMuokkausPaneeli, 400, 600);
                laskuMuokkausIkkuna.setScene(laskuMuokkausKehys);
                laskuMuokkausIkkuna.setTitle("Tallenna muutokset");
                laskuMuokkausIkkuna.show();
            });

            Nappula tarkasteleNappula = new Nappula(170, 30);
            ImageView tarkastelu = new ImageView(imageKuvasta("tarkastelu.png"));
            tarkastelu.setFitWidth(23);
            tarkastelu.setFitHeight(22);
            tarkasteleNappula.setGraphic(tarkastelu);
            laskuTaulukko.add(tarkasteleNappula, 6, rivi);

            // Ikkuna laskun tarkasteluun
            tarkasteleNappula.setOnMouseClicked(e -> {

                LocalDateTime pvm1 = obj.getVaraus().getVarausAlkuPvm();
                LocalDateTime pvm2 = obj.getVaraus().getVarausLoppuPvm();
                int daysBetween = (int) Duration.between(pvm1, pvm2).toDays();


                Stage tarkasteleLaskuIkkuna = new Stage();
                tarkasteleLaskuIkkuna.show();
                GridPane tarkasteleLaskuPaneeli = new GridPane();
                tarkasteleLaskuPaneeli.setPadding(new Insets(25));
                tarkasteleLaskuPaneeli.setVgap(15);
                tarkasteleLaskuPaneeli.setHgap(15);
                ScrollPane tarkasteleLaskuScroll = new ScrollPane(tarkasteleLaskuPaneeli);
                Scene tarkasteleLaskuKehys = new Scene(tarkasteleLaskuScroll, 400, 450);
                tarkasteleLaskuIkkuna.setScene(tarkasteleLaskuKehys);

                Text asiakkaanTiedot = new Text("Asiakkaan tiedot");
                asiakkaanTiedot.setFont(fonttiPaksu);
                tarkasteleLaskuPaneeli.add(asiakkaanTiedot,0,0);
                tarkasteleLaskuPaneeli.add(new Text("LaskuID: "),0,1);
                tarkasteleLaskuPaneeli.add(new Text("VarausID: "),0,2);
                tarkasteleLaskuPaneeli.add(new Text("AsiakasID: "),0,3);
                tarkasteleLaskuPaneeli.add(new Text("Asiakkaan nimi: "),0,4);
                tarkasteleLaskuPaneeli.add(new Text("Mökin hinta/vrk (€): "),0,5);
                tarkasteleLaskuPaneeli.add(new Text("Varauksen päivät: "),0,6);
                tarkasteleLaskuPaneeli.add(new Text("Lisäpalvelut: "),0,7);
                tarkasteleLaskuPaneeli.add(new Text("Lisäpalveluiden yhteishinta: "),0,9);
                tarkasteleLaskuPaneeli.add(new Text("Alv(%): "),0,10);
                tarkasteleLaskuPaneeli.add(new Text("Laskun summa (sis. alv): "),0,11);

                tarkasteleLaskuPaneeli.add(new Text(String.valueOf(obj.getID())),1,1);
                tarkasteleLaskuPaneeli.add(new Text(String.valueOf(obj.getVaraus().getID())),1,2);
                tarkasteleLaskuPaneeli.add(new Text(String.valueOf(obj.getVaraus().getAsiakas().getID())),1,3);
                tarkasteleLaskuPaneeli.add(new Text(obj.getVaraus().getAsiakas().getNimi(false)),1,4);
                tarkasteleLaskuPaneeli.add(new Text(String.valueOf(obj.getVaraus().getMokki().getHinta())),1,5);
                tarkasteleLaskuPaneeli.add(new Text(String.valueOf(daysBetween)),1,6);
                tarkasteleLaskuPaneeli.add(new Text(String.valueOf(obj.getAlv())),1,10);

                // Varaukseen liittyvät palvelut
                GridPane laskunVarauksenPalvelut = new GridPane();
                tarkasteleLaskuPaneeli.add(laskunVarauksenPalvelut, 0, 8);
                GridPane.setColumnSpan(laskunVarauksenPalvelut, 2);
                laskunVarauksenPalvelut.setHgap(10);
                Text palveluNimi = new Text("Palvelu");
                Text palveluMaara = new Text("Lukumäärä");
                palveluNimi.setFont(fonttiKursiivi);
                palveluNimi.setFont(fonttiKursiivi);
                laskunVarauksenPalvelut.add(palveluNimi, 0, 0);
                laskunVarauksenPalvelut.add(palveluMaara, 1, 0);
                int riviVp = 1;
                for (Map.Entry<Palvelu, Integer> vp : obj.getVaraus().getPalvelut().entrySet()) {
                    laskunVarauksenPalvelut.add(new Text(vp.getKey().getKuvaus()), 0, riviVp);
                    laskunVarauksenPalvelut.add(new Text(String.valueOf(vp.getValue())), 1, riviVp);
                    riviVp++;
                }
                // TODO lisätäänkö euron merkit näihin
                tarkasteleLaskuPaneeli.add(new Text(String.format("%,.2f", obj.getVarausPalveluSumma())),1,9);
                tarkasteleLaskuPaneeli.add(new Text(String.valueOf(String.format("%,.2f", obj.getVarausSumma()))),1,11);

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

    /**
     * Hakee tietokannasta kaikki tiedot.
     * <ul>
     *     <li>Postinumerot</li>
     *     <li>Alueet</li>
     *     <li>Asiakkaat</li>
     *     <li>Mökit</li>
     *     <li>Palvelut</li>
     *     <li>Varaukset</li>
     *     <li>Laskut</li>
     * </ul>
     */
    public void haeKaikkiTiedot() {
        // Haetaan tiedot tietokannasta olioiksi
        try {
            // Tiedot täytyy hakea tietyssä järjestyksessä, koska osa luokista viittaa toisiinsa
            postiLista = tietokanta.haePosti();
            alueLista = tietokanta.haeAlue();
            asiakasLista = tietokanta.haeAsiakas(postiLista);
            mokkiLista = tietokanta.haeMokki(alueLista, postiLista);
            palveluLista = tietokanta.haePalvelu(alueLista);
            varausLista = tietokanta.haeVaraus(asiakasLista, mokkiLista, palveluLista);
            laskuLista = tietokanta.haeLasku(varausLista);
        } catch (SQLException | NullPointerException e) {
            ilmoitusPaneeli.lisaaIlmoitus(IlmoitusTyyppi.VAROITUS,
                    "Virhe tietojen hakemisessa. Tietokantaa ei ole ehkä käynnistetty.");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}