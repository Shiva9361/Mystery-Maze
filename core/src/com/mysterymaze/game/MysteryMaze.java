package com.mysterymaze.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MysteryMaze extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public BitmapFont bigFont;

	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		bigFont = new BitmapFont(Gdx.files.internal("bigfont.fnt"),
				new TextureRegion(new Texture(Gdx.files.internal("bigfont_0.png"))), false);
		bigFont.setColor(Color.WHITE);
		setScreen(new Start(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		getScreen().dispose();
	}
}
