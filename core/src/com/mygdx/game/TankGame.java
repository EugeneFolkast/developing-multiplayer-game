package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.container.BulletsContainer;
import com.mygdx.game.container.Container;
import com.mygdx.game.container.PlayersContainer;
import com.mygdx.game.controls.KeyboardControls;
import com.mygdx.game.controls.NoopControls;
import com.mygdx.game.manager.Collider;
import com.mygdx.game.manager.Respawner;
import com.mygdx.game.model.Arena;
import com.mygdx.game.model.Bullet;
import com.mygdx.game.model.Player;
import com.mygdx.game.rendering.ContainerRenderer;
import com.mygdx.game.rendering.PlayerRenderer;
import com.mygdx.game.rendering.VisibleRenderer;

import java.util.UUID;

public class TankGame extends Game {
    public static final float WORLD_WIDTH = 800f;
    public static final float WORLD_HEIGHT = 600f;
    private Screen asteroids;

    @Override
    public void create() {
        Viewport viewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGHT);
        ShapeRenderer shapeRenderer =  new ShapeRenderer();

        Arena arena = new Arena(WORLD_WIDTH, WORLD_HEIGHT);
        Player player1 = new Player(UUID.randomUUID(), new KeyboardControls(), Color.WHITE);
        Player player2 = new Player(UUID.randomUUID(), new NoopControls(), Color.LIGHT_GRAY);
        Container<Bullet> bulletsContainer = new BulletsContainer();
        PlayersContainer<Player> playersContainer = new PlayersContainer<>();
        playersContainer.add(player1);
        playersContainer.add(player2);
        Respawner respawner = new Respawner<>(playersContainer, WORLD_WIDTH, WORLD_HEIGHT);
        Collider collider = new Collider<>(playersContainer, bulletsContainer);

        ContainerRenderer<Bullet> bulletsRenderer = new ContainerRenderer<>(bulletsContainer, VisibleRenderer::new);
        ContainerRenderer<Player> playersRenderer = new ContainerRenderer<>(playersContainer, PlayerRenderer::new);

        asteroids = new TankScreen(
                viewport, shapeRenderer,
                playersContainer, bulletsContainer,
                arena, respawner, collider,
                playersRenderer, bulletsRenderer);

        setScreen(asteroids);
    }

    @Override
    public void dispose() {
        asteroids.dispose();
    }
}