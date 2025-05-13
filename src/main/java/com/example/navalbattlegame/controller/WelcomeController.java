package com.example.navalbattlegame.controller;

import com.example.navalbattlegame.model.GameStateHandler;
import com.example.navalbattlegame.view.GameView;
import com.example.navalbattlegame.view.alert.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

/** Pantalla de bienvenida: ingresa nick y elige nueva partida o carga. */
public class WelcomeController {

    @FXML private TextField nicknameTextField;

    @FXML
    void onHandlePlayButton(ActionEvent event) throws IOException {

        String nick = nicknameTextField.getText().isBlank()
                ? "Jugador" : nicknameTextField.getText();

        if (GameStateHandler.savedGameExists()) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Partida guardada encontrada");
            alert.setHeaderText("¿Qué deseas hacer?");
            alert.setContentText("Cargar la partida anterior o iniciar una nueva:");

            ButtonType load  = new ButtonType("Cargar partida");
            ButtonType fresh = new ButtonType("Nueva partida");
            alert.getButtonTypes().setAll(load, fresh);

            Optional<ButtonType> res = alert.showAndWait();
            if (res.isPresent() && res.get() == load) {
                GameView.getInstance().getGameController().loadGame();
            } else {
                GameStateHandler.clearSavedGame();
                GameView.getInstance().getGameController().initializeBoard(nick);
            }
        } else {
            GameView.getInstance().getGameController().initializeBoard(nick);
        }
        /* cerrar ventana de bienvenida */
        ((javafx.stage.Stage) ((javafx.scene.Node) event.getSource())
                .getScene().getWindow()).close();
    }

    public void onHandleInstructionsButton(ActionEvent e) {
        new AlertBox().showAlert("INFORMATION", "INSTRUCCIONES DEL JUEGO",
                "Bienvenido a BattleField.\n\n" +
                        "- Arrastra tu flota al tablero (clic derecho para rotar).\n" +
                        "- Dispara haciendo clic en el tablero enemigo.\n" +
                        "- Gana quien hunda los 10 barcos rivales.\n" +
                        "- Usa el botón «Mostrar» para ver la flota de la IA (modo debug).",
                Alert.AlertType.INFORMATION);
    }
}
