package com.example.navalbattlegame;

import com.example.navalbattlegame.view.HomeStage;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Initialize the home stage
        HomeStage homeStage = HomeStage.getInstance();
        homeStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
