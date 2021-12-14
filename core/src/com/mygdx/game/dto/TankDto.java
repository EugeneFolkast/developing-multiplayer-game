package com.mygdx.game.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TankDto implements Dto {
    private final int x;
    private final int y;
    private final String rotation;

    @JsonCreator
    public TankDto(
            @JsonProperty("x") int x,
            @JsonProperty("y") int y,
            @JsonProperty("rotation") String rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getRotation() {
        return rotation;
    }
}