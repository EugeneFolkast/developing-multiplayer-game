package com.mygdx.game.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BulletDto implements Dto {
    private final String id;
    private final float x;
    private final float y;
    private final String rotation;
    private final String shooterId;

    @JsonCreator
    public BulletDto(
            @JsonProperty("id") String id,
            @JsonProperty("x") float x,
            @JsonProperty("y") float y,
            @JsonProperty("rotation") String rotation,
            @JsonProperty("shooterId") String shooterId) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.shooterId = shooterId;
    }

    public String getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getRotation() {
        return rotation;
    }

    public String getShooterId() {
        return shooterId;
    }
}