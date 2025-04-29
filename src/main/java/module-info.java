module org.example.navalbattle {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.navalbattle to javafx.fxml;
    opens org.example.navalbattle.controllers to javafx.fxml;
    exports org.example.navalbattle;
}