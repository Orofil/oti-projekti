package testit;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalTesti {
    public static void main(String[] args) {
        BigDecimal hinta = new BigDecimal("42.99");
        System.out.println("Hinta: " + hinta);

        BigDecimal hinta3pv = hinta.multiply(BigDecimal.valueOf(3));
        System.out.println("Kolmen päivän hinta: " + hinta3pv);

        BigDecimal hintaPuolikas = hinta.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP); // Säilyttää kaksi desimaalia, pyöristää ylös
        System.out.println("Puolikas hinta (pyöristettynä): " + hintaPuolikas);

        BigDecimal hintaPuolikasTarkka = hinta.divide(BigDecimal.valueOf(2)); // Ei pyöristetä, mutta antaa warningia jostain syystä
        System.out.println("Puolikas hinta: " + hintaPuolikasTarkka);

        double hintaDouble = hinta.doubleValue();
        System.out.println("Hinta doublena: " + hintaDouble);
    }
}
