package com.mygdx.game.dto.mapper;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.controls.Controls;
import com.mygdx.game.controls.RemoteControls;
import com.mygdx.game.dto.PlayerDto;
import com.mygdx.game.dto.TankDto;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.RemotePlayer;
import com.mygdx.game.model.Tank;

import java.util.Optional;
import java.util.UUID;

public class PlayerMapper {
    public static PlayerDto fromPlayer(Player player) {
        return new PlayerDto(player.getId().toString(), player.getColor().toString(),
                player.getTank()
                        .map(TankMapper::fromShip)
                        .orElseGet(() -> null)
        );
    }

    public static RemotePlayer remotePlayerFromDto(PlayerDto dto) {
        return new RemotePlayer(UUID.fromString(dto.getId()), new RemoteControls(),
                Color.valueOf(dto.getColor()));
    }

    public static Player localPlayerFromDto(PlayerDto dto, Controls controls) {
        Player player = new Player(UUID.fromString(dto.getId()), controls, Color.valueOf(dto.getColor()));
        player.setTank(TankMapper.fromDto(dto.getTankDto(), player));
        return player;
    }

    public static void updateByDto(Player player, PlayerDto dto) {
        Optional<Tank> currentShip = player.getTank();
        TankDto tankDto = dto.getTankDto();

        if(currentShip.isPresent() && tankDto != null) {
            TankMapper.updateByDto(currentShip.get(), tankDto);
        }
        else {
            player.setTank(TankMapper.fromDto(tankDto, player));
        }
    }
}