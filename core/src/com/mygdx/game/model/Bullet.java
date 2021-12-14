package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Texture;

import java.util.UUID;

public class Shot implements Visible, Identifiable{
    private Texture shotImage;
    private Integer xCoordinate;
    private Integer yCoordinate;
    private Integer damage;
    private final UUID id;
    private String rotation;

    public Shot(Texture shotImage, Integer xCoordinate, Integer yCoordinate, Integer damage, UUID id){
        this.shotImage = shotImage;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.damage = damage;
        this.id = id;
    }


    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setPosition(int x, int y) {
        xCoordinate = x;
        yCoordinate = y;
    }

    @Override
    public Integer getxCoordinate() {
        return xCoordinate;
    }

    @Override
    public Integer getyCoordinate() {
        return yCoordinate;
    }

    public String getRotation() {
        return rotation;
    }
}
