package com.mygdx.game.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ControlsDto implements Dto {
    private final boolean forward;
    private final boolean left;
    private final boolean right;
    private final boolean back;
    private final boolean shoot;

    @JsonCreator
    public ControlsDto(
            @JsonProperty("forward") boolean forward,
            @JsonProperty("left") boolean left,
            @JsonProperty("right") boolean right,
            @JsonProperty("back") boolean back,
            @JsonProperty("shoot") boolean shoot) {
        this.forward = forward;
        this.left = left;
        this.right = right;
        this.back = back;
        this.shoot = shoot;
    }

    public boolean getForward() {
        return forward;
    }

    public boolean getLeft() {
        return left;
    }

    public boolean getRight() {
        return right;
    }

    public boolean getBack() {
        return back;
    }

    public boolean getShoot() {
        return shoot;
    }
}