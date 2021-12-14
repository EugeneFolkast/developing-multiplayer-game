package com.mygdx.game.model;

public interface Visible {
    Integer xCoordinate = 0;
    Integer yCoordinate = 0;
    void setPosition(int x, int y);

    public default Integer getxCoordinate() {
        return xCoordinate;
    }

    public default Integer getyCoordinate() {
        return yCoordinate;
    }

}
