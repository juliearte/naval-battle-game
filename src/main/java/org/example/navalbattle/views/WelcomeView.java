package org.example.navalbattle.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeView extends Stage {
    public WelcomeView() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/navalbattle/welcome-view.fxml")
        );
        Parent root = loader.load();
        this.setTitle("NAVAL BATTLE");
        Scene scene = new Scene(root);
        this.setScene(scene);
        this.getIcons().add(new Image(
                getClass().getResource("/org/example/navalbattle/images/icon.png").toString()
        ));
        this.show();
    }

    public static WelcomeView getInstance() throws IOException {
        if (WelcomeViewHolder.INSTANCE == null) {
            return WelcomeViewHolder.INSTANCE = new WelcomeView();
        } else {
            return WelcomeViewHolder.INSTANCE;
        }
    }

    private static class WelcomeViewHolder {
        private static WelcomeView INSTANCE;
    }
}