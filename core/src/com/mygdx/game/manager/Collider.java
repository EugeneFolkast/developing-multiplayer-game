package com.mygdx.game.manager;

import com.mygdx.game.container.Container;
import com.mygdx.game.model.Bullet;
import com.mygdx.game.model.Player;

public class Collider <PlayerType extends Player> {
    private final Container<PlayerType> playersContainer;
    private final Container<Bullet> bulletsContainer;

    public Collider(Container<PlayerType> playersContainer, Container<Bullet> bulletsContainer) {
        this.playersContainer = playersContainer;
        this.bulletsContainer = bulletsContainer;
    }

    public void checkCollisions() {
        bulletsContainer.stream()
                .forEach(bullet -> playersContainer.stream()
                        .filter(player -> player.getShip().isPresent())
                        .filter(player -> player.getShip().get().collidesWith(bullet))
                        .findFirst()
                        .ifPresent(player -> {
                            player.noticeHit();
                            bullet.noticeHit();
                        }));
    }
}
