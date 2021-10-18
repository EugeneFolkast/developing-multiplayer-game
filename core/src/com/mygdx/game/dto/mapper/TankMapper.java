package com.mygdx.game.dto.mapper;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.dto.TankDto;
import com.mygdx.game.model.Tank;

public class TankMapper {
    public static TankDto fromShip(Tank tank) {
        Vector2 shipPosition = tank.getPosition();
        return new TankDto(shipPosition.x, shipPosition.y, tank.getRotation());
    }
}