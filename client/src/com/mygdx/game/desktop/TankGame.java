package com.mygdx.game.desktop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.container.BulletsContainer;
import com.mygdx.game.container.Container;
import com.mygdx.game.container.PlayersContainer;
import com.mygdx.game.controls.Controls;
import com.mygdx.game.controls.NoopControls;
import com.mygdx.game.desktop.connection.Client;
import com.mygdx.game.desktop.connection.SocketIoClient;
import com.mygdx.game.desktop.controls.KeyboardControls;
import com.mygdx.game.dto.BulletDto;
import com.mygdx.game.dto.GameStateDto;
import com.mygdx.game.dto.PlayerDto;
import com.mygdx.game.dto.TankDto;
import com.mygdx.game.dto.mapper.BulletMapper;
import com.mygdx.game.dto.mapper.ControlsMapper;
import com.mygdx.game.dto.mapper.PlayerMapper;
import com.mygdx.game.model.*;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class TankGame extends ApplicationAdapter {
    private Texture fire;
    private Texture mapToExport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Rectangle playerRec;
    private String playerGunDirection;
    private Sound shotSound;
    private Music battleMusic1;
    private int mapArray[][];
    private GameMap map;
    private Player localPlayer;
    private Texture playerImage;
    private Texture barricadeImage;
    private Texture bulletImage;
    private Barricade barricade;
    private Array<Barricade> barricades;
    private Array<Bullet> shots;
    private Client client;
    private PlayersContainer<Player> playersContainer;
    private Container<Bullet> bulletsContainer;
    private Sprite barricadeSprite;
    private Sprite playerSprite;
    private Sprite bulletSprite;

    Controls localControls;

    @Override
    public void create(){
        bulletsContainer = new BulletsContainer();
        playersContainer = new PlayersContainer<>();
        localControls = new KeyboardControls();

        Map<String, String> env = System.getenv();
        String protocol = env.getOrDefault("PROTOCOL", "http");
        String host = env.getOrDefault("HOST", "localhost");
        int port = Integer.parseInt(env.getOrDefault("PORT", "8080"));
        client = new SocketIoClient(protocol, host, port);
        show();

        mapArray = new int[][]{ {3,3,3,3,3,3,3,3,3,3},
                                {3,0,0,0,2,0,0,0,0,3},
                                {3,0,0,0,2,0,0,0,0,3},
                                {3,0,1,1,2,1,1,1,0,3},
                                {3,0,0,0,1,0,0,0,0,3},
                                {3,0,0,0,1,0,0,0,0,3},
                                {3,0,1,1,2,1,1,1,0,3},
                                {3,0,0,0,2,0,0,0,0,3},
                                {3,0,0,0,2,0,0,0,0,3},
                                {3,3,3,3,3,3,3,3,3,3},
        };
        barricadeImage = new Texture(Gdx.files.internal("box.png"));
        barricade = new Barricade(barricadeImage, 0, 0, 2, UUID.randomUUID());

        mapToExport = new Texture(Gdx.files.internal("mapEmpty.png"));
        map = new GameMap(mapToExport, 0, 0, mapArray);

        battleMusic1 = Gdx.audio.newMusic(Gdx.files.internal("redalert.mp3"));
        shotSound = Gdx.audio.newSound(Gdx.files.internal("pushka.mp3"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 640, 640);

        batch = new SpriteBatch();
        barricades = new Array<Barricade>();
        shots = new Array<Bullet>();
        battleMusic1.setLooping(true);
    }

    public void show() {
        client.onConnected(introductoryStateDto -> {
            localPlayer = PlayerMapper.localPlayerFromDto(introductoryStateDto.getConnected(), new NoopControls());

            playersContainer.add(localPlayer);
            GameStateDto gameStateDto = introductoryStateDto.getGameState();
            gameStateDto.getPlayers().stream()
                    .map(playerDto -> PlayerMapper.localPlayerFromDto(playerDto, new NoopControls()))
                    .forEach(playersContainer::add);

            gameStateDto.getBullets().stream()
                    .map(bulletDto -> BulletMapper.fromDto(bulletDto, playersContainer))
                    .forEach(bulletsContainer::add);

            this.map.setMapArray(gameStateDto.getArena());
        });

        client.onOtherPlayerConnected(connectedDto -> {
            Player connected = PlayerMapper.localPlayerFromDto(connectedDto, new NoopControls());
            connected.getTank().get().setPlayerImage("enemy.png");
            playersContainer.add(connected);

        });

        client.onOtherPlayerDisconnected(uuidDto -> {
            playersContainer.removeById(uuidDto.getUuid());
        });

        client.onGameStateReceived(gameStateDto -> {
            gameStateDto.getPlayers().stream()
                    .forEach(playerDto -> playersContainer
                            .getById(playerDto.getId())
                            .ifPresent(player -> PlayerMapper.updateByDto(player, playerDto)));

            gameStateDto.getBullets().stream()
                    .forEach(bulletDto -> {
                        Optional<Bullet> bullet = bulletsContainer.getById(bulletDto.getId());
                        if(!bullet.isPresent()) {
                            bulletsContainer.add(BulletMapper.fromDto(bulletDto, playersContainer));
                        } else {
                            BulletMapper.updateByDto(bullet.get(), bulletDto);
                        }
                    });

            List<String> existingBulletIds = gameStateDto.getBullets().stream()
                    .map(BulletDto::getId)
                    .collect(toList());

            bulletsContainer.stream()
                    .map(Bullet::getId)
                    .map(Object::toString)
                    .filter(id -> !existingBulletIds.contains(id))
                    .collect(toList())
                    .forEach(bulletsContainer::removeById);

            this.map.setMapArray(gameStateDto.getArena());

        });

        client.connect(new PlayerDto(null, null));
    }


    public void changeBarricadeStat(Integer type, Integer xCoor, Integer yCoor, String picName){
        barricadeImage = new Texture(Gdx.files.internal(picName));
        barricade.setBarricadeImage(barricadeImage);
        barricade.setTypeOfDestructiveness(type);
        barricade.setxCoor(xCoor);
        barricade.setyCoor(yCoor);
        barricades.add(barricade);
//        batch.draw(barricade.getBarricadeImage(), barricade.getxCoor(), barricade.getyCoor());
        barricadeSprite = new Sprite(barricadeImage, barricade.getxCoor(), barricade.getyCoor(), 64, 64);
        barricadeSprite.setPosition(barricade.getxCoor(), barricade.getyCoor());
        barricadeSprite.draw(batch);
    }

    public void displaceBarricade(Integer type, Integer xCoor, Integer yCoor){
        if(type != 0){
            if (type != 1){
                if (type != 2){
                    changeBarricadeStat(3, xCoor, yCoor, "unbreakablewall.png");
                }

                else {
                    changeBarricadeStat(2, xCoor, yCoor, "brakefromshotwall.png");
                }
            }
            else{
                changeBarricadeStat(1, xCoor, yCoor, "easytobreak.png");
            }
        }
    }

    @Override
    public void render(){
        if(!client.isConnected())
        {
            System.out.println("Disconnect!!!");
            return;
        }
        client.sendControls(ControlsMapper.mapToDto(localControls));

        ScreenUtils.clear(1, 1, 1, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(map.getMapImage(), map.getxCoor(), map.getyCoor());

        for (int i = 0; i < map.getMapArray().length; i++){
            for (int j = 0; j < map.getMapArray()[i].length; j++){
                displaceBarricade(map.getMapArray()[i][j], j * 64, i * 64);
            }
        }


        for (Player item: playersContainer.getAll()) {
            if(item.getTank().equals(Optional.empty())) {
                continue;
            }
            Tank tank = item.getTank().get();
            if(tank.getPlayerImage() == null)
                localPlayer.getTank().get().setPlayerImage("player.png");
            Vector2 tankPosition = tank.getPosition();
            System.out.println(tankPosition);

            Sprite playerSprite = new Sprite(new Texture(Gdx.files.internal(tank.getPlayerImage())));
            playerSprite.setOrigin(tankPosition.x + 16, tankPosition.y + 16);
            playerSprite.setOriginCenter();
            playerSprite.rotate (tank.getRotation());
            playerSprite.setPosition(tankPosition.x, tankPosition.y);
            playerSprite.setSize(32, 32);
            playerSprite.draw(batch);

            playerSprite.setPosition(tank.getxCoordinate()*64, tank.getyCoordinate()* 64);

            playerSprite.draw(batch);

//            batch.draw(new Texture(Gdx.files.internal(tank.getPlayerImage())),
//                    tank.getxCoordinate()* 64, tank.getyCoordinate()* 64);
        }
        for (Bullet item: bulletsContainer.getAll()) {
            Sprite bulletSprite = new Sprite(new Texture(Gdx.files.internal("shot.png")));
            bulletSprite.setOrigin(item.getPosition().x, item.getPosition().y);
            bulletSprite.setOriginCenter();
            bulletSprite.rotate (item.getRotation());
            bulletSprite.setPosition(item.getPosition().x, item.getPosition().y);
            bulletSprite.draw(batch);

            bulletImage = new Texture(Gdx.files.internal("shot.png"));

            bulletSprite = new Sprite(bulletImage,
                    (int)item.getxCoordinate()* 64,
                    (int)item.getyCoordinate()* 64,
                    64,
                    64);

            bulletSprite.setPosition(item.getxCoordinate()* 64, item.getyCoordinate()* 64);

            bulletSprite.draw(batch);
//            batch.draw(new Texture(Gdx.files.internal("shot.png")),
//                    item.getxCoordinate()* 64, item.getyCoordinate()* 64);

        }



        batch.end();
    }

    @Override
    public void dispose() {
        battleMusic1.dispose();
        shotSound.dispose();
        batch.dispose();
    }
}
