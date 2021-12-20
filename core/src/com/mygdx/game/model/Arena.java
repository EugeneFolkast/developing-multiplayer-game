package com.mygdx.game.model;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.container.BarricadeContainer;
import com.mygdx.game.container.Container;

public class Arena {
    private int mapArray[][];
    private final Rectangle bounds;

    public Arena() {
        bounds = new Rectangle(0, 0, 640, 640);
        mapArray = new int[][]{ {3,3,3,3,3,3,3,3,3,3},
                                {3,0,0,0,2,0,0,0,0,3},
                                {3,0,0,0,2,0,0,0,0,3},
                                {3,0,1,1,2,1,1,1,0,3},
                                {3,0,0,0,0,0,0,0,0,3},
                                {3,0,0,0,0,0,0,0,0,3},
                                {3,0,1,1,2,0,1,1,0,3},
                                {3,0,0,0,2,0,0,0,0,3},
                                {3,0,0,0,2,0,0,0,0,3},
                                {3,3,3,3,3,3,3,3,3,3}, };
    }

    public int[][] getMapArray() {
        return mapArray;
    }

    public void ensurePlacementWithinBounds(Visible visible, BarricadeContainer barricadeContainer) {
        Polygon shape = visible.getShape();
        Rectangle shapeBounds = shape.getBoundingRectangle();

        barricadeContainer.stream()
                .filter(barricade -> barricade.collidesWith(visible))
                .findFirst()
                .ifPresent(
                        barricade -> {
                            float xx = visible.getShape().getX();
                            float yy = visible.getShape().getY();

                            Polygon bShape = barricade.getShape();
                            Rectangle bShapeBounds = bShape.getBoundingRectangle();
                            float bx = bShape.getX();
                            float by = bShape.getY();

                            if(xx < bx+bShapeBounds.width) xx = bx+bShapeBounds.width;
                            else if(yy < by + bShapeBounds.height) yy = by + bShapeBounds.height;
                            else if(xx + shapeBounds.width > bx) xx = bx - bShapeBounds.width;
                            else if(yy + shapeBounds.height > by) yy = by - bShapeBounds.height;
                            shape.setPosition(xx, yy);
                        }
                );

        float x = shape.getX();
        float y = shape.getY();

        if(x + shapeBounds.width < bounds.x) x = bounds.width - 32;
        if(y + shapeBounds.height < bounds.y) y = bounds.height - 32;
        if(x > bounds.width) x = bounds.x - shapeBounds.width + 32;
        if(y > bounds.height) y = bounds.y - shapeBounds.height + 32;
        shape.setPosition(x, y);



    }

}