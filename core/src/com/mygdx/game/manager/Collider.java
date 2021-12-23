package com.mygdx.game.manager;

import com.mygdx.game.container.Container;
import com.mygdx.game.model.*;

import java.util.UUID;

public class Collider<PlayerType extends Player> {
    private final Container<Barricade> barricadeContainer;
    private final Container<PlayerType> playersContainer;
    private final Container<PlayerType> botsContainer;
    private final Container<Bullet> bulletsContainer;

    public Collider(Container<PlayerType> playersContainer, Container<PlayerType> botsContainer,
                    Container<Bullet> bulletsContainer, Container<Barricade> barricadeContainer) {
        this.playersContainer = playersContainer;
        this.botsContainer = botsContainer;
        this.bulletsContainer = bulletsContainer;
        this.barricadeContainer = barricadeContainer;
    }

    public void checkBulletCollisions() {
        bulletsContainer.stream()
                .forEach(bullet -> {
                    playersContainer.stream()
                            .filter(player -> player.getTank().isPresent())
                            .filter(player -> player.getTank().get().collidesWith(bullet))
                            .findFirst()
                            .ifPresent(player -> {
                                player.noticeHit();
                                bullet.noticeHit();
                            });

                    botsContainer.stream()
                            .filter(player -> player.getTank().isPresent())
                            .filter(player -> player.getTank().get().collidesWith(bullet))
                            .findFirst()
                            .ifPresent(player -> {
                                player.noticeHit();
                                bullet.noticeHit();
                            });

                    barricadeContainer.stream()
                            .filter(barricade -> barricade.collidesWith(bullet))
                            .findFirst()
                            .ifPresent(barricade -> {
                                bullet.noticeHit();
                                barricade.hitting();
                            });
                });
    }
}