package com.mygdx.game.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Round {
    public static float floatToInt(float value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Float.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}
