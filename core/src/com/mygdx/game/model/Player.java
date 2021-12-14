package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.controls.Controls;

import java.util.Optional;
import java.util.UUID;

public class Player implements Identifiable{
    private final UUID id;
    private final Controls controls;
    private Optional<Tank> tank;

    public Player(UUID id, Controls controls) {
        this.id = id;
        this.controls = controls;
        this.tank = Optional.empty();
    }

    public void setTank(Tank tank) {
        this.tank = Optional.ofNullable(tank);
    }

    public void noticeHit() {
        this.tank = Optional.empty();
    }

    public void update(int[][] map, float delta) {
        tank.ifPresent(tank -> {
            tank.control(map, controls);
        });
    }

    @Override
    public UUID getId() {
        return id;
    }

    public Optional<Tank> getTank() {
        return tank;
    }


}
