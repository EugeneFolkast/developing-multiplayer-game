package com.mygdx.game.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TankDto implements Dto {
    private final float x;
    private final float y;
    private final float rotation;

    @JsonCreator
    public TankDto(
            @JsonProperty("x") float x,
            @JsonProperty("y") float y,
            @JsonProperty("rotation") float rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRotation() {
        return rotation;
    }
}