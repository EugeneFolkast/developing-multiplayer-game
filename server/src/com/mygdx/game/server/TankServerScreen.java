package com.mygdx.game.server;

import com.badlogic.gdx.ScreenAdapter;
import com.mygdx.game.container.BarricadeContainer;
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
import com.mygdx.game.model.Barricade;
import com.mygdx.game.model.RemotePlayer;
import com.mygdx.game.server.connection.Server;

import java.util.UUID;

public class TankServerScreen extends ScreenAdapter {
    private final Server server;
    private final PlayersContainer<RemotePlayer> playersContainer;
    private final BulletsContainer bulletsContainer;
    private final Arena arena;
    private final Respawner respawner;
    private final Collider collider;
    private final BarricadeContainer barricadeContainer;

    public TankServerScreen(Server server,
                                 PlayersContainer<RemotePlayer> playersContainer, BulletsContainer bulletsContainer,
                                 BarricadeContainer barricadeContainer,
                                 Arena arena, Respawner respawner, Collider collider) {
        this.server = server;
        this.playersContainer = playersContainer;
        this.bulletsContainer = bulletsContainer;
        this.barricadeContainer = barricadeContainer;
        this.arena = arena;
        this.respawner = respawner;
        this.collider = collider;
    }


    @Override
    public void show() {
        for (int i = 0; i < arena.getMapArray().length; i++){
            for (int j = 0; j < arena.getMapArray()[i].length; j++){
                if (arena.getMapArray()[i][j] != 0) {
                    Barricade barricade = new Barricade(null, j * 64, i * 64,
                            arena.getMapArray()[i][j], UUID.randomUUID());
                    barricadeContainer.add(barricade);
                }
            }
        }

        server.onPlayerConnected(playerDto -> {
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
    public void render(float delta) {
        respawner.respawn();
        collider.checkBulletCollisions();

        playersContainer.update(delta);
        playersContainer.streamTank()
                .forEach(player -> arena.ensurePlacementWithinBounds(player, barricadeContainer));
        playersContainer.obtainAndStreamBullets()
                .forEach(bulletsContainer::add);

        bulletsContainer.update(delta);
        bulletsContainer.stream()
                .forEach(bullet -> arena.ensurePlacementWithinBounds(bullet, barricadeContainer));

        server.broadcast(GameStateMapper.fromState(playersContainer, bulletsContainer, arena.getMapArray()));
    }
}

