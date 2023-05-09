module com.example.otiprojekti {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires itextpdf;
    requires java.desktop;
    //requires itext;

    opens com.example.otiprojekti to javafx.fxml;
    exports com.example.otiprojekti;

    // TODO testeihin liittyvät kaksi riviä poistetaan kun testit poistetaan, mutta saattaa ne poistua automaattisestikin
    opens testit to javafx.fxml;
    exports testit;
    exports com.example.otiprojekti.nakymat;
    opens com.example.otiprojekti.nakymat to javafx.fxml;
    exports com.example.otiprojekti.ilmoitukset;
    opens com.example.otiprojekti.ilmoitukset to javafx.fxml;
}