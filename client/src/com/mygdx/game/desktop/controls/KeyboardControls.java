package com.mygdx.game.desktop.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.controls.Controls;

public class KeyboardControls implements Controls {
    @Override
    public boolean forward() {
        return Gdx.input.isKeyPressed(Input.Keys.W);
    }

    @Override
    public boolean left() {
        return Gdx.input.isKeyPressed(Input.Keys.A);
    }

    @Override
    public boolean right() {
        return Gdx.input.isKeyPressed(Input.Keys.D);
    }

    @Override
    public boolean back() {
        return Gdx.input.isKeyPressed(Input.Keys.S);
    }

    @Override
    public boolean shoot() {
        return Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
    }
}
