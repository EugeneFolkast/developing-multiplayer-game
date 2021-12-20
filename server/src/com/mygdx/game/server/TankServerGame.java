package com.mygdx.game.server;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.mygdx.game.container.BarricadeContainer;
import com.mygdx.game.container.BulletsContainer;
import com.mygdx.game.container.Container;
import com.mygdx.game.container.PlayersContainer;
import com.mygdx.game.dto.GameStateDto;
import com.mygdx.game.dto.PlayerDto;
import com.mygdx.game.dto.mapper.ControlsMapper;
import com.mygdx.game.dto.mapper.GameStateMapper;
import com.mygdx.game.dto.mapper.PlayerMapper;
import com.mygdx.game.manager.Collider;
import com.mygdx.game.manager.Respawner;
import com.mygdx.game.model.Arena;
import com.mygdx.game.model.Barricade;
import com.mygdx.game.model.RemotePlayer;
import com.mygdx.game.model.Tank;
import com.mygdx.game.server.connection.Server;
import com.mygdx.game.server.connection.SocketIoServer;

import java.util.List;
import java.util.Map;

public class TankServerGame extends Game {
    private Screen tanks;



    @Override
    public void create() {
        Arena arena = new Arena();
        BulletsContainer bulletsContainer = new BulletsContainer();
        PlayersContainer<RemotePlayer> playersContainer = new PlayersContainer<>();
        BarricadeContainer barricadeContainer = new BarricadeContainer();
        Respawner respawner = new Respawner<>(playersContainer, 640, 640);
        Collider collider = new Collider<>(playersContainer, bulletsContainer, barricadeContainer);

        Map<String, String> env = System.getenv();
        String host = env.getOrDefault("HOST", "localhost");
        int port = Integer.parseInt(env.getOrDefault("PORT", "8080"));
        Server server = new SocketIoServer(host, port);

        tanks = new TankServerScreen(
                server,
                playersContainer, bulletsContainer, barricadeContainer,
                arena, respawner, collider);

        setScreen(tanks);
    }


    @Override
    public void dispose() {
        tanks.dispose();
    }
}
