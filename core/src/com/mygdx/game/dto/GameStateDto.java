package com.mygdx.game.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GameStateDto implements Dto {
    private final List<PlayerDto> players;
    private final List<BulletDto> bullets;
    private final int[][] arena;


    @JsonCreator
    public GameStateDto(
            @JsonProperty("players") List<PlayerDto> players,
            @JsonProperty("bullets") List<BulletDto> bullets,
            @JsonProperty("arena") int[][] arena) {
        this.players = players;
        this.bullets = bullets;
        this.arena = arena;
    }

    public List<PlayerDto> getPlayers() {
        return players;
    }

    public List<BulletDto> getBullets() {
        return bullets;
    }

    public int[][] getArena() {
        return arena;
    }
}