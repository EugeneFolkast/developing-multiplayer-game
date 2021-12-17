package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.manager.Vectors;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Bullet implements Visible, Identifiable{
    private Texture shotImage;
    private float xCoordinate;
    private float yCoordinate;
    private Integer damage;
    private final UUID id;
    private float  rotation;
    private final Player shooter;
    private float remainingRange;
    private boolean hasHitSomething;
    private static final float RANGE = 30;

    public Bullet(Texture shotImage, float xCoordinate, float yCoordinate,
                  Integer damage, UUID id, Player shooter, float  rotation){
        this.shotImage = shotImage;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
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

    @Override
    public void setPosition(float x, float y) {
        xCoordinate = x;
        yCoordinate = y;
    }

    public UUID getShooterId() {
        return shooter.getId();
    }

    @Override
    public float getxCoordinate() {
        return xCoordinate;
    }

    @Override
    public float getyCoordinate() {
        return yCoordinate;
    }

    public float getRotation() {
        return rotation;
    }

    public void move(int[][] map) {
        remainingRange -= 1;

        Vector2 direction = Vectors.getDirectionVector(rotation);
        Vector2 movement = new Vector2((float) (direction.x * 0.1 * 0.1), (float) (direction.y * 0.1 * 0.1));
        remainingRange -= movement.len();
        xCoordinate= movement.x;
        yCoordinate =  movement.y;

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
