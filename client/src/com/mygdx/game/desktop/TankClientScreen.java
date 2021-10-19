package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.container.Container;
import com.mygdx.game.container.PlayersContainer;
import com.mygdx.game.controls.Controls;
import com.mygdx.game.controls.NoopControls;
import com.mygdx.game.desktop.connection.Client;
import com.mygdx.game.desktop.rendering.ContainerRenderer;
import com.mygdx.game.dto.BulletDto;
import com.mygdx.game.dto.GameStateDto;
import com.mygdx.game.dto.PlayerDto;
import com.mygdx.game.dto.mapper.BulletMapper;
import com.mygdx.game.dto.mapper.ControlsMapper;
import com.mygdx.game.dto.mapper.PlayerMapper;
import com.mygdx.game.model.Bullet;
import com.mygdx.game.model.Player;
import com.mygdx.game.util.Randomize;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class TankClientScreen extends ScreenAdapter {
    private final Controls localControls;
    private final Client client;
    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;
    private final PlayersContainer<Player> playersContainer;
    private final Container<Bullet> bulletsContainer;
    private final ContainerRenderer<Player> playersRenderer;
    private final ContainerRenderer<Bullet> bulletsRenderer;
    private Player localPlayer;

    public TankClientScreen(
            Controls localControls, Client client,
            Viewport viewport, ShapeRenderer shapeRenderer,
            PlayersContainer<Player> playersContainer, Container<Bullet> bulletsContainer,
            ContainerRenderer<Player> playersRenderer, ContainerRenderer<Bullet> bulletsRenderer) {
        this.localControls = localControls;
        this.client = client;
        this.viewport = viewport;
        this.playersContainer = playersContainer;
        this.bulletsContainer = bulletsContainer;
        this.shapeRenderer = shapeRenderer;
        this.playersRenderer = playersRenderer;
        this.bulletsRenderer = bulletsRenderer;
    }

    @Override
    public void show() {
        client.onConnected(introductoryStateDto -> {
            localPlayer = PlayerMapper.localPlayerFromDto(introductoryStateDto.getConnected(), new NoopControls());
            playersContainer.add(localPlayer);
            GameStateDto gameStateDto = introductoryStateDto.getGameState();
            gameStateDto.getPlayers().stream()
                    .map(playerDto -> PlayerMapper.localPlayerFromDto(playerDto, new NoopControls()))
                    .forEach(playersContainer::add);

            gameStateDto.getBullets().stream()
                    .map(bulletDto -> BulletMapper.fromDto(bulletDto, playersContainer))
                    .forEach(bulletsContainer::add);
        });

        client.onOtherPlayerConnected(connectedDto -> {
            Player connected = PlayerMapper.localPlayerFromDto(connectedDto, new NoopControls());
            playersContainer.add(connected);
        });

        client.onOtherPlayerDisconnected(uuidDto -> playersContainer.removeById(uuidDto.getUuid()));

        client.onGameStateReceived(gameStateDto -> {
            gameStateDto.getPlayers().stream()
                    .forEach(playerDto -> playersContainer
                            .getById(playerDto.getId())
                            .ifPresent(player -> PlayerMapper.updateByDto(player, playerDto)));

            gameStateDto.getBullets().stream()
                    .forEach(bulletDto -> {
                        Optional<Bullet> bullet = bulletsContainer.getById(bulletDto.getId());
                        if(!bullet.isPresent()) {
                            bulletsContainer.add(BulletMapper.fromDto(bulletDto, playersContainer));
                        } else {
                            BulletMapper.updateByDto(bullet.get(), bulletDto);
                        }
                    });

            List<String> existingBulletIds = gameStateDto.getBullets().stream()
                    .map(BulletDto::getId)
                    .collect(toList());

            bulletsContainer.stream()
                    .map(Bullet::getId)
                    .map(Object::toString)
                    .filter(id -> !existingBulletIds.contains(id))
                    .collect(toList())
                    .forEach(bulletsContainer::removeById);
        });

        client.connect(new PlayerDto(null, Randomize.fromList(Player.POSSIBLE_COLORS).toString(), null));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(!client.isConnected()) return;

        client.sendControls(ControlsMapper.mapToDto(localControls));

        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        playersRenderer.render(shapeRenderer);
        bulletsRenderer.render(shapeRenderer);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}