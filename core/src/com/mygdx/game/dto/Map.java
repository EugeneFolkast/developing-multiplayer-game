package com.mygdx.game.dto;

import com.badlogic.gdx.graphics.Texture;

public class Map {
    private Texture mapImage;
    private Integer xCoor;
    private Integer yCoor;
    private int mapArray[][];

    public Map(Texture mapImage, Integer xCoor, Integer yCoor, int[][] mapArray){
        this.mapImage = mapImage;
        this.xCoor = xCoor;
        this.yCoor = yCoor;
        this.mapArray = mapArray;
    }

    public Texture getMapImage() {
        return mapImage;
    }

    public Integer getxCoor() {
        return xCoor;
    }

    public Integer getyCoor() {
        return yCoor;
    }

    public int[][] getMapArray() {
        return mapArray;
    }

    public void setMapImage(Texture mapImage) {
        this.mapImage = mapImage;
    }

    public void setMapArray(int[][] mapArray) {
        this.mapArray = mapArray;
    }

    public void setxCoor(Integer xCoor) {
        this.xCoor = xCoor;
    }

    public void setyCoor(Integer yCoor) {
        this.yCoor = yCoor;
    }
}
