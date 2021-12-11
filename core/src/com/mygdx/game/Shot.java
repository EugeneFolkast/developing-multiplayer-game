package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

import java.util.UUID;

public class Shot{
    private Texture shotImage;
    private Integer xCoordinate;
    private Integer yCoordinate;
    private Integer damage;
    private final UUID id;

    public Shot(Texture shotImage, Integer xCoordinate, Integer yCoordinate, Integer damage, UUID id){
        this.shotImage = shotImage;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.damage = damage;
        this.id = id;
    }


}
