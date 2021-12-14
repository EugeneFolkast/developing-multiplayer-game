package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Texture;

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
    private String rotation;
    private final Player shooter;
    private float remainingRange;
    private boolean hasHitSomething;
    private static final float RANGE = 30;

    public Bullet(Texture shotImage, float xCoordinate, float yCoordinate,
                  Integer damage, UUID id, Player shooter, String rotation){
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

    public String getRotation() {
        return rotation;
    }

    public void move(int[][] map) {
        remainingRange -= 1;
        if (Objects.equals(rotation, "left")){
            xCoordinate -= (float) 0.1;
        }
        else if (Objects.equals(rotation, "right")){
            xCoordinate += (float) 0.1;
        }
        else if (Objects.equals(rotation, "back")){
            yCoordinate -= (float) 0.1;
        }
        else if (Objects.equals(rotation, "forward")){
            yCoordinate += (float) 0.1;
        }
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
