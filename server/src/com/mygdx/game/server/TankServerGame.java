package com.mygdx.game.server;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.mygdx.game.container.BulletsContainer;
import com.mygdx.game.container.PlayersContainer;
import com.mygdx.game.manager.Collider;
import com.mygdx.game.manager.Respawner;
import com.mygdx.game.model.Arena;
import com.mygdx.game.model.RemotePlayer;
import com.mygdx.game.server.connection.Server;
import com.mygdx.game.server.connection.SocketIoServer;

import java.util.Map;

import static com.mygdx.game.TankGame.WORLD_HEIGHT;
import static com.mygdx.game.TankGame.WORLD_WIDTH;


public class TankServerGame extends Game {
    private Screen tanks;

    @Override
    public void create() {
        Arena arena = new Arena(WORLD_WIDTH, WORLD_HEIGHT);
        BulletsContainer bulletsContainer = new BulletsContainer();
        PlayersContainer<RemotePlayer> playersContainer = new PlayersContainer<>();
        Respawner respawner = new Respawner<>(playersContainer, WORLD_WIDTH, WORLD_HEIGHT);
        Collider collider = new Collider<>(playersContainer, bulletsContainer);

        Map<String, String> env = System.getenv();
        String host = env.getOrDefault("HOST", "192.168.0.107");
        int port = Integer.parseInt(env.getOrDefault("PORT", "8888"));
        Server server = new SocketIoServer(host, port);

        tanks = new TankServerScreen(
                server,
                playersContainer, bulletsContainer,
                arena, respawner, collider);

        setScreen(tanks);
    }

    @Override
    public void dispose() {
        tanks.dispose();
    }
}
