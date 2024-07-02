package com.mysterymaze.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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
    Texture enemyTexture;
    Texture coinTexture;
    Maze maze;
    Player player;
    Array<Enemy> enemies;
    OrthographicCamera camera;
    float enemy_refresh;

    public Level(final MysteryMaze _game) {
        game = _game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 600);

        wallTexture = new Texture("V01_Tile1.png");
        pathTexture = new Texture("V01_Tile3.png");
        playerTexture = new Texture("V01_MainCharacter.png");
        spikeTexture = new Texture("V01_Obstacle.png");
        keyTexture = new Texture("V01_Key.png");
        enemyTexture = new Texture("V01_Enemy.png");
        coinTexture = new Texture("V01_Coin.png");

        enemies = new Array<>();
        Array<Vector2> enemySpawns = new Array<>();
        maze = new Maze(16, 16, 16, enemySpawns);
        player = new Player(1, 31, 31, 1, 16);
        for (Vector2 position : enemySpawns) {
            enemies.add(new Enemy(1, 31, 31, 1, 16, position));
        }
        enemy_refresh = 0;
    }

    @Override
    public void render(float delta) {
        enemy_refresh += delta;
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        maze.render(game.batch, wallTexture, pathTexture, spikeTexture, keyTexture, coinTexture);
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
                break;
            case Maze.COIN:
                player.score += 10;
                maze.playerMaze[playerX][playerY] = 0;
                break;
            default:
                break;
        }
        player.render(game.batch, playerTexture);
        if (enemy_refresh > .4) {
            enemy_refresh = 0;
            for (Enemy enemy : enemies) {
                enemy.move(maze, player.getX(), player.getY());
            }
        }
        for (Enemy enemy : enemies) {
            if (enemy.getX() == player.getX() && enemy.getY() == player.getY()) {
                game.setScreen(new GameOver(game));
                dispose();
            }
            enemy.render(game.batch, enemyTexture);
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
        wallTexture.dispose();
        pathTexture.dispose();
        playerTexture.dispose();
    }
}
