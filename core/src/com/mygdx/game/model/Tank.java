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

public class Tank implements Visible{
    private final Player owner;
    private String playerImage;
    private Integer health;
    private Integer xCoordinate;
    private Integer yCoordinate;
    private String rotation;
    private String gunDirection;
    private boolean canShoot;
    private boolean wantsToShoot;
    private Instant lastShot;
    private static final Duration SHOT_INTERVAL = Duration.ofMillis(300);

    public Tank(Player owner, String playerImage, Integer health, Integer xCoordinate, Integer yCoordinate, String rotation){
        this.owner = owner;
        this.playerImage = playerImage;
        this.health = health;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.rotation = rotation;
        lastShot = Instant.EPOCH;
    }

    public void setGunDirection(String gunDirection) {
        this.gunDirection = gunDirection;
    }

    public Integer getHealth() {
        return health;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    @Override
    public void setPosition(int x, int y) {
        xCoordinate=x;
        yCoordinate=y;
    }

    public Optional<Bullet> obtainBullet() {
        if(canShoot && wantsToShoot) {
            lastShot = Instant.now();
            Vector2 bulletPos = bulletStartingPosition();
            return Optional.of(new Bullet(
                    new Texture(Gdx.files.internal("shot.png")),
                    (int)bulletPos.x, (int)bulletPos.y, 50, UUID.randomUUID(),
                            owner)
            );
        }
        return Optional.empty();
    }

    public void control(int[][] map, Controls controls) {
        int x = xCoordinate;
        int y = yCoordinate;
        if(controls.forward()) {
            y += 1;
        }
        if(controls.back()) {
            y -= 1;
        }
        if(controls.left()) {
            x -= 1;
        }
        if(controls.right()) {
            x += 1;
        }
        wantsToShoot = controls.shoot();

        if(x <=9 && x >= 0 && y <= 8 && y >= 0 && map[x][y] == 0)
            this.setPosition(x, y);
    }

    public Integer getxCoordinate() {
        return xCoordinate;
    }

    public Integer getyCoordinate() {
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
        if(Objects.equals(rotation, "left"))
            return new Vector2(xCoordinate-1, yCoordinate);
        else if (Objects.equals(rotation, "right"))
            return new Vector2(xCoordinate+1, yCoordinate);
        else if (Objects.equals(rotation, "forward"))
            return new Vector2(xCoordinate, yCoordinate+1);
        else if (Objects.equals(rotation, "back"))
            return new Vector2(xCoordinate, yCoordinate-1);

        return null;
    }
}
