package com.mygdx.game.dto.mapper;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.controls.RemoteControls;
import com.mygdx.game.dto.PlayerDto;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.RemotePlayer;

import java.util.UUID;

public class PlayerMapper {
    public static PlayerDto fromPlayer(Player player) {
        return new PlayerDto(player.getId().toString(), player.getColor().toString(),
                player.getShip()
                        .map(TankMapper::fromShip)
                        .orElseGet(() -> null)
        );
    }

    public static RemotePlayer remotePlayerFromDto(PlayerDto dto) {
        return new RemotePlayer(UUID.fromString(dto.getId()), new RemoteControls(),
                Color.valueOf(dto.getColor()));
    }
}