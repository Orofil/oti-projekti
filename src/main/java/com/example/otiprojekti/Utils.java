package com.example.otiprojekti;

import javafx.scene.image.Image;

import java.util.ArrayList;
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
        return findByProperty(lista, alue -> id == alue.getID());
    }

    /**
     * Etsii asiakkaista sen, jolla on annettu ID.
     * @param lista Lista asiakkaista
     * @param id asiakasID
     * @return {@link Asiakas}
     * @author <a href="https://stackoverflow.com/a/48839294">oleg.cherednik</a>
     */
    public static Asiakas etsiAsiakasID(Collection<Asiakas> lista, int id) {
        return findByProperty(lista, asiakas -> id == asiakas.getID());
    }

    /**
     * Etsii laskuista sen, jolla on annettu ID.
     * @param lista Lista laskuista
     * @param id laskuID
     * @return {@link Lasku}
     * @author <a href="https://stackoverflow.com/a/48839294">oleg.cherednik</a>
     */
    public static Lasku etsiLaskuID(Collection<Lasku> lista, int id) {
        return findByProperty(lista, lasku -> id == lasku.getID());
    }

    /**
     * Etsii mökeistä sen, jolla on annettu ID.
     * @param lista Lista mökeistä
     * @param id mokkiID
     * @return {@link Mokki}
     * @author <a href="https://stackoverflow.com/a/48839294">oleg.cherednik</a>
     */
    public static Mokki etsiMokkiID(Collection<Mokki> lista, int id) {
        return findByProperty(lista, mokki -> id == mokki.getID());
    }

    /**
     * Etsii palveluista sen, jolla on annettu ID.
     * @param lista Lista palveluista
     * @param id palveluID
     * @return {@link Palvelu}
     * @author <a href="https://stackoverflow.com/a/48839294">oleg.cherednik</a>
     */
    public static Palvelu etsiPalveluID(Collection<Palvelu> lista, int id) {
        return findByProperty(lista, palvelu -> id == palvelu.getID());
    }

    /**
     * Etsii posteista sen, jolla on annettu postinumero.
     * @param lista Lista posteista
     * @param postiNro postiNro
     * @return {@link Posti}
     * @author <a href="https://stackoverflow.com/a/48839294">oleg.cherednik</a>
     */
    public static Posti etsiPostiNro(Collection<Posti> lista, String postiNro) {
        return findByProperty(lista, posti -> postiNro.equals(posti.getPostiNro()));
    }

    /**
     * Etsii varauksista sen, jolla on annettu ID.
     * @param lista Lista varauksista
     * @param id varausID
     * @return {@link Varaus}
     * @author <a href="https://stackoverflow.com/a/48839294">oleg.cherednik</a>
     */
    public static Varaus etsiVarausID(Collection<Varaus> lista, int id) {
        return findByProperty(lista, varaus -> id == varaus.getID());
    }

    /**
     * @author <a href="https://stackoverflow.com/a/48839294">oleg.cherednik</a>
     */
    public static <T> T findByProperty(Collection<T> col, Predicate<T> filter) {
        return col.stream().filter(filter).findFirst().orElse(null);
    }


    public static ArrayList<Alue> haeAlueHakusanalla(ArrayList<Alue> alueLista, String hakusana) {
        ArrayList<Alue> tulokset = new ArrayList<>();
        for (Alue obj : alueLista) {
            if (obj.getNimi().contains(hakusana)) {
                tulokset.add(obj);
            }
        }
        return tulokset;
    }

    public static ArrayList<Mokki> haeMokkiHakusanalla(ArrayList<Mokki> mokkiLista, String hakusana) {
        ArrayList<Mokki> tulokset = new ArrayList<>();
        for (Mokki obj : mokkiLista) {
            if (obj.getNimi().contains(hakusana)) {
                tulokset.add(obj);
            }
            else if (obj.getAlue().getNimi().contains(hakusana)) {
                tulokset.add(obj);
            }
            else if (obj.getVarustelu().contains(hakusana)) {
                tulokset.add(obj);
            }
        }
        return tulokset;
    }

    public static ArrayList<Palvelu> haePalveluHakusanalla(ArrayList<Palvelu> palveluLista, String hakusana) {
        ArrayList<Palvelu> tulokset = new ArrayList<>();
        for (Palvelu obj : palveluLista) {
            if (obj.getNimi().contains(hakusana)) {
                tulokset.add(obj);
            }
            else if (obj.getAlue().getNimi().contains(hakusana)) {
                tulokset.add(obj);
            }
            else if (obj.getKuvaus().contains(hakusana)) {
                tulokset.add(obj);
            }
        }
        return tulokset;
    }

    public static ArrayList<Varaus> haeVarausHakusanalla(ArrayList<Varaus> varausLista, String hakusana) {
        ArrayList<Varaus> tulokset = new ArrayList<>();
        for (Varaus obj : varausLista) {
            if (obj.getAsiakas().getNimi(false).contains(hakusana)) {
                tulokset.add(obj);
            }
            else if (obj.getMokki().getNimi().contains(hakusana)) {
                tulokset.add(obj);
            }
        }
        return tulokset;
    }

    public static ArrayList<Asiakas> haeAsiakasHakusanalla(ArrayList<Asiakas> asiakasLista, String hakusana) {
        ArrayList<Asiakas> tulokset = new ArrayList<>();
        for (Asiakas obj : asiakasLista) {
            if (obj.getNimi(false).contains(hakusana)) {
                tulokset.add(obj);
            }
            else if (obj.getEmail().contains(hakusana)) {
                tulokset.add(obj);
            }
            else if (obj.getLahiosoite().contains(hakusana)) {
                tulokset.add(obj);
            }
            else if (obj.getPuhelinNro().contains(hakusana)) {
                tulokset.add(obj);
            }
        }
        return tulokset;
    }

    public static ArrayList<Lasku> haeLaskuHakusanalla(ArrayList<Lasku> laskuLista, String hakusana) {
        ArrayList<Lasku> tulokset = new ArrayList<>();
        for (Lasku obj : laskuLista) {
            if (obj.getVaraus().getAsiakas().getNimi(false).contains(hakusana)) {
                tulokset.add(obj);
            }
        }
        return tulokset;
    }
    
}
