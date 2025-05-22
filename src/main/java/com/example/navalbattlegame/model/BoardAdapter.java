package com.example.navalbattlegame.model;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/** Pinta un Board en un AnchorPane.  revealShips=false → oculta barcos intactos. */
public class BoardAdapter {

    private final AnchorPane pane;
    private final Board board;
    private final ImagePattern bombPattern, xPattern, sunkPattern;
    private boolean revealShips;

    public BoardAdapter(AnchorPane pane,
                        Board board,
                        ImagePattern bombPattern,
                        ImagePattern xPattern,
                        ImagePattern sunkPattern,
                        boolean revealShips) {
        this.pane = pane;
        this.board = board;
        this.bombPattern = bombPattern;
        this.xPattern = xPattern;
        this.sunkPattern = sunkPattern;
        this.revealShips = revealShips;
    }

    public void setRevealShips(boolean value) { this.revealShips = value; }

    public void paint() {
        for (int i = 0; i < pane.getChildren().size(); i++)
            paintCell(i / Board.SIZE, i % Board.SIZE);
    }

    public void paintCell(int r, int c) {
        Rectangle cell = (Rectangle) pane.getChildren().get(r * Board.SIZE + c);
        int v = board.getCell(r, c);
        cell.setStroke(Color.BLACK);

        switch (v) {
            case 1 -> cell.setFill(revealShips ? Color.YELLOWGREEN : Color.web("#9EECFF"));
            case 2 -> cell.setFill(revealShips ? Color.CORAL       : Color.web("#9EECFF"));
            case 3 -> cell.setFill(revealShips ? Color.GOLD        : Color.web("#9EECFF"));
            case 4 -> cell.setFill(revealShips ? Color.SALMON      : Color.web("#9EECFF"));
            case 5 -> cell.setFill(xPattern);          // agua
            case -1, -2, -3, -4 -> cell.setFill(bombPattern); // tocado
            case -5 -> cell.setFill(sunkPattern);      // hundido
            default -> cell.setFill(Color.web("#9EECFF"));
        }
    }
}
