package com.example.navalbattlegame.model;

import com.example.navalbattlegame.exceptions.InvalidPlacementException;

public class Player implements Shooter {

    private final String nick;
    private final BoardImpl board = new BoardImpl();

    public Player(String nick) { this.nick = nick; }

    /* ===== Métodos solicitados por GameController ===== */

    /** ¿Está libre la celda (r,c)? ⇒ delega en BoardImpl.free() */
    public boolean freePosition(int r, int c) {
        return board.free(r, c);
    }

    /** Coloca un barco en el tablero propio */
    public void boatPosition(boolean vertical, int r, int c, int size) {
        try {
            board.placeBoat(r, c, size, vertical);
        } catch (InvalidPlacementException e) {
            // Aquí no debería llegar porque el controlador ya comprobó
            // que las celdas están libres y dentro de rango. Si ocurre,
            // simplemente ignoramos o registra el error según necesites.
            System.err.println(e.getMessage());
        }
    }

    /* ===== Métodos que ya usaba tu lógica de disparo ===== */

    public java.util.ArrayList<java.util.ArrayList<Integer>> getPlayerTable() {
        return board.toList();
    }



    public String getPlayerNickName() { return nick; }

    /* ===== Shooter ===== */
    @Override public int[] nextShot(Board enemy) { return new int[]{-1, -1}; }
    @Override public Board getOwnBoard() { return board; }
}
