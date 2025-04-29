package org.example.navalbattle.model.alerts;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AlertBoxGame implements AlertBoxGameInterface {
    @Override
    public void showAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        Image icon = new Image(getClass().getResourceAsStream("/org/example/navalbattle/images/icon.png"));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(icon);
        alert.showAndWait();
    }
}
