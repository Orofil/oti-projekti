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

    /**
     * Etsii asiakkaista sen, jolla on annettu ID.
     * @param lista Lista asiakkaista
     * @param id asiakasID
     * @return {@link Asiakas}
     * @author <a href="https://stackoverflow.com/a/48839294">oleg.cherednik</a>
     */
    public static Asiakas etsiAsiakasID(Collection<Asiakas> lista, int id) {
        return findByProperty(lista, asiakas -> id == asiakas.getAsiakasID());
    }

    /**
     * Etsii laskuista sen, jolla on annettu ID.
     * @param lista Lista laskuista
     * @param id laskuID
     * @return {@link Lasku}
     * @author <a href="https://stackoverflow.com/a/48839294">oleg.cherednik</a>
     */
    public static Lasku etsiLaskuID(Collection<Lasku> lista, int id) {
        return findByProperty(lista, lasku -> id == lasku.getLaskuID());
    }

    /**
     * Etsii mökeistä sen, jolla on annettu ID.
     * @param lista Lista mökeistä
     * @param id mokkiID
     * @return {@link Mokki}
     * @author <a href="https://stackoverflow.com/a/48839294">oleg.cherednik</a>
     */
    public static Mokki etsiMokkiID(Collection<Mokki> lista, int id) {
        return findByProperty(lista, mokki -> id == mokki.getMokkiID());
    }

    /**
     * Etsii palveluista sen, jolla on annettu ID.
     * @param lista Lista palveluista
     * @param id palveluID
     * @return {@link Palvelu}
     * @author <a href="https://stackoverflow.com/a/48839294">oleg.cherednik</a>
     */
    public static Palvelu etsiPalveluID(Collection<Palvelu> lista, int id) {
        return findByProperty(lista, palvelu -> id == palvelu.getPalveluID());
    }

    /**
     * Etsii varauksista sen, jolla on annettu ID.
     * @param lista Lista varauksista
     * @param id varausID
     * @return {@link Varaus}
     * @author <a href="https://stackoverflow.com/a/48839294">oleg.cherednik</a>
     */
    public static Varaus etsiVarausID(Collection<Varaus> lista, int id) {
        return findByProperty(lista, varaus -> id == varaus.getVarausID());
    }

    /**
     * @author <a href="https://stackoverflow.com/a/48839294">oleg.cherednik</a>
     */
    public static <T> T findByProperty(Collection<T> col, Predicate<T> filter) {
        return col.stream().filter(filter).findFirst().orElse(null);
    }
}
