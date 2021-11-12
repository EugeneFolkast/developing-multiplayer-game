package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.controls.Controls;

import java.util.Optional;
import java.util.UUID;
import com.badlogic.gdx.graphics.Color;

import java.util.*;
public class Player implements Identifiable {
    private final UUID id;
    protected final Controls controls;
    private final Color color;
    protected Optional<Tank> tank;
    public static final List<Color> POSSIBLE_COLORS = Collections.unmodifiableList(Arrays.asList(
            Color.WHITE, Color.GRAY, Color.BLUE, Color.GREEN, Color.ORANGE, Color.LIGHT_GRAY));

    public Player(UUID id, Controls controls, Color color) {
        this.id = id;
        this.controls = controls;
        this.color = color;
        this.tank = Optional.empty();
    }

    public void setTank(Tank ship) {
        this.tank = Optional.ofNullable(ship);
    }

    public void noticeHit() {
        this.tank = Optional.empty();
    }

    public void update(float delta) {
        tank.ifPresent(tank -> {
            tank.control(controls, delta);
            tank.update(delta);
        });
    }

    @Override
    public UUID getId() {
        return id;
    }

    public Optional<Tank> getTank() {
        return tank;
    }

    public Color getColor() {
        return color;
    }
}
