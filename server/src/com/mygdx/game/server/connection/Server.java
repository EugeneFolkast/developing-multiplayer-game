package com.mygdx.game.server.connection;

import com.mygdx.game.dto.ControlsDto;
import com.mygdx.game.dto.GameStateDto;
import com.mygdx.game.dto.PlayerDto;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface  Server {
    void start();
    void onPlayerConnected(Consumer<PlayerDto> handler);
    void onPlayerDisconnected(Consumer<UUID> handler);
    void onPlayerSentControls(BiConsumer<UUID, ControlsDto> handler);
    void broadcast(GameStateDto gameState);
    void sendIntroductoryDataToConnected(PlayerDto connected, GameStateDto gameState);
    void notifyOtherPlayersAboutConnected(PlayerDto connected);
}