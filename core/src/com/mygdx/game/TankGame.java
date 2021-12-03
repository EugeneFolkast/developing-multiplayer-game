package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;

import java.sql.Time;
import java.util.Iterator;
import java.util.Random;


public class TankGame extends ApplicationAdapter {
    private Texture mapImage;
    private Texture playerImage;
    private Texture shotImg;
    private Texture fire;
    private Texture immortalWall;
    private Texture easyWall;
    private Texture brakeMeWall;
    private Texture enemyImage;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Rectangle player;
    private Rectangle shot;
    private Rectangle enemy;
    private String playerGunDirection;
    private Sound shotSound;
    private Music battleMusic1;
//    private Array<Rectangle> shots;
    private Array<Rectangle> easyWalls;
    private Array<Rectangle> immortalWalls;
    private Array<Rectangle> brakemeWalls;

    @Override
    public void create(){
        playerImage= new Texture(Gdx.files.internal("player.png"));
        mapImage = new Texture(Gdx.files.internal("mapfield.png"));
//        shotImg = new Texture(Gdx.files.internal("shot.png"));
        enemyImage = new Texture(Gdx.files.internal("enemy.png"));
        fire = new Texture(Gdx.files.internal("fire.png"));
        immortalWall = new Texture(Gdx.files.internal("unbreakablewall.png"));
        brakeMeWall = new Texture(Gdx.files.internal("easytobreak.png"));
        easyWall = new Texture(Gdx.files.internal("brakefromshotwall.png"));
        battleMusic1 = Gdx.audio.newMusic(Gdx.files.internal("redalert.mp3"));
        shotSound = Gdx.audio.newSound(Gdx.files.internal("pushka.mp3"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 640, 640);

        batch = new SpriteBatch();

        enemy = new Rectangle();
        enemy.x = 320;
        enemy.y = 320;
        enemy.width = 64;
        enemy.height = 64;

        player = new Rectangle();
        player.x = 0;
        player.y = 0;
        player.width = 64;
        player.height = 64;


        battleMusic1.setLooping(true);
//        battleMusic1.play();
    }

//    public void spawnShot(){
//        shot = new Rectangle();
//        shot.x = player.x + 64;
//        shot.y = player.y + 64;
//        shot.width = 32;
//        shot.height = 32;
//        shots.add(shot);
//    }

    public void shooted(){
        shotSound.stop();
        shotSound.play();
        if(playerGunDirection == "forward") batch.draw(fire = new Texture("fire.png"), player.x, player.y+64);
        else{
            if (playerGunDirection == "left") batch.draw(fire = new Texture("fireLeft.png"), player.x-64, player.y);
            else{
                if (playerGunDirection == "right") batch.draw(fire = new Texture("fireRight.png"), player.x+64, player.y);
                else batch.draw(fire = new Texture("fireDown.png"), player.x, player.y-64);
            }
        }
        Timer.instance();

    }

    @Override
    public void render(){

        ScreenUtils.clear(1, 1, 1, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(mapImage, 0, 0);
        batch.draw(playerImage, player.x, player.y);
        batch.draw(enemyImage, enemy.x, enemy.y);
        batch.draw(immortalWall, 384, 320);
        batch.draw(easyWall, 448, 320);
        batch.draw(brakeMeWall, 512, 320);

        if(Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            playerImage = new Texture("player.png");
            batch.draw(playerImage, player.x, player.y);
            player.y += 64 ;
            playerGunDirection = "forward";
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            playerImage = new Texture("playerDown.png");
            batch.draw(playerImage, player.x, player.y);
            player.y -= 64 ;
            playerGunDirection = "down";
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            playerImage = new Texture("playerRight.png");
            batch.draw(playerImage, player.x, player.y);
            player.x += 64 ;
            playerGunDirection = "right";
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            playerImage = new Texture("playerLeft.png");
            batch.draw(playerImage, player.x, player.y);
            player.x -= 64 ;
            playerGunDirection = "left";
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            shooted();
        }

        batch.end();

        if(player.x < 0) player.x = 0;
        if(player.x > 640 - 64) player.x = 640 - 64;

        if(player.y < 0) player.y = 0;
        if(player.y > 640 - 64) player.y = 640 - 64;
    }

    @Override
    public void dispose() {
        playerImage.dispose();
        mapImage.dispose();
        shotImg.dispose();
        battleMusic1.dispose();
        shotSound.dispose();
        fire.dispose();
        batch.dispose();

    }
}
