package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.container.Container;
import com.mygdx.game.container.PlayersContainer;
import com.mygdx.game.manager.Collider;
import com.mygdx.game.manager.Respawner;
import com.mygdx.game.model.Arena;
import com.mygdx.game.model.Bullet;
import com.mygdx.game.model.Player;
import com.mygdx.game.rendering.ContainerRenderer;

import java.util.Optional;
import java.util.stream.Stream;

public class TankScreen extends ScreenAdapter {
    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;
    private final Arena arena;
    private final PlayersContainer<Player> playersContainer;
    private final Container<Bullet> bulletsContainer;
    private final Respawner respawner;
    private final Collider collider;
    private final ContainerRenderer<Player> playersRenderer;
    private final ContainerRenderer<Bullet> bulletsRenderer;

    public TankScreen(
            Viewport viewport, ShapeRenderer shapeRenderer,
            PlayersContainer<Player> playersContainer, Container<Bullet> bulletsContainer,
            Arena arena, Respawner respawner, Collider collider,
            ContainerRenderer<Player> playersRenderer, ContainerRenderer<Bullet> bulletsRenderer)
    {
        this.viewport = viewport;
        this.shapeRenderer = shapeRenderer;
        this.arena = arena;
        this.respawner = respawner;
        this.collider = collider;
        this.playersContainer = playersContainer;
        this.bulletsContainer = bulletsContainer;
        this.playersRenderer = playersRenderer;
        this.bulletsRenderer = bulletsRenderer;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
