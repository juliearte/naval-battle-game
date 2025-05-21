package com.example.navalbattlegame.model;



/**Behavior common to any “player” (human or AI). */
public interface Shooter {

    /** Selects or receives the position of the next shot. */
    int[] nextShot(Board enemyBoard);

    /**Returns own board. */
    Board getOwnBoard();
}
