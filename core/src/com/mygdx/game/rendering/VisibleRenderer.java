package com.mygdx.game.rendering;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.model.Visible;

public class VisibleRenderer  implements Renderer {
    private final Visible visible;

    public VisibleRenderer(Visible visible) {
        this.visible = visible;
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(visible.getColor());
        shapeRenderer.polygon(visible.getShape().getTransformedVertices());
    }
}