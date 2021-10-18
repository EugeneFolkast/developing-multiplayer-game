package com.mygdx.game.rendering;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.container.Container;
import com.mygdx.game.model.Identifiable;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

public class ContainerRenderer<Thing extends Identifiable> implements Renderer {
    private final Container<Thing> container;
    private final Function<Thing, Renderer> rendererFactory;
    private final Map<Thing, Renderer> cache;

    public ContainerRenderer(Container<Thing> container,
                             Function<Thing, Renderer> rendererFactory, Map<Thing, Renderer> cache) {
        this.container = container;
        this.rendererFactory = rendererFactory;
        this.cache = cache;
    }

    public ContainerRenderer(Container<Thing> container, Function<Thing, Renderer> rendererFactory) {
        this(container, rendererFactory, new WeakHashMap<>());
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        container.getAll().forEach(thing ->
                cache
                        .computeIfAbsent(thing, rendererFactory)
                        .render(shapeRenderer));
    }
}