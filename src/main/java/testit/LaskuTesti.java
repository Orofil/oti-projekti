package testit;

import com.example.otiprojekti.Lasku;

import java.math.BigDecimal;

public class LaskuTesti {

    public static void main(String[] args) {
        BigDecimal luku = new BigDecimal("4800");
        Lasku lasku = new Lasku(1, 5, luku, 20, "maksamatta");
        lasku.vieDokumentiksi();
    }
}
