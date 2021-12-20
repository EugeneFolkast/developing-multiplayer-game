package com.mygdx.game.container;

import com.mygdx.game.model.Barricade;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BarricadeContainer implements Container<Barricade> {
    private static List<Barricade> barricade = null;


    public BarricadeContainer(List<Barricade> barricade) {
        BarricadeContainer.barricade = barricade;
    }


    public BarricadeContainer() {
        this(new ArrayList<>());
    }

    public static List<Barricade> getBarricades(){
        return barricade;
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
        barricade.removeIf(barricade1 -> barricade1.getTypeOfDestructiveness() == 0);
    }
}
