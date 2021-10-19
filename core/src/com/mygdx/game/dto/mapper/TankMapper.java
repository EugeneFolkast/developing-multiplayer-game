package com.mygdx.game.dto.mapper;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.dto.TankDto;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.Tank;

public class TankMapper {
    public static TankDto fromShip(Tank tank) {
        Vector2 shipPosition = tank.getPosition();
        return new TankDto(shipPosition.x, shipPosition.y, tank.getRotation());
    }

    public static Tank fromDto(TankDto dto, Player owner) {
        if(dto == null) return null;
        return new Tank(owner, new Vector2(dto.getX(), dto.getY()), dto.getRotation());
    }

    public static void updateByDto(Tank ship, TankDto dto) {
        ship.setPosition(new Vector2(dto.getX(), dto.getY()));
        ship.setRotation(dto.getRotation());
    }
}