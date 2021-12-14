package com.mygdx.game.dto;

import com.badlogic.gdx.graphics.Texture;

import java.util.UUID;

public class Barricade {
    private Texture barricadeImage;
    private Integer xCoor;
    private Integer yCoor;
    private Integer typeOfDestructiveness;
    private final UUID id;

    public Barricade(Texture barricadeImage, Integer xCoor, Integer yCoor, Integer typeOfDestructiveness, UUID id){
        this.barricadeImage = barricadeImage;
        this.typeOfDestructiveness = typeOfDestructiveness;
        this.xCoor = xCoor;
        this.yCoor = yCoor;
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public Integer getyCoor() {
        return yCoor;
    }

    public Integer getxCoor() {
        return xCoor;
    }

    public Integer getTypeOfDestructiveness() {
        return typeOfDestructiveness;
    }

    public Texture getBarricadeImage() {
        return barricadeImage;
    }

    public void setyCoor(Integer yCoor) {
        this.yCoor = yCoor;
    }

    public void setxCoor(Integer xCoor) {
        this.xCoor = xCoor;
    }

    public void setBarricadeImage(Texture barricadeImage) {
        this.barricadeImage = barricadeImage;
    }

    public void setTypeOfDestructiveness(Integer typeOfDestructiveness) {
        this.typeOfDestructiveness = typeOfDestructiveness;
    }
}
