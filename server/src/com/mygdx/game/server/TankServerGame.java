package com.mygdx.game.server;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.mygdx.game.container.BarricadeContainer;
import com.mygdx.game.container.BotContainer;
import com.mygdx.game.container.BulletsContainer;
import com.mygdx.game.container.PlayersContainer;
import com.mygdx.game.manager.Collider;
import com.mygdx.game.manager.Respawner;
import com.mygdx.game.model.Arena;
import com.mygdx.game.model.RemotePlayer;
import com.mygdx.game.server.connection.Server;
import com.mygdx.game.server.connection.SocketIoServer;

import java.util.Map;

public class TankServerGame extends Game {
    private Screen tanks;



    @Override
    public void create() {
        Arena arena = new Arena();
        BulletsContainer bulletsContainer = new BulletsContainer();
        PlayersContainer<RemotePlayer> playersContainer = new PlayersContainer<>();
        BarricadeContainer barricadeContainer = new BarricadeContainer();
        BotContainer botContainer = new BotContainer();
        Respawner respawnerPlayer = new Respawner<>(playersContainer, 640, 640, arena);
        Respawner respawnerBot = new Respawner<>(botContainer, 640, 640, arena);
        Collider collider = new Collider<>(playersContainer, botContainer, bulletsContainer, barricadeContainer);

        Map<String, String> env = System.getenv();
        String host = env.getOrDefault("HOST", "localhost");
        int port = Integer.parseInt(env.getOrDefault("PORT", "8080"));
        Server server = new SocketIoServer(host, port);

        tanks = new TankServerScreen(
                server,
                playersContainer, bulletsContainer, barricadeContainer, botContainer,
                arena, respawnerPlayer, respawnerBot, collider);

        setScreen(tanks);
    }


    @Override
    public void dispose() {
        tanks.dispose();
    }
}
