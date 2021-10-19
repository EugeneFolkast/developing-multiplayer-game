package com.mygdx.game.desktop.rendering;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.Tank;

import java.util.Map;
import java.util.WeakHashMap;


public class PlayerRenderer implements Renderer {
    private final Map<Tank, Renderer> cache;
    private final Player player;

    public PlayerRenderer(Player player, Map<Tank, Renderer> cache) {
        this.player = player;
        this.cache = cache;
    }

    public PlayerRenderer(Player player) {
        this(player, new WeakHashMap<>());
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        player.getShip().ifPresent(tank ->
                cache
                        .computeIfAbsent(tank, VisibleRenderer::new)
                        .render(shapeRenderer));
    }
}