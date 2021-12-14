package com.mygdx.game.model;

public interface Visible {
    float xCoordinate = 0;
    float yCoordinate = 0;
    void setPosition(float x, float y);

    public default float getxCoordinate() {
        return xCoordinate;
    }

    public default float getyCoordinate() {
        return yCoordinate;
    }

}
