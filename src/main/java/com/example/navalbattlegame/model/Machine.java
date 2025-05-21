package com.example.navalbattlegame.model;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Machine implements Shooter {

    private final BoardImpl board = new BoardImpl();
    private final Random rnd = new Random();

    // Ready to store successful shots and their surroundings
    private final List<int[]> targetQueue = new ArrayList<>();
    private int[] lastHit = null;
    private int lastDirection = -1; // -1: no hay dirección, 0: arriba, 1: derecha, 2: abajo, 3: izquierda

    public Machine(){ autoFillBoats(); }

    /** ---------- Placement AI ---------- */
    public void autoFillBoats(){
        int[] sizes = {4,3,3,2,2,2,1,1,1,1};
        for(int s:sizes) while(!tryPlaceRandom(s)){}
    }
    private boolean tryPlaceRandom(int size){
        int r=rnd.nextInt(Board.SIZE), c=rnd.nextInt(Board.SIZE);
        boolean v=rnd.nextBoolean();
        try{ board.placeBoat(r,c,size,v); return true; }
        catch(Exception e){ return false; }
    }

    /** ---------- Improved shooting AI ---------- */
    @Override
    public int[] nextShot(Board enemy) {
        // we have potential targets in the queue, we use those first.
        if (!targetQueue.isEmpty()) {
            return getTargetedShot(enemy);
        }

        // If we just hit but there are no targets in the queue, we go back to random shooting
        if (lastHit != null) {
            lastHit = null;
            lastDirection = -1;
        }

        //Random shooting as a last resort
        return getRandomShot(enemy);
    }

    private int[] getTargetedShot(Board enemy) {
        // We tested every position on our target list
        while (!targetQueue.isEmpty()) {
            int[] shot = targetQueue.remove(0);
            int r = shot[0], c = shot[1];

            // We check if the position has already been triggered
            if (isValidTarget(enemy, r, c)) {
                return new int[]{r, c};
            }
            //If it is not valid, we continue with the next one.
        }

        //If we get here, we don't find a valid target in the queue
        lastHit = null;
        lastDirection = -1;
        return getRandomShot(enemy);
    }

    private int[] getRandomShot(Board enemy) {
        int r, c;
        int attempts = 0;
        do {
            r = rnd.nextInt(Board.SIZE);
            c = rnd.nextInt(Board.SIZE);
            attempts++;

            // Avoid infinite loop in extreme cases
            if (attempts > 100) {
                //Exhaustive search for an unfired cell
                for (int i = 0; i < Board.SIZE; i++) {
                    for (int j = 0; j < Board.SIZE; j++) {
                        if (isValidTarget(enemy, i, j)) {
                            return new int[]{i, j};
                        }
                    }
                }
                // If we get here, there are no more valid cells
                return new int[]{0, 0};
            }
        } while (!isValidTarget(enemy, r, c));

        return new int[]{r, c};
    }

    //Checks if a cell is a valid target (has not been fired yet)
    private boolean isValidTarget(Board enemy, int r, int c) {
        if (r < 0 || r >= Board.SIZE || c < 0 || c >= Board.SIZE) {
            return false;
        }
        int cellValue = enemy.getCell(r, c);
        return cellValue != 5 && cellValue > -1; // Ni agua ni tocado ni hundido
    }




    @Override public Board getOwnBoard(){ return board; }

    /** ----------API expected by your GameController---------- */
    public java.util.ArrayList<java.util.ArrayList<Integer>> getmachineTable(){
        return board.toList();
    }

}