package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.util.Vectors;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Bullet implements Visible, Identifiable{
    private static final float[] VERTICES = new float[] {
            0, 0,
            16, 0,
            16, 16,
            0, 16
    };
    private static final float SPEED = 300f;
    private static final float RANGE = 300f;

    private final Polygon shape;
    private String shotImage;
    private Integer damage;
    private final UUID id;
    private float rotation;
    private final Player shooter;
    private float remainingRange;
    private boolean hasHitSomething;

    public Bullet(String shotImage, float xCoordinate, float yCoordinate,
                  Integer damage, UUID id, Player shooter, float rotation){
        shape = new Polygon(VERTICES);
        shape.setPosition(xCoordinate, yCoordinate);
        shape.setRotation(rotation);
        shape.setOrigin(0, -Tank.getMiddle().y);
        this.shotImage = shotImage;
        this.damage = damage;
        this.id = id;
        this.shooter = shooter;
        remainingRange = RANGE;
        hasHitSomething = false;
        this.rotation = rotation;
    }


    @Override
    public UUID getId() {
        return id;
    }


    public UUID getShooterId() {
        return shooter.getId();
    }

    @Override
    public Polygon getShape() {
        return shape;
    }


    public float getRotation() {
        return rotation;
    }

    public void move(float delta) {
        Vector2 direction = Vectors.getDirectionVector(shape.getRotation());
        Vector2 movement = new Vector2(direction.x * delta * SPEED, direction.y * delta * SPEED);
        remainingRange -= movement.len();
        shape.translate(movement.x, movement.y);
    }

    public boolean isInRange() {
        return remainingRange > 0;
    }

    public void noticeHit() {
        hasHitSomething = true;
    }

    public boolean hasHitSomething() {
        return hasHitSomething;
    }
}
