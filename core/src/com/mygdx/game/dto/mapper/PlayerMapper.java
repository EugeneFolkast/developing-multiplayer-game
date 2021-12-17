package com.mygdx.game.dto.mapper;

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
        return new PlayerDto(player.getId().toString(),
                player.getTank()
                        .map(TankMapper::fromTank)
                        .orElseGet(() -> null)
        );
    }

    public static RemotePlayer remotePlayerFromDto(PlayerDto dto) {
        return new RemotePlayer(UUID.fromString(dto.getId()), new RemoteControls());
    }

    public static Player localPlayerFromDto(PlayerDto dto, Controls controls) {
        Player player = new Player(UUID.fromString(dto.getId()), controls);
        player.setTank(TankMapper.fromDto(dto.getTankDto(), player));
        return player;
    }

    public static void updateByDto(Player player, PlayerDto dto) {
        Optional<Tank> currentTank = player.getTank();
        TankDto tankDto = dto.getTankDto();

        if(currentTank.isPresent() && tankDto != null) {
            TankMapper.updateByDto(currentTank.get(), tankDto);
        }
        else {
            player.setTank(TankMapper.fromDto(tankDto, player));
        }
    }
}
