package com.mysterymaze.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mysterymaze.game.MysteryMaze;

@SuppressWarnings("unused")
public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(10);
		config.setTitle("Mystery Maze");
		config.useVsync(true);
		config.setWindowedMode(720, 720);
		new Lwjgl3Application(new MysteryMaze(), config);
	}
}
