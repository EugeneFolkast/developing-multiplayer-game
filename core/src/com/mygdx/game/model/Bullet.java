package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Texture;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class Bullet implements Visible, Identifiable{
    private Texture shotImage;
    private Integer xCoordinate;
    private Integer yCoordinate;
    private Integer damage;
    private final UUID id;
    private String rotation;
    private final Player shooter;
    private float remainingRange;
    private boolean hasHitSomething;
    private static final float RANGE = 5;

    public Bullet(Texture shotImage, Integer xCoordinate, Integer yCoordinate, Integer damage, UUID id, Player shooter){
        this.shotImage = shotImage;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.damage = damage;
        this.id = id;
        this.shooter = shooter;
        remainingRange = RANGE;
        hasHitSomething = false;

    }


    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setPosition(int x, int y) {
        xCoordinate = x;
        yCoordinate = y;
    }

    public UUID getShooterId() {
        return shooter.getId();
    }

    @Override
    public Integer getxCoordinate() {
        return xCoordinate;
    }

    @Override
    public Integer getyCoordinate() {
        return yCoordinate;
    }

    public String getRotation() {
        return rotation;
    }

    public void move(int[][] map) {
        remainingRange -= 1;
//        xCoordinate = x;
//        yCoordinate = y;
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
