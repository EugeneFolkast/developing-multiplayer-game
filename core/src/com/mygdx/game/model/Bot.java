package com.mygdx.game.model;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.container.Container;
import com.mygdx.game.controls.Controls;
import com.mygdx.game.controls.RemoteControls;
import com.mygdx.game.util.Vectors;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Bot extends Player{
    private final RemoteControls controls;
    private static final float RANGE = 300f;
    private float remainingRange;
    private final Container<RemotePlayer> playersContainer;
    private static final float[] VERTICES = new float[] {
            0, 0,
            8, 0,
            8, 8,
            0, 8
    };

    public Bot(UUID id, Container<RemotePlayer> playersContainer) {
        super(id, null);
        this.playersContainer = playersContainer;
        this.controls = new RemoteControls();
        this.remainingRange = RANGE;
    }

    private void see(float delta){
        Optional<Tank> tank = super.getTank();

        if(Objects.equals(tank, Optional.empty()))
            return;

        Polygon shape = tank.get().getShape();
        Polygon shapeSee = new Polygon(VERTICES);
        shapeSee.setPosition(shape.getX(), shape.getY());
        shapeSee.setRotation(shape.getRotation());
        shape.setOrigin(0, -Tank.getMiddle().y);

        while (this.remainingRange>0) {

            Vector2 direction = Vectors.getDirectionVector(shapeSee.getRotation());
            Vector2 movement = new Vector2(direction.x * delta * 100, direction.y * delta * 100);
            this.remainingRange -= (0.01);
            shapeSee.translate(movement.x, movement.y);

            Optional res = playersContainer.stream()
                    .filter(player -> player.getTank().isPresent())
                    .filter(player -> player.getTank().get().collidesWith(shapeSee))
                    .findFirst();

            if(!res.equals(Optional.empty())) {
                this.controls.setShoot(true);
                return;
            }
            this.controls.setShoot(false);



        }
        this.remainingRange = RANGE;

    }

    @Override
    public void update(float delta) {
        see(delta);

        super.getTank().ifPresent(tank -> {
            tank.control(this.controls, delta);
            tank.update(delta);
        });
    }
}
