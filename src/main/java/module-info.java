module com.example.navalbattlegame {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.example.navalbattlegame.controller to javafx.fxml;
    exports com.example.navalbattlegame;

    opens com.example.navalbattlegame.controller to javafx.fxml;
}