package com.example.navalbattlegame.model;

import com.example.navalbattlegame.exceptions.AlreadyShotException;
import com.example.navalbattlegame.exceptions.InvalidPlacementException;

import java.util.ArrayDeque;
import java.util.Queue;

public class BoardImpl implements Board, java.io.Serializable {

    /** 0 = water · 1-4 = ship · 5 = water shot · −1…−4 = hit · −5 = sunk*/
    private final int[][] grid = new int[SIZE][SIZE];
    private int sunkShipsCount = 0;  // Sunken ship counter

    /** ---------- utilities ---------- */
    @Override public int  getCell(int r,int c){ return grid[r][c]; }
    public  boolean free(int r,int c){ return grid[r][c]==0; }

    public java.util.ArrayList<java.util.ArrayList<Integer>> toList(){
        var list = new java.util.ArrayList<java.util.ArrayList<Integer>>(SIZE);
        for(int i=0;i<SIZE;i++){
            var row = new java.util.ArrayList<Integer>(SIZE);
            for(int j=0;j<SIZE;j++) row.add(grid[i][j]);
            list.add(row);
        }
        return list;
    }
    public void loadFromList(java.util.ArrayList<java.util.ArrayList<Integer>> src){
        sunkShipsCount = 0; //Reset counter on load
        for(int i=0;i<SIZE;i++)
            for(int j=0;j<SIZE;j++) {
                grid[i][j] = src.get(i).get(j);
                // Update sunken ship counter
                if (grid[i][j] == -5) {
                    sunkShipsCount++;
                }
            }
    }

    /** ---------- boat placement ---------- */
    @Override
    public void placeBoat(int r,int c,int size,boolean v)
            throws InvalidPlacementException {

        if(v && r+size>SIZE || !v && c+size>SIZE)
            throw new InvalidPlacementException("Fuera de rango");
        for(int k=0;k<size;k++)
            if(!free(r+(v?k:0), c+(v?0:k)))
                throw new InvalidPlacementException("Ocupado");

        for(int k=0;k<size;k++)
            grid[r+(v?k:0)][c+(v?0:k)] = size;          // 1-4 según barco
    }

    /** ---------- shot ---------- */
    @Override
    public ShotResult shoot(int r,int c){
        int v = grid[r][c];
        if(v==5 || v<0) throw new AlreadyShotException("¡Ya disparaste ahí!");

        if(v==0){ grid[r][c]=5; return ShotResult.WATER; }

        grid[r][c] = -v;                              //mark touched
        if (boatSunk(r, c, v)) {                        //sunk
            //Increase sunken ship counter
            sunkShipsCount++;
            floodFillTo(-v, -5);
            return ShotResult.SUNK;
        }
        return ShotResult.HIT;
    }

    /** Check if the ship containing (r,c) still has live parts */
    private boolean boatSunk(int r, int c, int size) {
        boolean[][] vis = new boolean[SIZE][SIZE];
        Queue<int[]> q = new ArrayDeque<>();
        q.add(new int[]{r,c});
        vis[r][c] = true;

        int[] dr = {1,-1,0,0}, dc = {0,0,1,-1};
        while(!q.isEmpty()){
            int[] p=q.poll(); int pr=p[0], pc=p[1];
            for(int k=0;k<4;k++){
                int nr=pr+dr[k], nc=pc+dc[k];
                if(nr<0||nr>=SIZE||nc<0||nc>=SIZE||vis[nr][nc]) continue;
                int val = grid[nr][nc];
                if(Math.abs(val)==size){            //same part of the boat
                    if(val>0) return false;         //living part found
                    vis[nr][nc]=true;
                    q.add(new int[]{nr,nc});
                }
            }
        }
        return true;   // no live parts remain
    }

    /** Converts all connected −v starting from the last touched cell */
    private void floodFillTo(int from, int to){
        for(int r=0;r<SIZE;r++)
            for(int c=0;c<SIZE;c++)
                if(grid[r][c]==from) grid[r][c]=to;
    }

    @Override
    public boolean allBoatsSunk(){
        // In Naval Battle, the total number of ships is 10
        return sunkShipsCount == 10;
    }
}