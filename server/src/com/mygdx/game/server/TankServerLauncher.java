package com.mygdx.game.server;

import com.badlogic.gdx.backends.headless.HeadlessApplication;

public class TankServerLauncher {
	public static void main(String[] args) {
		new HeadlessApplication(new TankServerGame());
	}
}