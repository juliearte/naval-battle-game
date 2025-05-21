package com.example.navalbattlegame.model;

import com.example.navalbattlegame.exceptions.InvalidPlacementException;

public class Player implements Shooter {

    private final String nick;
    private final BoardImpl board = new BoardImpl();

    public Player(String nick) { this.nick = nick; }

    /** =====Methods requested by GameController ===== */

    /**Is cell (r,c) free? ⇒ delegate to BoardImpl.free()*/
    public boolean freePosition(int r, int c) {
        return board.free(r, c);
    }

    /**Place a ship on your own board */
    public void boatPosition(boolean vertical, int r, int c, int size) {
        try {
            board.placeBoat(r, c, size, vertical);
        } catch (InvalidPlacementException e) {
            // It shouldn't happen here because the controller has already checked
            // that the cells are free and within range. If it does,
            // we simply ignore or log the error as needed.
            System.err.println(e.getMessage());
        }
    }

    /** ===== Methods that your trigger logic already used ===== */

    public java.util.ArrayList<java.util.ArrayList<Integer>> getPlayerTable() {
        return board.toList();
    }



    public String getPlayerNickName() { return nick; }

    /* ===== Shooter ===== */
    @Override public int[] nextShot(Board enemy) { return new int[]{-1, -1}; }
    @Override public Board getOwnBoard() { return board; }
}
