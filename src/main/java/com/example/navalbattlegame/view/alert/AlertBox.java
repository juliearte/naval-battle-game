package com.example.navalbattlegame.view.alert;
import javafx.scene.control.Alert;


public class AlertBox implements AlertBoxInterface{

    @Override
    public void showAlert(String title, String header, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
