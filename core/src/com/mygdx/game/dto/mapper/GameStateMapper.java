package com.mygdx.game.dto.mapper;

import com.mygdx.game.container.Container;
import com.mygdx.game.dto.BulletDto;
import com.mygdx.game.dto.GameStateDto;
import com.mygdx.game.dto.PlayerDto;
import com.mygdx.game.model.Bullet;
import com.mygdx.game.model.Player;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class GameStateMapper {
    public static GameStateDto fromState(Container<? extends Player> players, Container<Bullet> bullets) {
        List<PlayerDto> playerDtos = players.stream()
                .map(PlayerMapper::fromPlayer)
                .collect(toList());
        List<BulletDto> bulletDtos = bullets.stream()
                .map(BulletMapper::fromBullet)
                .collect(toList());

        return new GameStateDto(playerDtos, bulletDtos);
    }
}
