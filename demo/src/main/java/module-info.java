module com.calabashbros.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.calabashbros.demo to javafx.fxml;
    exports com.calabashbros.demo;
}