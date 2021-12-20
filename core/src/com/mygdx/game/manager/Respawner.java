package com.mygdx.game.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.container.Container;
import com.mygdx.game.model.Arena;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.Tank;

import java.util.Random;

public class Respawner<PlayerType extends Player> {
    private static final Random random = new Random();
    private final Container<PlayerType> playersContainer;
    private final float widthBound;
    private final float heightBound;
    private final Arena arena;

    public Respawner(Container<PlayerType> playersContainer, float widthBound, float heightBound, Arena arena) {
        this.playersContainer = playersContainer;
        this.widthBound = widthBound;
        this.heightBound = heightBound;
        this.arena = new Arena();
    }

    public void respawn() {
        playersContainer.stream()
                .filter(player -> !player.getTank().isPresent())
                .forEach(this::respawnFor);
    }

    public void respawnFor(Player player) {
        Vector2 startCoord = randomRespawnPoint();
        player.setTank(new Tank(player,
                null, 200,
                (int)startCoord.x, (int)startCoord.y, 0));
    }

    private Vector2 randomRespawnPoint() {
        int x=0,y=0;
        do{
            x = random.nextInt(arena.getMapArray().length);
            y = random.nextInt(arena.getMapArray()[0].length);
        }while(arena.getMapArray()[x][y] == 0);

        return new Vector2(x,y);
    }
}
