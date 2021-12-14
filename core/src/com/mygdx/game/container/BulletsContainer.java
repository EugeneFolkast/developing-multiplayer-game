package com.mygdx.game.container;

import com.mygdx.game.model.Bullet;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BulletsContainer implements Container<Bullet> {
    private final List<Bullet> bullets;

    public BulletsContainer(List<Bullet> bullets) {
        this.bullets = bullets;
    }

    public BulletsContainer() {
        this(new ArrayList<>());
    }

    @Override
    public void add(Bullet bullet) {
        bullets.add(bullet);
    }

    public void removeByPlayerId(UUID id) {
        bullets.removeIf(bullet -> bullet.getShooterId().equals(id));
    }

    @Override
    public List<Bullet> getAll() {
        return bullets;
    }

    @Override
    public void update(int[][] map, float delta) {
        bullets.forEach(bullet -> bullet.move(map));
        bullets.removeIf(bullet -> !bullet.isInRange() || bullet.hasHitSomething());
    }
}