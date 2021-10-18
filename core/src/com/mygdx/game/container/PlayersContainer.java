package com.mygdx.game.container;

import com.mygdx.game.model.Bullet;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.Tank;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class PlayersContainer<PlayerType extends Player> implements Container<PlayerType> {
    private final List<PlayerType> players;

    public PlayersContainer(List<PlayerType> players) {
        this.players = players;
    }

    public PlayersContainer() {
        this(new ArrayList<>());
    }

    @Override
    public void add(PlayerType toAdd) {
        players.add(toAdd);
    }

    @Override
    public List<PlayerType> getAll() {
        return players;
    }

    @Override
    public void update(float delta) {
        players.forEach(player -> player.update(delta));
    }

    public Stream<Tank> streamShips() {
        return stream()
                .map(Player::getShip)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public Stream<Bullet> obtainAndStreamBullets() {
        return streamShips()
                .map(Tank::obtainBullet)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }
}