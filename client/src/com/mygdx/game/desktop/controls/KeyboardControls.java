package com.mygdx.game.desktop.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.controls.Controls;

public class KeyboardControls implements Controls {
    @Override
    public boolean forward() {
        return Gdx.input.isKeyPressed(Input.Keys.UP);
    }

    @Override
    public boolean left() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT);
    }

    @Override
    public boolean right() {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }

    @Override
    public boolean back() {
        return Gdx.input.isKeyPressed(Input.Keys.DOWN);
    }

    @Override
    public boolean shoot() {
        return Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
    }
}
