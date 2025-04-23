package com.example.navalbattlegame.view;

import com.example.navalbattlegame.controller.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GameStage extends Stage {
    static GameController gameController;
    public GameStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/navalbattlegame/game-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        setResizable(false);
        setTitle("Naval Battle Game");
        gameController = loader.getController();
        double screenWidth = javafx.stage.Screen.getPrimary().getBounds().getWidth();
        double screenHeight = javafx.stage.Screen.getPrimary().getBounds().getHeight();
        Image icon = new Image(Objects.requireNonNull(getClass().getResource("/com/example/navalbattlegame/images/favicon.png")).toExternalForm());
        getIcons().add(icon);
        setWidth(screenWidth * 1);
        setHeight(screenHeight * 1);
        setScene(scene);
        show();
    }

    public static GameStage getInstance() throws IOException {
        if (GameStageHolder.INSTANCE == null) {
            GameStageHolder.INSTANCE = new GameStage();
        }
        return GameStageHolder.INSTANCE;
        //return GameStageHolder.INSTANCE = new GameStage();
    }

    public static void deleteInstance() {
        GameStageHolder.INSTANCE.close();
        GameStageHolder.INSTANCE = null;
    }

    private static class GameStageHolder {
        private static GameStage INSTANCE;
    }

    public GameController getGameController() {
        return gameController;
    }


}
