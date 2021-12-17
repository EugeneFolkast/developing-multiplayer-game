package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.controls.Controls;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class Tank implements Visible{
    private final Player owner;
    private String playerImage;
    private Integer health;
    private float xCoordinate;
    private float yCoordinate;
    private float rotation;
    private String gunDirection;
    private boolean canShoot;
    private boolean wantsToShoot;
    private Instant lastShot;
    private static final Duration SHOT_INTERVAL = Duration.ofMillis(600);

    public Tank(Player owner, String playerImage, Integer health, float xCoordinate, float yCoordinate, float rotation){
        this.owner = owner;
        this.playerImage = playerImage;
        this.health = health;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.rotation = 0.0F;
        lastShot = Instant.EPOCH;
    }

    public void setGunDirection(String gunDirection) {
        this.gunDirection = gunDirection;
    }

    public Integer getHealth() {
        return health;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    @Override
    public void setPosition(float x, float y) {
        xCoordinate=x;
        yCoordinate=y;
    }

    public Optional<Bullet> obtainBullet() {
        if(canShoot && wantsToShoot) {
            lastShot = Instant.now();
             Vector2 bulletPos = bulletStartingPosition();
            return Optional.of(new Bullet(
                    null,
                    (int)bulletPos.x, (int)bulletPos.y, 50,
                    UUID.randomUUID(),owner, rotation)
            );
        }
        return Optional.empty();
    }

    public void control(int[][] map, Controls controls) {
        float x = xCoordinate;
        float y = yCoordinate;
        applyShootingPossibility();
        if(controls.forward() && map[(int)y+1][(int)x]==0) {
            x += 0.1 * xCoordinate * rotation;
            y += 0.1 * xCoordinate * rotation;
        }
        if(controls.back() && map[(int)y][(int)x+1]==0) {
            x -= 0.1 * xCoordinate * rotation;
            y -= 0.1 * xCoordinate * rotation;
        }
        if(controls.left() && map[(int)y][(int)x]==0) {
            rotation += 0.1;
        }
        if(controls.right() && map[(int)y][(int)x]==0) {
            rotation -= 0.1;
        }

        wantsToShoot = controls.shoot();

        int intx = (int)x;
        int inty = (int)y;


        setPosition(x, y);
    }

    public float getxCoordinate() {
        return xCoordinate;
    }

    public float getyCoordinate() {
        return yCoordinate;
    }

    public String getPlayerImage() {
        return playerImage;
    }

    public void setxCoordinate(Integer xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public void setyCoordinate(Integer yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public void setPlayerImage(String playerImage) {
        this.playerImage = playerImage;
    }

    private void applyShootingPossibility() {
        canShoot = Instant.now().isAfter(lastShot.plus(SHOT_INTERVAL));
    }

    private Vector2 bulletStartingPosition() {
        return new Vector2(xCoordinate, yCoordinate);
    }


}
