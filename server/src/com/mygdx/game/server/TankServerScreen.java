package com.mygdx.game.server;

import com.badlogic.gdx.ScreenAdapter;
import com.mygdx.game.container.BulletsContainer;
import com.mygdx.game.container.PlayersContainer;
import com.mygdx.game.dto.GameStateDto;
import com.mygdx.game.dto.PlayerDto;
import com.mygdx.game.dto.mapper.ControlsMapper;
import com.mygdx.game.dto.mapper.GameStateMapper;
import com.mygdx.game.dto.mapper.PlayerMapper;
import com.mygdx.game.manager.Collider;
import com.mygdx.game.manager.Respawner;
import com.mygdx.game.model.Arena;
import com.mygdx.game.model.RemotePlayer;
import com.mygdx.game.server.connection.Server;

import java.util.Optional;
import java.util.stream.Stream;

public class TankServerScreen extends ScreenAdapter {
    private final Server server;
    private final PlayersContainer<RemotePlayer> playersContainer;
    private final BulletsContainer bulletsContainer;
    private final Arena arena;
    private final Respawner respawner;
    private final Collider collider;

    public TankServerScreen(Server server,
                            PlayersContainer<RemotePlayer> playersContainer, BulletsContainer bulletsContainer,
                            Arena arena, Respawner respawner, Collider collider) {
        this.server = server;
        this.playersContainer = playersContainer;
        this.bulletsContainer = bulletsContainer;
        this.arena = arena;
        this.respawner = respawner;
        this.collider = collider;
    }

    @Override
    public void show() {
        server.onPlayerConnected(playerDto -> {
            
            RemotePlayer connected = PlayerMapper.remotePlayerFromDto(playerDto);
            respawner.respawnFor(connected);
            PlayerDto connectedDto = PlayerMapper.fromPlayer(connected);
            GameStateDto gameStateDto = GameStateMapper.fromState(playersContainer, bulletsContainer);

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
    public void render(float delta) {
        respawner.respawn();
        collider.checkCollisions();

        playersContainer.update(delta);
        playersContainer.streamShips()
                .forEach(arena::ensurePlacementWithinBounds);
        playersContainer.obtainAndStreamBullets()
                .forEach(bulletsContainer::add);

        bulletsContainer.update(delta);
        bulletsContainer.stream()
                .forEach(arena::ensurePlacementWithinBounds);

        server.broadcast(GameStateMapper.fromState(playersContainer, bulletsContainer));
    }
}