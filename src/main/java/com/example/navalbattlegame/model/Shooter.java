package com.example.navalbattlegame.model;



/** Comportamiento común a cualquier “jugador” (humano o IA). */
public interface Shooter {

    /** Selecciona o recibe la posición del siguiente disparo. */
    int[] nextShot(Board enemyBoard);

    /** Devuelve el tablero propio. */
    Board getOwnBoard();
}
