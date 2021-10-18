package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.controls.RemoteControls;

import java.util.UUID;

public class RemotePlayer extends Player {
    private final RemoteControls remoteControls;

    public RemotePlayer(UUID id, RemoteControls remoteControls, Color color) {
        super(id, remoteControls, color);
        this.remoteControls = remoteControls;
    }

    public RemoteControls getRemoteControls() {
        return remoteControls;
    }
}
