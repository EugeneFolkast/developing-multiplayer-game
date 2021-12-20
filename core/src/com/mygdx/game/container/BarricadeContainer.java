package com.mygdx.game.container;

import com.mygdx.game.model.Barricade;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BarricadeContainer implements Container<Barricade> {
    private final List<Barricade> barricade;

    public BarricadeContainer(List<Barricade> barricade) {
        this.barricade = barricade;
    }

    public BarricadeContainer() {
        this(new ArrayList<>());
    }

    @Override
    public void add(Barricade bullet) {
        barricade.add(bullet);
    }

    public void removeByPlayerId(UUID id) {
        barricade.removeIf(barricade -> barricade.getId().equals(id));
    }

    @Override
    public List<Barricade> getAll() {
        return barricade;
    }

    @Override
    public void update(float delta) {
    }
}
