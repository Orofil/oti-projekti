module com.example.otiprojekti {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
//    requires mysql.connector.j; // TODO miten tämä ulkoisen kirjaston vaatimus toteutetaan, SQL yhdistäminen ei toimi ilman tätä


    opens com.example.otiprojekti to javafx.fxml;
    exports com.example.otiprojekti;

    // TODO poistetaan kun testit poistetaan
    opens testit to javafx.fxml;
    exports testit;
}