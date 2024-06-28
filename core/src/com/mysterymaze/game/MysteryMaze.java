package com.mysterymaze.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MysteryMaze extends ApplicationAdapter {
	SpriteBatch batch;
	Texture wall_texture;
	Texture path_texture;
	Maze maze;

	@Override
	public void create() {
		batch = new SpriteBatch();
		wall_texture = new Texture("V01_Tile1.png");
		path_texture = new Texture("V01_Tile3.png");
		maze = new Maze(16, 16, 16);
	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		maze.render(batch, wall_texture, path_texture);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		wall_texture.dispose();
		path_texture.dispose();
	}
}
