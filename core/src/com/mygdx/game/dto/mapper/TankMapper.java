package com.mygdx.game.dto.mapper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.dto.TankDto;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.Tank;

public class TankMapper {
    public static TankDto fromTank(Tank tank) {
        return new TankDto(tank.getxCoordinate(), tank.getyCoordinate(), tank.getRotation());
    }

    public static Tank fromDto(TankDto dto, Player owner) {
        if(dto == null) return null;
        return new Tank(owner, null,
                200, dto.getX(), dto.getY(), dto.getRotation());
    }

    public static void updateByDto(Tank tank, TankDto dto) {
        tank.setPosition(dto.getX(), dto.getY());
        tank.setRotation(dto.getRotation());
    }
}
