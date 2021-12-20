package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.controls.Controls;
import com.mygdx.game.util.Vectors;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Tank implements Visible{
    private static final float[] VERTICES = new float[] {
            0, 0,
            0, 64,
            64, 64,
            64, 0
    };
//    private static final float MAX_SPEED = 200f;
    private static final float ACCELERATION = 10f;
    private static final float ROTATION = 50;
    private static final float DRAG = 8f;
    private static final Vector2 MIDDLE = new Vector2(32, 32);
    private static final Vector2 BULLET_OUTPUT = new Vector2(0, 20);
    private static final Duration SHOT_INTERVAL = Duration.ofMillis(600);
    private final Polygon shape;
    private final Vector2 velocity;

    private final Player owner;
    private String playerImage;
    private Integer health;
    private float rotation;
    private String gunDirection;
    private boolean canShoot;
    private boolean wantsToShoot;
    private Instant lastShot;

    public Tank(Player owner, String playerImage, Integer health, float xCoordinate, float yCoordinate, float rotation){
        this.owner = owner;
        this.playerImage = playerImage;
        this.health = health;
        this.rotation = rotation;
        lastShot = Instant.EPOCH;

        shape = new Polygon(VERTICES);
        shape.setOrigin(MIDDLE.x, MIDDLE.y);
        shape.setPosition(xCoordinate, yCoordinate);
        shape.setRotation(rotation);
        velocity = new Vector2(0, 0);
    }

    public static Vector2 getMiddle() {
        return new Vector2(MIDDLE);
    }

    public void control(Controls controls, float delta) {
        if(controls.forward()) moveForwards(delta);
        if(controls.left()) moveBack(delta);
        if(controls.right()) rotateLeft(delta);
        if(controls.back()) rotateRight(delta);
        wantsToShoot = controls.shoot();
    }

    public void update(float delta) {
        applyMovement(delta);
        applyShootingPossibility();
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


    public Optional<Bullet> obtainBullet() {
        if(canShoot && wantsToShoot) {
            lastShot = Instant.now();
             Vector2 bulletPos = bulletStartingPosition();
            return Optional.of(new Bullet(
                    null, bulletPos.x, bulletPos.y, 50,
                    UUID.randomUUID(),owner, shape.getRotation())
            );
        }
        return Optional.empty();

    }

    @Override
    public Polygon getShape() {
        return shape;
    }

    private Vector2 getDirection() {
        return Vectors.getDirectionVector(shape.getRotation());
    }

    public String getPlayerImage() {
        return playerImage;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public void setPlayerImage(String playerImage) {
        this.playerImage = playerImage;
    }

    private void moveForwards(float delta) {

        Vector2 direction = getDirection();
        velocity.x += delta * ACCELERATION * direction.x;
        velocity.y += delta * ACCELERATION * direction.y;
    }

    private void moveBack(float delta) {
        Vector2 direction = getDirection();
        velocity.x -= delta * ACCELERATION * direction.x;
        velocity.y -= delta * ACCELERATION * direction.y;
    }

    private void rotateLeft(float delta) {
        rotation += delta * ROTATION;
    }

    private void rotateRight(float delta) {
        rotation -= delta * ROTATION;
    }

    private void applyMovement(float delta) {
//        velocity.clamp(0, MAX_SPEED);
//
        velocity.x -= delta * DRAG * velocity.x;
        velocity.y -= delta * DRAG * velocity.y;
//        rotation -= delta * DRAG * rotation;
//
//        float x = delta * velocity.x;
//        float y = delta * velocity.y;

        shape.translate(velocity.x, velocity.y);
        shape.setRotation(rotation);
    }

    private void applyShootingPossibility() {
        canShoot = Instant.now().isAfter(lastShot.plus(SHOT_INTERVAL));
    }

    private Vector2 bulletStartingPosition() {
        return new Vector2(shape.getX(), shape.getY()).add(BULLET_OUTPUT);
    }


}
