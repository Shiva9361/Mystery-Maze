package com.mysterymaze.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOver implements Screen {
    private final MysteryMaze game;
    private int score;
    private Sound sound;

    public GameOver(MysteryMaze _game, int _score) {
        game = _game;
        score = _score;
        sound = Gdx.audio.newSound(Gdx.files.internal("GameOver.mp3"));
        sound.loop();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.begin();
        game.bigFont.draw(game.batch, "Game Over", 150, 400);
        game.font.draw(game.batch, "Final Score " + score, 200, 325);
        game.font.draw(game.batch, "Press Space to restart", 180, 345);
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
