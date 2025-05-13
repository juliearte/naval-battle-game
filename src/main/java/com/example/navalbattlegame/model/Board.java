package com.example.navalbattlegame.model;



import com.example.navalbattlegame.exceptions.AlreadyShotException;
import com.example.navalbattlegame.exceptions.InvalidPlacementException;

public interface Board {

    int SIZE = 10;

    /** Coloca un barco de longitud {@code size}. */
    void placeBoat(int row, int col, int size, boolean vertical)
            throws InvalidPlacementException;

    /** Devuelve el valor crudo almacenado en la celda. */
    int getCell(int row, int col);

    /** Ejecuta un disparo y devuelve el resultado. */
    ShotResult shoot(int row, int col) throws AlreadyShotException;

    /** Devuelve {@code true} si TODOS los barcos están hundidos. */
    boolean allBoatsSunk();
}
