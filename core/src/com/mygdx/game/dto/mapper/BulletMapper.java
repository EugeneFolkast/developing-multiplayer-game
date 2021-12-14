package com.mygdx.game.dto.mapper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.container.Container;
import com.mygdx.game.dto.BulletDto;
import com.mygdx.game.model.Bullet;
import com.mygdx.game.model.Player;

import java.util.UUID;

public class BulletMapper {
    public static BulletDto fromBullet(Bullet bullet) {
        return new BulletDto(bullet.getId().toString(),
                bullet.getxCoordinate(), bullet.getyCoordinate(), bullet.getRotation(),
                bullet.getShooterId().toString());
    }

    public static Bullet fromDto(BulletDto dto, Container<Player> playersContainer) {
        Player shooter = playersContainer.getById(dto.getShooterId())
                .orElseThrow(() -> new RuntimeException("Cannot find Player of id " + dto.getShooterId() + " to create a Bullet."));
        return new Bullet(new Texture(Gdx.files.internal("shot.png")), dto.getX(),
                dto.getY(), 50, UUID.fromString(dto.getId()), shooter);
    }

    public static void updateByDto(Bullet bullet, BulletDto dto) {
        bullet.setPosition(dto.getX(), dto.getY());
    }
}