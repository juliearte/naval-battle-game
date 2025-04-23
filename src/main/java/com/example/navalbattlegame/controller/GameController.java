package com.example.navalbattlegame.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class GameController {
    @FXML
    private GridPane gridPaneGame;

    @FXML
    private GridPane gridPaneGame1;

    @FXML
    public void initialize() {
        // Initialize the game grid
        for (int row = 0; row < gridPaneGame.getRowCount(); row++) {
            for (int col = 0; col < gridPaneGame.getColumnCount(); col++) {
                // Add your logic to initialize each cell in the grid
                // For example, you can set a default style or add event handlers
            }
        }

        // Initialize the game grid 1
        for (int row = 0; row < gridPaneGame1.getRowCount(); row++) {
            for (int col = 0; col < gridPaneGame1.getColumnCount(); col++) {
                // Add your logic to initialize each cell in the grid
                // For example, you can set a default style or add event handlers
            }
        }
    }
}
