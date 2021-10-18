package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.controls.Controls;
import com.mygdx.game.util.Vectors;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class Tank implements Visible {
    private static final float[] VERTICES = new float[] {
            0, 0,
            16, 32,
            32, 0,
            16, 10
    };
    private static final float MAX_SPEED = 500f;
    private static final float ACCELERATION = 300f;
    private static final float ROTATION = 10f;
    private static final float DRAG = 2f;
    private static final Vector2 MIDDLE = new Vector2(16, 16);
    private static final Vector2 BULLET_OUTPUT = new Vector2(16, 32);
    private static final Duration SHOT_INTERVAL = Duration.ofMillis(300);

    private final Player owner;
    private final Polygon shape;
    private final Vector2 velocity;
    private float rotationVelocity;
    private Instant lastShot;
    private boolean canShoot;
    private boolean wantsToShoot;


    public Tank(Player owner, Vector2 startingPosition, float startingRotation) {
        shape = new Polygon(VERTICES);
        shape.setOrigin(MIDDLE.x, MIDDLE.y);
        shape.setPosition(startingPosition.x, startingPosition.y);
        shape.setRotation(startingRotation);
        this.owner = owner;
        velocity = new Vector2(0, 0);
        lastShot = Instant.EPOCH;
    }

    public static Vector2 getMiddle() {
        return new Vector2(MIDDLE);
    }

    public void control(Controls controls, float delta) {
        if(controls.forward()) moveForwards(delta);
        if(controls.left()) rotateLeft(delta);
        if(controls.right()) rotateRight(delta);
        if(controls.back()) rotateBack(delta);
        wantsToShoot = controls.shoot();
    }

    public void update(float delta) {
        applyMovement(delta);
        applyShootingPossibility();
    }

    public Optional<Bullet> obtainBullet() {
        if(canShoot && wantsToShoot) {
            lastShot = Instant.now();
            return Optional.of(new Bullet(
                    UUID.randomUUID(),
                    owner,
                    bulletStartingPosition(),
                    shape.getRotation())
            );
        }
        return Optional.empty();
    }

    @Override
    public Color getColor() {
        return owner.getColor();
    }

    @Override
    public Polygon getShape() {
        return shape;
    }

    private Vector2 getDirection() {
        return Vectors.getDirectionVector(shape.getRotation());
    }

    private void moveForwards(float delta) {
        Vector2 direction = getDirection();
        velocity.x += delta * ACCELERATION * direction.x;
        velocity.y += delta * ACCELERATION * direction.y;
    }

    private void rotateLeft(float delta) {
        rotationVelocity += delta * ROTATION;
    }

    private void rotateRight(float delta) {
        rotationVelocity -= delta * ROTATION;
    }

    private void rotateBack(float delta) {
        Vector2 direction = getDirection();
        velocity.x -= delta * ACCELERATION * direction.x;
        velocity.y -= delta * ACCELERATION * direction.y;
    }

    private void applyMovement(float delta) {
        velocity.clamp(0, MAX_SPEED);

        velocity.x -= delta * DRAG * velocity.x;
        velocity.y -= delta * DRAG * velocity.y;
        rotationVelocity -= delta * DRAG * rotationVelocity;

        float x = delta * velocity.x;
        float y = delta * velocity.y;
        shape.translate(x, y);
        shape.rotate(rotationVelocity);
    }

    private void applyShootingPossibility() {
        canShoot = Instant.now().isAfter(lastShot.plus(SHOT_INTERVAL));
    }

    private Vector2 bulletStartingPosition() {
        return new Vector2(shape.getX(), shape.getY()).add(BULLET_OUTPUT);
    }
}