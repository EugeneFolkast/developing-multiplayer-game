package com.mygdx.game.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ControlsDto implements Dto {
    private final boolean forward;
    private final boolean left;
    private final boolean right;
    private final boolean shoot;
    private final boolean back;

    @JsonCreator
    public ControlsDto(
            @JsonProperty("forward") boolean forward,
            @JsonProperty("left") boolean left,
            @JsonProperty("right") boolean right,
            @JsonProperty("shoot") boolean shoot,
            @JsonProperty("back") boolean back) {
        this.forward = forward;
        this.left = left;
        this.right = right;
        this.shoot = shoot;
        this.back = back;
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