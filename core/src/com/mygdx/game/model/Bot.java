package com.mygdx.game.model;

import java.util.Optional;
import java.util.UUID;

public class Bot extends Player{
    private final UUID id;
    private Optional<Tank> tank;

    public Bot(UUID id) {
        super(id, null);
        this.id = id;
        this.tank = Optional.empty();
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setTank(Tank tank) {
        this.tank = Optional.ofNullable(tank);
    }

    public void noticeHit() {
        this.tank = Optional.empty();
    }

    @Override
    public void update(float delta) {
//        tank.ifPresent(tank -> {
//            tank.control(controls, delta);
//            tank.update(delta);
//        });
    }

    public Optional<Tank> getTank() {
        return tank;
    }
}
