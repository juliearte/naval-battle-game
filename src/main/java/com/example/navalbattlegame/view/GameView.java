package com.example.navalbattlegame.view;

import com.example.navalbattlegame.controller.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * GameView class representing the game window for the application.
 * It extends Stage to manage the main game window and sets up the view using an FXML file.
 * @author Celeste Berrio - Leonardo Rios - Juan Montealegre
 */
public class GameView extends Stage {

    private GameController gameController; // Game controller

    /**
     * Constructor for GameView.
     * Loads the FXML file, sets the scene, title, and window icon.
     *
     * @throws IOException If there is an error loading the FXML file
     */
    public GameView() throws IOException {
        // Load the FXML file using FXMLLoader
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/navalbattlegame/game-view.fxml")
        );
        Parent root = loader.load(); // Load the root element from the FXML file

        // Get the associated controller from the FXMLLoader
        this.gameController = loader.getController();

        // Set the window title
        this.setTitle("Batalla Naval");

        // Create the scene with the loaded root element
        Scene scene = new Scene(root);

        // Set the window icon
        this.getIcons().add(new Image(
                getClass().getResourceAsStream("/com/example/navalbattlegame/images/crucero.png")
        ));

        // Set the scene for this stage
        this.setScene(scene);

        // Display the window
        this.show();
    }

    /**
     * Returns the game controller associated with this view.
     *
     * @return GameController instance used in this view
     */
    public GameController getGameController() {
        return this.gameController;
    }

    /**
     * Returns the singleton instance of GameView.
     * If no instance exists, it creates a new one.
     *
     * @return GameView instance
     * @throws IOException If there is an error creating the instance
     */
    public static GameView getInstance() throws IOException {
        if (GameViewHolder.INSTANCE == null) {
            // Create a new instance if it does not exist
            return GameViewHolder.INSTANCE = new GameView();
        } else {
            // Return the existing instance
            return GameViewHolder.INSTANCE;
        }
    }

    /**
     * Static inner class to hold the singleton instance of GameView.
     * This ensures lazy initialization and thread safety.
     */
    private static class GameViewHolder {
        private static GameView INSTANCE; // Singleton instance of GameView
    }
}