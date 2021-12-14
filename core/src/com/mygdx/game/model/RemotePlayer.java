package com.mygdx.game.model;

import com.mygdx.game.controls.RemoteControls;

import java.util.UUID;

public class RemotePlayer extends Player {
    private final RemoteControls remoteControls;
    public RemotePlayer(UUID id, RemoteControls remoteControls) {
        super(id, remoteControls);
        this.remoteControls = remoteControls;
    }
    public RemoteControls getRemoteControls() {
        return remoteControls;
    }
}