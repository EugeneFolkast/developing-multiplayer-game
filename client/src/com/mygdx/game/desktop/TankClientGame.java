package com.mygdx.game.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.container.BulletsContainer;
import com.mygdx.game.container.Container;
import com.mygdx.game.container.PlayersContainer;
import com.mygdx.game.controls.Controls;
import com.mygdx.game.desktop.connection.Client;
import com.mygdx.game.desktop.connection.SocketIoClient;
import com.mygdx.game.desktop.controls.KeyboardControls;
import com.mygdx.game.desktop.rendering.ContainerRenderer;
import com.mygdx.game.desktop.rendering.PlayerRenderer;
import com.mygdx.game.desktop.rendering.VisibleRenderer;
import com.mygdx.game.model.Bullet;
import com.mygdx.game.model.Player;

import java.util.Map;

import static com.mygdx.game.TankGame.WORLD_HEIGHT;
import static com.mygdx.game.TankGame.WORLD_WIDTH;

public class TankClientGame extends Game {
    private Screen asteroids;

    @Override
    public void create() {
        Viewport viewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGHT);
        ShapeRenderer shapeRenderer =  new ShapeRenderer();
        Controls localControls = new KeyboardControls();

        Container<Bullet> bulletsContainer = new BulletsContainer();
        PlayersContainer<Player> playersContainer = new PlayersContainer<>();

        ContainerRenderer<Bullet> bulletsRenderer = new ContainerRenderer<>(bulletsContainer, VisibleRenderer::new);
        ContainerRenderer<Player> playersRenderer = new ContainerRenderer<>(playersContainer, PlayerRenderer::new);

        Map<String, String> env = System.getenv();
        String protocol = env.getOrDefault("PROTOCOL", "http");
        String host = env.getOrDefault("HOST", "91.238.231.85");
        int port = Integer.parseInt(env.getOrDefault("PORT", "8888"));
        Client client = new SocketIoClient(protocol, host, port);

        asteroids = new TankClientScreen(
                localControls, client,
                viewport, shapeRenderer,
                playersContainer, bulletsContainer,
                playersRenderer, bulletsRenderer);

        setScreen(asteroids);
    }

    @Override
    public void dispose() {
        asteroids.dispose();
    }
}