package com.mygdx.game.util.maze;

public class Position {
    private int px;
    private int py;
    public Position(int px, int py) {
        this.px = px;
        this.py = py;
    }
    public int getPx() {
        return px;
    }
    public void setPx(int px) {
        this.px = px;
    }
    public int getPy() {
        return py;
    }
    public void setPy(int py) {
        this.py = py;
    }
}
