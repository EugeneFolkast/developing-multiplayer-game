package com.mygdx.game.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class PlayerDto implements Dto {
    private final String id;
    private final String color;
    private final TankDto tankDto;

    @JsonCreator
    public PlayerDto(
            @JsonProperty("id") String id,
            @JsonProperty("color") String color,
            @JsonProperty("ship") TankDto tankDto) {
        this.id = id;
        this.color = color;
        this.tankDto = tankDto;
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public TankDto getTankDto() {
        return tankDto;
    }
}
