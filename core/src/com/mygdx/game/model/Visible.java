package com.mygdx.game.model;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public interface Visible {
    float xCoordinate = 0;
    float yCoordinate = 0;

    default boolean collidesWith(Visible anotherVisible) {
        return Intersector.overlapConvexPolygons(getShape(), anotherVisible.getShape());
    }

    default Vector2 getPosition() {
        return new Vector2(getShape().getX(), getShape().getY());
    }
    default void setPosition(Vector2 position) {
        getShape().setPosition(position.x, position.y);
    }
    Polygon getShape();
}
