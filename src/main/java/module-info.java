module com.example.otiprojekti {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.otiprojekti to javafx.fxml;
    exports com.example.otiprojekti;
}