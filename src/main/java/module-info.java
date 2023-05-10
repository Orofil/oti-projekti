module com.example.otiprojekti {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;
    requires itextpdf;

    opens com.example.otiprojekti to javafx.fxml;
    exports com.example.otiprojekti;

    exports com.example.otiprojekti.ilmoitukset;
    opens com.example.otiprojekti.ilmoitukset to javafx.fxml;
}