package com.example.otiprojekti.ilmoitukset;

import com.example.otiprojekti.ilmoitukset.Ilmoitus;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;

public class IlmoitusPaneeli extends VBox {
    private ArrayList<Ilmoitus> ilmoitukset = new ArrayList<>();
    // Onko hiiri jonkin ilmoituksen päällä
    private boolean hiiriPaalla;
    // Oletuskesto ilmoitukselle
    private int kesto = 8;

    public IlmoitusPaneeli() {
        super(15);
        setPadding(new Insets(15));
        setWidth(150);
    }

    public void lisaaIlmoitus(IlmoitusTyyppi tyyppi, String teksti) {
        Ilmoitus i = new Ilmoitus(tyyppi, teksti, kesto);
        getChildren().add(i);
        ilmoitukset.add(i);

        // Timeline laskee ajan, jonka kuluttua loppuun ilmoitus katoaa
        Timeline t = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            i.setKesto(i.getKesto() - 1);
            System.out.println("Kesto: " + i.getKesto()); // TEMP
            if (i.getKesto() <= 0) {
                /*
                Jos hiiri on jonkun ilmoituksen päällä, tehdään muista
                ilmoituksista vain näkymättömiä ja poistetaan myöhemmin.
                 */
                if (!hiiriPaalla) {
                    getChildren().remove(i);
                } else {
                    i.setVisible(false);
                }
            }
        }));
        t.setCycleCount(kesto);
        t.playFromStart();

        i.setOnMouseEntered(e -> {
            hiiriPaalla = true;
            t.pause();
            i.setKesto(kesto);
        });
        i.setOnMouseExited(e -> {
            hiiriPaalla = false;
            t.playFromStart();
            // Poistetaan näkymättömäksi muutetut ilmoitukset
            for (Ilmoitus ilm : ilmoitukset) {
                if (!ilm.isVisible()) {
                    getChildren().remove(ilm);
                }
            }
        });
    }

    public int getKesto() {
        return kesto;
    }

    public void setKesto(int kesto) {
        this.kesto = kesto;
    }
}
