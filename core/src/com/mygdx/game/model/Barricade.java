package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import java.util.UUID;

public class Barricade implements Identifiable, Visible {
    private static final float[] VERTICES = new float[] {
            0, 0,
            0, 64,
            64, 64,
            64, 0
    };
    private Texture barricadeImage;
    private Integer xCoor;
    private Integer yCoor;
    private Integer typeOfDestructiveness;
    private final UUID id;
    private final Polygon shape;
    private static final Vector2 MIDDLE = new Vector2(32, 32);

    public Barricade(Texture barricadeImage, Integer xCoor, Integer yCoor, Integer typeOfDestructiveness, UUID id){
        this.barricadeImage = barricadeImage;
        this.typeOfDestructiveness = typeOfDestructiveness;
        this.xCoor = xCoor;
        this.yCoor = yCoor;
        this.id = id;
        shape = new Polygon(VERTICES);
        shape.setOrigin(MIDDLE.x, MIDDLE.y);
        shape.setPosition(xCoor, yCoor);
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

    public void hitting(){
        if (typeOfDestructiveness != 3)
            typeOfDestructiveness--;
    }

    @Override
    public Polygon getShape() {
        return shape;
    }
}
