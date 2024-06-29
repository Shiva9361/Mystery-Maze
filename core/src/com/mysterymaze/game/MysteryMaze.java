package com.mysterymaze.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MysteryMaze extends ApplicationAdapter {
	SpriteBatch batch;
	Texture wallTexture;
	Texture pathTexture;
	Texture playerTexture;
	Texture spikeTexture;
	Texture keyTexture;
	Maze maze;
	Player player;

	@Override
	public void create() {
		batch = new SpriteBatch();
		wallTexture = new Texture("V01_Tile1.png");
		pathTexture = new Texture("V01_Tile3.png");
		playerTexture = new Texture("V01_MainCharacter.png");
		spikeTexture = new Texture("V01_Obstacle.png");
		keyTexture = new Texture("V01_Key.png");
		maze = new Maze(16, 16, 16);
		player = new Player(1, 31, 31, 1, 16);
	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		maze.render(batch, wallTexture, pathTexture, spikeTexture, keyTexture);
		player.move(maze);
		player.render(batch, playerTexture);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		wallTexture.dispose();
		pathTexture.dispose();
		playerTexture.dispose();
	}
}
