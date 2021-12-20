package com.mygdx.game.dto.mapper;

import com.mygdx.game.container.Container;
import com.mygdx.game.dto.BulletDto;
import com.mygdx.game.dto.GameStateDto;
import com.mygdx.game.dto.PlayerDto;
import com.mygdx.game.model.Bullet;
import com.mygdx.game.model.Player;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class GameStateMapper {
    public static GameStateDto fromState(Container<? extends Player> players, Container<? extends Player> bots,
                                         Container<Bullet> bullets, int[][] arena) {
        List<PlayerDto> playerDtos = players.stream()
                .map(PlayerMapper::fromPlayer)
                .collect(toList());
        List<PlayerDto> botsDtos = bots.stream()
                .map(PlayerMapper::fromPlayer)
                .collect(toList());
        List<PlayerDto> tanksDtos = Stream.concat(playerDtos.stream(), botsDtos.stream()).collect(toList());

        List<BulletDto> bulletDtos = bullets.stream()
                .map(BulletMapper::fromBullet)
                .collect(toList());

        return new GameStateDto(tanksDtos, bulletDtos, arena);
    }
}
