package com.mygdx.game.desktop.connection;

import com.mygdx.game.dto.*;

import java.util.function.Consumer;

public interface Client {
    void connect(PlayerDto playerDto);
    void onConnected(Consumer<IntroductoryStateDto> handler);
    void onOtherPlayerConnected(Consumer<PlayerDto> handler);
    void onOtherPlayerDisconnected(Consumer<UuidDto> handler);
    void onGameStateReceived(Consumer<GameStateDto> handler);
    void sendControls(ControlsDto controlsDto);
    boolean isConnected();
}