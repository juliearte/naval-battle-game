package com.example.navalbattlegame.model;



import com.example.navalbattlegame.exceptions.AlreadyShotException;
import com.example.navalbattlegame.exceptions.InvalidPlacementException;

public interface Board {

    int SIZE = 10;

    /** Place a ship in length {@code size}. */
    void placeBoat(int row, int col, int size, boolean vertical)
            throws InvalidPlacementException;

    /** Returns the raw value stored in the cell. */
    int getCell(int row, int col);

        /** Executes a shot and returns the result. */
    ShotResult shoot(int row, int col) throws AlreadyShotException;

    /** Returns {@code true} if ALL ships are sunk. */
    boolean allBoatsSunk();
}
