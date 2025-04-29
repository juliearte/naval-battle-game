package org.example.navalbattle;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.navalbattle.views.GameView;
import org.example.navalbattle.views.InstructionsView;
import org.example.navalbattle.views.WelcomeView;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        WelcomeView.getInstance();
    }
}