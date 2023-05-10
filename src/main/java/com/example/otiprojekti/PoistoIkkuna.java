package com.example.otiprojekti;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class PoistoIkkuna {
    private final Stage ikkuna;
    private final Nappula poistoNappula;

    /**
     * Luo uuden poistoikkunan. Ikkunalle täytyy erikseen asettaa toiminnallisuus poistoNappulalle.
     * @param teksti Mikä poistetaan, esim. "varaus"
     * @param tekstiGen Mikä poistetaan, mutta genetiivissä, esim. "varauksen"
     * @param tekstiEkstra Ylimääräinen teksti joka tulee varmistustekstin alle
     */
    public PoistoIkkuna(String teksti, String tekstiGen, String tekstiEkstra) {
        ikkuna = new Stage();
        ikkuna.show();

        BorderPane paneeli = new BorderPane();
        paneeli.setPadding(new Insets(50));

        Scene kehys = new Scene(paneeli, 600, 200);
        ikkuna.setScene(kehys);
        ikkuna.setTitle("Poista " + teksti);

        HBox nappulaPaneeli = new HBox();
        nappulaPaneeli.setSpacing(30);
        nappulaPaneeli.setPadding(new Insets(30));
        poistoNappula = new Nappula("Poista " + teksti);
        Nappula peruutaNappula = new Nappula("Peruuta");
        nappulaPaneeli.getChildren().addAll(poistoNappula, peruutaNappula);

        StackPane tekstiPaneeli = new StackPane();
        // Lisätään poistotekstin loppuun ekstrateksti jos se on annettu
        Text poistoTeksti = new Text("Haluatko varmasti poistaa " + tekstiGen + "?" +
                (tekstiEkstra == null ? "" : "\n" + tekstiEkstra));
        poistoTeksti.setTextAlignment(TextAlignment.CENTER);
        poistoTeksti.setFont(Font.font(16));
        tekstiPaneeli.getChildren().add(poistoTeksti);
        paneeli.setTop(tekstiPaneeli);
        paneeli.setCenter(nappulaPaneeli);

        peruutaNappula.setOnAction(e -> ikkuna.close());
    }

    /**
     * Luo uuden poistoikkunan. Ikkunalle täytyy erikseen asettaa toiminnallisuus poistoNappulalle.
     * @param teksti Mikä poistetaan, esim. "varaus"
     * @param tekstiGen Mikä poistetaan, mutta genetiivissä, esim. "varauksen"
     */
    public PoistoIkkuna(String teksti, String tekstiGen) {
        this(teksti, tekstiGen, null);
    }

    public Nappula getPoistoNappula() {
        return poistoNappula;
    }

    public Stage getIkkuna() {
        return ikkuna;
    }
}
