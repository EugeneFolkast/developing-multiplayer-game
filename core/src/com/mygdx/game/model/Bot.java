package com.mygdx.game.model;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.container.Container;
import com.mygdx.game.controls.Controls;
import com.mygdx.game.controls.RemoteControls;
import com.mygdx.game.util.Vectors;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;

public class Bot extends Player{
    private final RemoteControls controls;
    private static final float RANGE = 300f;
    private float remainingRange;
    private final Container<RemotePlayer> playersContainer;
    private final Polygon shapeSee;
    private final ArrayList<Polygon> leftSee;
    private final ArrayList<Polygon> rightSee;

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

        this.shapeSee = new Polygon(VERTICES);
        this.leftSee = new ArrayList<>();
        this.rightSee = new ArrayList<>();

        for( int i=0; i<9; i+= 1){
            Polygon vectorLeft = new Polygon(VERTICES);
            Polygon vectorRight = new Polygon(VERTICES);

            this.leftSee.add(vectorLeft);
            this. rightSee.add(vectorRight);
        }

    }

    private void see(float delta){
        Optional<Tank> tank = super.getTank();

        if(Objects.equals(tank, Optional.empty()))
            return;

        Polygon shape = tank.get().getShape();
        this.shapeSee.setPosition(shape.getX(), shape.getY());
        this.shapeSee.setRotation(shape.getRotation());
        shape.setOrigin(0, -Tank.getMiddle().y);

        for(int i=0; i<9; i+= 1){
            this.leftSee.get(i).setPosition(shape.getX(), shape.getY());
            this.rightSee.get(i).setPosition(shape.getX(), shape.getY());

            this.leftSee.get(i).setRotation(shape.getRotation()+(i*10));
            this.rightSee.get(i).setRotation(shape.getRotation()-(i*10));
        }

        while (this.remainingRange>0) {
            this.controls.setBack(false);
            this.controls.setRight(false);
            this.controls.setShoot(false);


            this.remainingRange -= (1);

            if (rotateLeft(shape, delta)) {
                this.controls.setBack(true);
                return;
            }
            if (rotateRight(shape, delta)) {
                this.controls.setRight(true);
                return;
            }
            if (shoot(delta)){
                this.controls.setShoot(true);
            }

        }
        this.remainingRange = RANGE;
    }

    private boolean shoot(float delta){
        Vector2 direction = Vectors.getDirectionVector(shapeSee.getRotation());
        Vector2 movement = new Vector2(direction.x * delta * 100, direction.y * delta * 100);
        shapeSee.translate(movement.x, movement.y);

        Optional res = playersContainer.stream()
                .filter(player -> player.getTank().isPresent())
                .filter(player -> player.getTank().get().collidesWith(shapeSee))
                .findFirst();

        return !res.equals(Optional.empty());
    }

    private boolean rotateLeft(Polygon shape, float delta){
        Optional resLeft = Optional.empty();
        for(int i=0; i<9; i++) {
            Vector2 directionLeft = Vectors.getDirectionVector(shape.getRotation()-((i+1)*10)+5);
            Vector2 movementLeft = new Vector2(directionLeft.x * delta * 100, directionLeft.y * delta * 100);
            leftSee.get(i).translate(movementLeft.x, movementLeft.y);

            int finalI = i;
            resLeft = playersContainer.stream()
                    .filter(player -> player.getTank().isPresent())
                    .filter(player -> player.getTank().get().collidesWith(leftSee.get(finalI)))
                    .findFirst();

            if(!resLeft.equals(Optional.empty())) {
                return true;
            }
        }
        return false;
    }

    private boolean rotateRight(Polygon shape, float delta){
        Optional resRight = Optional.empty();
        for(int i=0; i<9; i++) {
            Vector2 directionRight = Vectors.getDirectionVector(shape.getRotation()+((i+1)*10)-5);
            Vector2 movementRight= new Vector2(directionRight.x * delta * 100, directionRight.y * delta * 100);
            rightSee.get(i).translate(movementRight.x, movementRight.y);

            int finalI = i;
            resRight = playersContainer.stream()
                    .filter(player -> player.getTank().isPresent())
                    .filter(player -> player.getTank().get().collidesWith(rightSee.get(finalI)))
                    .findFirst();

            if(!resRight.equals(Optional.empty())) {
                return true;
            }
        }
        return false;
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
