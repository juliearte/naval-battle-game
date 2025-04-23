package com.example.navalbattlegame.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HomeStage extends Stage {
    public HomeStage() throws IOException {
        // Load the FXML file for the home stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/navalbattlegame/home-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        setResizable(false);
        setTitle("Batalla Naval");
        Image icon = new Image(Objects.requireNonNull(getClass().getResource("/com/example/navalbattlegame/images/favicon.png")).toExternalForm());
        getIcons().add(icon);
        setScene(scene);
        //show(); // Show the home stage
    }

    // Static method to obtain a unique instance of the home stage
    public static HomeStage getInstance() throws IOException {
        if (HomeStageHolder.INSTANCE == null) {
            HomeStageHolder.INSTANCE = new HomeStage();
        }
        return HomeStageHolder.INSTANCE;
        //return HomeStageHolder.INSTANCE = new HomeStage();
    }

    // Static method to remove the instance of the home stage
    public static void deleteInstance() {
        HomeStageHolder.INSTANCE.close();
        HomeStageHolder.INSTANCE = null;
    }

    // Static class to hold the unique instance of the home stage
    private static class HomeStageHolder {
        private static HomeStage INSTANCE;
    }
}
