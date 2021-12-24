package com.mygdx.game.model;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.container.Container;
import com.mygdx.game.controls.RemoteControls;
import com.mygdx.game.util.Vectors;
import com.mygdx.game.util.maze.Position;
import com.mygdx.game.util.maze.SearchMaze;

import java.text.MessageFormat;
import java.util.*;

import static com.badlogic.gdx.math.MathUtils.random;

public class Bot extends Player{
    private final RemoteControls controls;
    private static final float RANGE = 300f;
    private float remainingRange;
    private final Container<RemotePlayer> playersContainer;
    private final Polygon shapeSee;
    private final ArrayList<Polygon> leftSee;
    private final ArrayList<Polygon> rightSee;
    private final Arena map;
    private int goToX =0;
    private int goToY =0;
    private Stack<Position> path;
    private int goToIndex = 0;


    private static final float[] VERTICES = new float[] {
            0, 0,
            8, 0,
            8, 8,
            0, 8
    };

    public Bot(UUID id, Container<RemotePlayer> playersContainer, Arena map) {
        super(id, null);
        this.playersContainer = playersContainer;
        this.map = map;
        this.controls = new RemoteControls();
        this.remainingRange = RANGE;

        this.shapeSee = new Polygon(VERTICES);
        this.leftSee = new ArrayList<>();
        this.rightSee = new ArrayList<>();

        path = new Stack<>();

        for( int i=0; i<9; i+= 1){
            Polygon vectorLeft = new Polygon(VERTICES);
            Polygon vectorRight = new Polygon(VERTICES);

            this.leftSee.add(vectorLeft);
            this. rightSee.add(vectorRight);
        }

    }

    public void clearPath() {
        this.path.clear();
    }

    private void updateGoTo(){
        do{
            goToX = random.nextInt(map.getMapArray().length)*64;
            goToY = random.nextInt(map.getMapArray()[0].length)*64;
        }while(map.getMapArray()[(int)(goToY/64)][(int)(goToX/64)] != 0);

    }

    private void see(float delta){
        Optional<Tank> tank = super.getTank();

        if(Objects.equals(tank, Optional.empty()))
            return;

        Polygon shape = tank.get().getShape();
        this.shapeSee.setPosition(shape.getX(), shape.getY());
        this.shapeSee.setRotation(shape.getRotation());
        shape.setOrigin(0, -Tank.getMiddle().y);

        if ((Math.round(shape.getX()) - goToX < 64 && Math.round(shape.getY()) - goToY < 64
                &&
                Math.round(shape.getX()) - goToX >=0  && Math.round(shape.getY()) - goToY >=0 )
                || path.empty() || path.size() == goToIndex) {
            updateGoTo();
            goToIndex = 0;
            SearchMaze maze = new SearchMaze(map.getMapArray(), Math.round(shape.getX()/64), Math.round(shape.getY()/64),
                    goToX/64, goToY/64);
            try {
                this.path = maze.startSearch(Math.round(shape.getX()/64), Math.round(shape.getY()/64));
                Position res = path.get(goToIndex);

                goToX = res.getPx()*64;
                goToY = res.getPy()*64;
            }catch (EmptyStackException ex)
            {
                path.clear();
                return;
            }

        }


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
            this.controls.setForward(false);

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
                return;
            }
        }
        move(Math.round(shape.getX()), Math.round(shape.getY()), Math.round(shape.getRotation()));

        this.remainingRange = RANGE;
    }

    private void move(int thisX, int thisY, float rotation) {
        System.out.println(MessageFormat.format("thisX {0} thisY {1} \ngoToX {2}, goToY {3} \nrotation {4}",
                thisX, thisY, goToX, goToY, rotation));

        if (thisX - goToX < 64 && thisY - goToY < 64
            && thisX - goToX > 0 && thisY - goToY > 0){
            goToIndex++;
            if (path.size() <= goToIndex)
                return;

            Position res = path.get(goToIndex);

            goToX = res.getPx()*64;
            goToY = res.getPy()*64;
            return;
        }

        if (thisX > goToX) {
            if (rotation > 90) {
                this.controls.setBack(true);
                return;
            }
            if (rotation < 90 && rotation > 0) {
                this.controls.setRight(true);
                return;
            }

            if (rotation < -270) {
                this.controls.setBack(true);
                return;
            }
            if (rotation > -270 && rotation <= 0) {
                this.controls.setRight(true);
                return;
            }

            this.controls.setForward(true);

        }
        if (thisX < goToX) {
            if (rotation > 270) {
                this.controls.setRight(true);
                return;
            }
            if (rotation < 270 && rotation > 0) {
                this.controls.setBack(true);
                return;
            }

            if (rotation < -90) {
                this.controls.setRight(true);
                return;
            }
            if (rotation > -90 && rotation <= 0) {
                this.controls.setBack(true);
                return;
            }

            this.controls.setForward(true);

        }
        if (thisY < goToY) {
            if (rotation > 0 && rotation < 180) {
                this.controls.setRight(true);
                return;
            }
            if (rotation > 180) {
                this.controls.setBack(true);
                return;
            }

            if (rotation < -180) {
                this.controls.setRight(true);
                return;
            }
            if (rotation > -180 && rotation < 0) {
                this.controls.setBack(true);
                return;
            }

            this.controls.setForward(true);

        }
        if (thisY > goToY) {
            if (rotation > 180) {
                this.controls.setRight(true);
                return;
            }
            if (rotation < 180 && rotation > 0) {
                this.controls.setBack(true);
                return;
            }

            if (rotation < -180) {
                this.controls.setBack(true);
                return;
            }
            if (rotation > -180 && rotation < 0) {
                this.controls.setRight(true);
                return;
            }

            this.controls.setForward(true);

        }
        this.controls.setForward(true);
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
