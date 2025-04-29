package org.example.navalbattle.controllers;

import javafx.scene.Node;

public class Draggable {

    private double mouseAnchorX;
    private double mouseAnchorY;

    public void makeDraggable(Node node) {
        node.setOnMousePressed(event -> {
            mouseAnchorX = event.getSceneX();
            mouseAnchorY = event.getSceneY();
        });

        node.setOnMouseDragged(event -> {
            node.setLayoutX(mouseAnchorX - event.getSceneX());
            node.setLayoutY(mouseAnchorY - event.getSceneY());
        });
    }
}
