package com.mysterymaze.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ScreenUtils;

public class Start implements Screen {
    private final MysteryMaze game;
    Sound sound;

    public Start(MysteryMaze _game) {
        game = _game;
        sound = Gdx.audio.newSound(Gdx.files.internal("start.ogg"));
        sound.loop();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.begin();
        game.bigFont.draw(game.batch, "Welcome to Mystery Maze", 75, 400);
        game.font.draw(game.batch, "Press Space to start", 290, 325);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new Level(game, 5, 0));
            dispose();
        }
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void hide() {
    }

    @Override
    public void show() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        sound.dispose();
    }
}
