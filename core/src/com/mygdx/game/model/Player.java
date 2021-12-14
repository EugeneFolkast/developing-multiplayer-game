package com.mygdx.game.dto;

import com.badlogic.gdx.graphics.Texture;

import java.util.UUID;

public class Player{
    private final UUID id;
    private Texture playerImage;
    private Integer health;
    private Integer xCoordinate;
    private Integer yCoordinate;
    private String gunDirection;

    public Player(Texture playerImage, Integer health, Integer xCoordinate, Integer yCoordinate, UUID id){
        this.playerImage = playerImage;
        this.health = health;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.id = id;
    }

    public void setGunDirection(String gunDirection) {
        this.gunDirection = gunDirection;
    }

    public Integer getHealth() {
        return health;
    }

    public Integer getxCoordinate() {
        return xCoordinate;
    }

    public Integer getyCoordinate() {
        return yCoordinate;
    }

    public Texture getPlayerImage() {
        return playerImage;
    }

    public void setxCoordinate(Integer xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public void setyCoordinate(Integer yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public void setPlayerImage(Texture playerImage) {
        this.playerImage = playerImage;
    }
}
