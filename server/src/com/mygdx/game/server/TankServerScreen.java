package com.mygdx.game.server;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.container.BarricadeContainer;
import com.mygdx.game.container.BotContainer;
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
import com.mygdx.game.model.Bot;
import com.mygdx.game.model.RemotePlayer;
import com.mygdx.game.server.connection.Server;

import java.util.UUID;

public class TankServerScreen extends ScreenAdapter {
    private final Server server;
    private final PlayersContainer<RemotePlayer> playersContainer;
    private final BulletsContainer bulletsContainer;
    private final Arena arena;
    private final Respawner respawnerPlayer;
    private final Respawner respawnerBot;
    private final Collider collider;
    private final BarricadeContainer barricadeContainer;
    private final BotContainer<Bot> botContainer;

    public TankServerScreen(Server server,
                                 PlayersContainer<RemotePlayer> playersContainer, BulletsContainer bulletsContainer,
                                 BarricadeContainer barricadeContainer, BotContainer<Bot> botContainer,
                                 Arena arena, Respawner respawnerPlayer, Respawner respawnerBot, Collider collider) {
        this.server = server;
        this.playersContainer = playersContainer;
        this.bulletsContainer = bulletsContainer;
        this.barricadeContainer = barricadeContainer;
        this.botContainer = botContainer;
        this.arena = arena;
        this.respawnerPlayer = respawnerPlayer;
        this.respawnerBot = respawnerBot;
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

        for (int i =0; i<1; i++) {
            Bot bot = new Bot(UUID.randomUUID(), playersContainer, arena);
            botContainer.add(bot);
            respawnerBot.respawnFor(bot);
        }


        server.onPlayerConnected(playerDto -> {
            RemotePlayer connected = PlayerMapper.remotePlayerFromDto(playerDto);
            respawnerPlayer.respawnFor(connected);
            PlayerDto connectedDto = PlayerMapper.fromPlayer(connected);
            GameStateDto gameStateDto = GameStateMapper.fromState(playersContainer, botContainer, bulletsContainer, arena.getMapArray());

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
        respawnerPlayer.respawn();
        respawnerBot.respawn();
        collider.checkBulletCollisions();

        botContainer.update(delta);
        botContainer.obtainAndStreamBullets()
                .forEach(bulletsContainer::add);

        playersContainer.update(delta);
        playersContainer.streamTank()
                .forEach(arena::ensurePlacementWithinBounds);
        playersContainer.obtainAndStreamBullets()
                .forEach(bulletsContainer::add);

        bulletsContainer.update(delta);
        bulletsContainer.stream()
                .forEach(arena::ensurePlacementWithinBounds);

        barricadeContainer.update(delta);
        mapUpdate();

        server.broadcast(GameStateMapper.fromState(playersContainer, botContainer, bulletsContainer, arena.getMapArray()));
    }

    private void mapUpdate(){
        int[][] mapArray = new int[arena.getMapArray().length][arena.getMapArray()[0].length];

        barricadeContainer.getAll().forEach(barricade ->
        {
            Vector2 pos = barricade.getPosition();
            mapArray[(int) (pos.y/64)][(int) (pos.x/64)] = barricade.getTypeOfDestructiveness();
        });

        arena.setMapArray(mapArray);
    }
}

