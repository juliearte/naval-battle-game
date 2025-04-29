package org.example.navalbattle.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.navalbattle.model.Player;
import org.example.navalbattle.views.GameView;


import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;

public class WelcomeController {

    @FXML
    private TextField nicknameTextField;


    @FXML
    public void onActionLetsPlayButton (ActionEvent event)throws IOException {
        String nickname = nicknameTextField.getText();
        Player player = new Player();
        player.setNickname(nickname);
        System.out.println(player.getNickname());
        GameView gameView = GameView.getInstance();
        gameView.getGameController().setPlayer(player);
    }
}
