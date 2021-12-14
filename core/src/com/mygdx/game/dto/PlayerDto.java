package com.mygdx.game.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerDto implements Dto {
    private final String id;
    private final TankDto tankDto;

    @JsonCreator
    public PlayerDto(
            @JsonProperty("id") String id,
            @JsonProperty("tank") TankDto tankDto) {
        this.id = id;
        this.tankDto = tankDto;
    }

    public String getId() {
        return id;
    }

    public TankDto getTankDto() {
        return tankDto;
    }
}
