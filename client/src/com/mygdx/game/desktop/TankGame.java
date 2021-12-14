package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Map;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.model.Barricade;
import com.mygdx.game.model.GameMap;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.Shot;
import com.sun.security.ntlm.Client;

import java.util.UUID;

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
    private Player player;
    private Texture playerImage;
    private Texture barricadeImage;
    private Barricade barricade;
    private Array<Barricade> barricades;
    private Array<Shot> shots;

    @Override
    public void create(){
        Map<String, String> env = System.getenv();
        String protocol = env.getOrDefault("PROTOCOL", "http");
        String host = env.getOrDefault("HOST", "91.238.231.85");
        int port = Integer.parseInt(env.getOrDefault("PORT", "8888"));
        Client client = new SocketIoClient(protocol, host, port);

        mapArray = new int[][]{ {0,1,2,3,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
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

//        playerRec = new Rectangle();
//        playerRec.x = 0;
//        playerRec.y = 0;
//        playerRec.width = 64;
//        playerRec.height = 64;

        barricades = new Array<Barricade>();

        shots = new Array<Shot>();

        battleMusic1.setLooping(true);
//        battleMusic1.play();
    }

//    public void shooted(){
//        shotSound.stop();
//        shotSound.play();
//        if(playerGunDirection == "forward") batch.draw(fire = new Texture("fire.png"), playerRec.x, playerRec.y+64);
//        else{
//            if (playerGunDirection == "left") batch.draw(fire = new Texture("fireLeft.png"), playerRec.x-64, playerRec.y);
//            else{
//                if (playerGunDirection == "right") batch.draw(fire = new Texture("fireRight.png"), playerRec.x+64, playerRec.y);
//                else batch.draw(fire = new Texture("fireDown.png"), playerRec.x, playerRec.y-64);
//            }
//        }
//    }
    public void changeBarricadeStat(Integer type, Integer xCoor, Integer yCoor, String picName){
        barricadeImage = new Texture(Gdx.files.internal(picName));
        barricade.setBarricadeImage(barricadeImage);
        barricade.setTypeOfDestructiveness(type);
        barricade.setxCoor(xCoor);
        barricade.setyCoor(yCoor);
        barricades.add(barricade);
        batch.draw(barricade.getBarricadeImage(), barricade.getxCoor(), barricade.getyCoor());
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

        ScreenUtils.clear(1, 1, 1, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(map.getMapImage(), map.getxCoor(), map.getyCoor());

        for (int i = 0; i < map.getMapArray().length; i++){
            for (int j = 0; j < map.getMapArray()[i].length; j++){
//                batch.draw(player.getPlayerImage(), player.getxCoordinate(), player.getyCoordinate());

                displaceBarricade(map.getMapArray()[i][j], j * 64, i * 64);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)){
            System.out.print("forward\n");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)){
            System.out.print("left\n");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)){
            System.out.print("right\n");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)){
            System.out.print("back\n");
        }
        batch.end();
    }

    @Override
    public void dispose() {
        battleMusic1.dispose();
        shotSound.dispose();
        fire.dispose();
        batch.dispose();
    }
}
