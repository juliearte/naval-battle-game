package com.example.navalbattlegame.view.alert;
import javafx.scene.control.Alert;

public interface AlertBoxInterface {
    public void showAlert(String title, String header, String message, Alert.AlertType alertType);
}
