package com.mygdx.game.dto.mapper;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.dto.BulletDto;
import com.mygdx.game.model.Bullet;

public class BulletMapper {
    public static BulletDto fromBullet(Bullet bullet) {
        Vector2 position = bullet.getPosition();
        return new BulletDto(bullet.getId().toString(),
                position.x, position.y, bullet.getRotation(),
                bullet.getShooterId().toString());
    }
}