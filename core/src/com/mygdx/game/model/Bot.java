package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.controls.Controls;

import java.util.*;

public class Bot extends Player {

    public Bot(UUID id, Controls controls, Color color) {
        super(id, controls, color);
    }

    @Override
    public void update(float delta) {
        tank.ifPresent(tank -> {
            tank.control(controls, delta);
            tank.update(delta);
        });
    }

}
