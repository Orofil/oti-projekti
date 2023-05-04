package com.example.otiprojekti;

import javafx.scene.image.Image;

import java.util.Collection;
import java.util.function.Predicate;

public class Utils {
    /**
     * Polku projektin resurssikansioon.
     */
    public static final String IMGPOLKU = "src/main/resources/com/example/otiprojekti/";

    /**
     * Avaa resources-kansiossa sijaitsevan kuvan JavaFX:n {@link Image Image}-luokan oliona.
     * @param kuva Kuvan nimi
     * @return Image
     */
    public static Image imageKuvasta(String kuva) {
        try {
            return new Image(IMGPOLKU + kuva);
        } catch (IllegalArgumentException e) {
            return new Image(kuva);
        }
    }

    /**
     * Etsii alueista sen, jolla on annettu ID.
     * @param lista Lista alueista
     * @param id alueID
     * @return {@link Alue}
     * @author <a href="https://stackoverflow.com/a/48839294">oleg.cherednik</a>
     */
    public static Alue etsiAlueID(Collection<Alue> lista, int id) {
        return findByProperty(lista, alue -> id == alue.getAlueID());
    }

    // TODO muut etsintäfunktiot jos tarvitsee

    /**
     * @author <a href="https://stackoverflow.com/a/48839294">oleg.cherednik</a>
     */
    public static <T> T findByProperty(Collection<T> col, Predicate<T> filter) {
        return col.stream().filter(filter).findFirst().orElse(null);
    }
}
