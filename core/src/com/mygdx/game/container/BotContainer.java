package com.mygdx.game.container;

import com.mygdx.game.model.Bot;
import com.mygdx.game.model.Bullet;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.Tank;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class BotContainer <BotType extends Bot> implements Container <BotType>{
    private final List<BotType> bots;

    public BotContainer(List<BotType> players) {
        this.bots = players;
    }

    public BotContainer() {
        this(new ArrayList<>());
    }

    @Override
    public void add(BotType toAdd) {
        bots.add(toAdd);
    }

    @Override
    public List<BotType> getAll() {
        return bots;
    }

    @Override
    public void update(float delta) {
        bots.forEach((bot -> bot.update(delta)));
    }

    public Stream<Tank> streamTank() {
        return stream()
                .map(Bot::getTank)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public Stream<Bullet> obtainAndStreamBullets() {
        return streamTank()
                .map(Tank::obtainBullet)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }
}
