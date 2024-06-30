package com.mysterymaze.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Screen;

public class Level implements Screen {
    final MysteryMaze game;

    SpriteBatch batch;
    Texture wallTexture;
    Texture pathTexture;
    Texture playerTexture;
    Texture spikeTexture;
    Texture keyTexture;
    Maze maze;
    Player player;
    OrthographicCamera camera;

    public Level(final MysteryMaze _game) {
        game = _game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 600);
        wallTexture = new Texture("V01_Tile1.png");
        pathTexture = new Texture("V01_Tile3.png");
        playerTexture = new Texture("V01_MainCharacter.png");
        spikeTexture = new Texture("V01_Obstacle.png");
        keyTexture = new Texture("V01_Key.png");
        maze = new Maze(16, 16, 16);
        player = new Player(1, 31, 31, 1, 16);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        maze.render(game.batch, wallTexture, pathTexture, spikeTexture, keyTexture);
        player.move(maze);
        int playerX = player.getX();
        int playerY = player.getY();
        switch (maze.playerMaze[playerX][playerY]) {
            case Maze.KEY:
                maze.keyObtained = true;
                maze.playerMaze[playerX][playerY] = 0;
                break;
            case Maze.SPIKE:
                player.lives -= 1;
                if (player.lives <= 0) {
                    game.setScreen(new GameOver(game));
                    dispose();
                }
                player.reset();
            default:
                break;
        }
        player.render(game.batch, playerTexture);
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
        wallTexture.dispose();
        pathTexture.dispose();
        playerTexture.dispose();
    }
}
