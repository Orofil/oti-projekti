package com.example.otiprojekti;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PoistoIkkuna {
    private Stage ikkuna;
    private Nappula poistoNappula;

    /**
     * Luo uuden poistoikkunan. Ikkunalle täytyy erikseen asettaa toiminnallisuus poistoNappulalle.
     * @param teksti Mikä poistetaan, esim. "varaus"
     * @param tekstiGen Mikä poistetaan, mutta genetiivissä, esim. "varauksen"
     */
    public PoistoIkkuna(String teksti, String tekstiGen) {
        ikkuna = new Stage();
        // TODO ylaIkkuna parametriksi jos haluaa lukita alemman ikkunan käytön
//        ikkuna.initModality(Modality.WINDOW_MODAL);
//        ikkuna.initOwner(ylaIkkuna);
        ikkuna.show();

        BorderPane paneeli = new BorderPane();
        paneeli.setPadding(new Insets(50));

        Scene kehys = new Scene(paneeli, 500, 200);
        ikkuna.setScene(kehys);
        ikkuna.setTitle("Poista " + teksti);

        HBox nappulaPaneeli = new HBox();
        nappulaPaneeli.setSpacing(30);
        nappulaPaneeli.setPadding(new Insets(30));
        poistoNappula = new Nappula("Poista " + teksti);
        Nappula peruutaNappula = new Nappula("Peruuta");
        nappulaPaneeli.getChildren().addAll(poistoNappula, peruutaNappula);

        StackPane tekstiPaneeli = new StackPane();
        Text poistoTeksti = new Text("Haluatko varmasti poistaa " + tekstiGen + "?");
        poistoTeksti.setTextAlignment(TextAlignment.CENTER);
        poistoTeksti.setFont(Font.font(16));
        tekstiPaneeli.getChildren().add(poistoTeksti);
        paneeli.setTop(tekstiPaneeli);
        paneeli.setCenter(nappulaPaneeli);

        peruutaNappula.setOnAction(e -> ikkuna.close());
    }

    public Nappula getPoistoNappula() {
        return poistoNappula;
    }

    public Stage getIkkuna() {
        return ikkuna;
    }
}
