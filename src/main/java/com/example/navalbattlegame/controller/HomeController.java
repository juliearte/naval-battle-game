package com.example.navalbattlegame.controller;

import com.example.navalbattlegame.view.GameStage;
import com.example.navalbattlegame.view.HomeStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class HomeController {
    @FXML
    private void onPlayButtonClicked(ActionEvent event) {
        try {

            GameStage gameStage = GameStage.getInstance();
            HomeStage.deleteInstance();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
