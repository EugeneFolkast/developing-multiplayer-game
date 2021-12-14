package com.mygdx.game.model;

public class Arena {
    private int mapArray[][];

    public Arena() {
        mapArray = new int[][]{ {3,3,3,3,3,3,3,3,3,3},
                {3,0,0,0,2,0,0,0,0,3},
                {3,0,0,0,2,0,0,0,0,3},
                {3,0,1,1,2,1,1,1,0,3},
                {3,0,0,0,1,0,0,0,0,3},
                {3,0,0,0,1,0,0,0,0,3},
                {3,0,1,1,2,1,1,1,0,3},
                {3,0,0,0,2,0,0,0,0,3},
                {3,0,0,0,2,0,0,0,0,3},
                {3,3,3,3,3,3,3,3,3,3}, };
    }

    public int[][] getMapArray() {
        return mapArray;
    }

    public void ensurePlacementWithinBounds(Visible visible) {
//        int x = visible.getxCoordinate();
//        int y = visible.getyCoordinate();
//
//        if(x <=9 && x >= 0 && y <= 8 && y >= 0 && mapArray[x] [y] == 0)
//            visible.setPosition(x, y);
    }
}