package com.mygdx.game.server;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.mygdx.game.container.BulletsContainer;
import com.mygdx.game.container.PlayersContainer;
import com.mygdx.game.dto.GameStateDto;
import com.mygdx.game.dto.PlayerDto;
import com.mygdx.game.dto.mapper.ControlsMapper;
import com.mygdx.game.dto.mapper.GameStateMapper;
import com.mygdx.game.dto.mapper.PlayerMapper;
import com.mygdx.game.manager.Respawner;
import com.mygdx.game.model.Arena;
import com.mygdx.game.model.RemotePlayer;
import com.mygdx.game.server.connection.Server;
import com.mygdx.game.server.connection.SocketIoServer;

import java.util.Map;

public class TankServerGame extends Game {
    private Screen tanks;
    private Server server;
    private BulletsContainer  bulletsContainer;
    private PlayersContainer<RemotePlayer>  playersContainer;
    private Respawner respawner;
    private Arena arena;
    @Override
    public void create() {
        arena = new Arena();
        Map<String, String> env = System.getenv();
        String host = env.getOrDefault("HOST", "localhost");
        int port = Integer.parseInt(env.getOrDefault("PORT", "8888"));
        server = new SocketIoServer(host, port);

        bulletsContainer = new BulletsContainer();
        playersContainer = new PlayersContainer<>();
        respawner = new Respawner<>(playersContainer, 8, 8);

        show();
    }

    public void show() {
        server.onPlayerConnected(playerDto -> {
            System.out.println(playerDto.getId());

            RemotePlayer connected = PlayerMapper.remotePlayerFromDto(playerDto);
            respawner.respawnFor(connected);
            PlayerDto connectedDto = PlayerMapper.fromPlayer(connected);
            GameStateDto gameStateDto = GameStateMapper.fromState(playersContainer, bulletsContainer, arena.getMapArray());

            server.sendIntroductoryDataToConnected(connectedDto, gameStateDto);
            server.notifyOtherPlayersAboutConnected(connectedDto);
            playersContainer.add(connected);
        });

        server.onPlayerDisconnected(id -> {
            playersContainer.removeById(id);
            bulletsContainer.removeByPlayerId(id);
        });

        server.onPlayerSentControls((id, controlsDto) -> {
            playersContainer
                    .getById(id)
                    .ifPresent(sender -> ControlsMapper
                            .setRemoteControlsByDto(controlsDto, sender.getRemoteControls()));
        });

        server.start();
    }


    @Override
    public void render(){
        respawner.respawn();

        playersContainer.update(arena.getMapArray(), (float) 0.1);
        playersContainer.streamTank()
                .forEach(arena::ensurePlacementWithinBounds);
        playersContainer.obtainAndStreamBullets()
                .forEach(bulletsContainer::add);

        bulletsContainer.update(arena.getMapArray(), (float) 0.1);
        bulletsContainer.stream()
                .forEach(arena::ensurePlacementWithinBounds);

        server.broadcast(GameStateMapper.fromState(playersContainer, bulletsContainer, arena.getMapArray()));
    }

    @Override
    public void dispose() {

    }
}
