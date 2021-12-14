package com.mygdx.game.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.container.Container;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.Tank;

import java.util.Random;

public class Respawner<PlayerType extends Player> {
    private static final Random random = new Random();
    private final Container<PlayerType> playersContainer;
    private final float widthBound;
    private final float heightBound;

    public Respawner(Container<PlayerType> playersContainer, float widthBound, float heightBound) {
        this.playersContainer = playersContainer;
        this.widthBound = widthBound;
        this.heightBound = heightBound;
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
                (int)startCoord.x, (int)startCoord.y, "forward"));
    }

    private Vector2 randomRespawnPoint() {
//        return new Vector2(random.nextInt(8), random.nextInt(8));
        return new Vector2(2, 2);
    }
}
