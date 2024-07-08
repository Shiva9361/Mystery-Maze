package com.mysterymaze.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;

public class Level implements Screen {
    final MysteryMaze game;

    SpriteBatch batch;
    float timer;
    Texture wallTexture;
    Texture pathTexture;
    Texture playerTexture;
    Texture spikeTexture;
    Texture keyTexture;
    Texture enemyTexture;
    Texture coinTexture;
    Texture doorTexture;
    Texture bombTexture;
    Texture bombFlashTexture;
    Texture treasureTexture;
    Texture treasureOpenTexture;

    Sound treasureSound;
    Sound coinSound;
    Sound doorSound;

    Maze maze;
    Player player;
    Array<Enemy> enemies;
    OrthographicCamera camera;
    float enemy_refresh;

    public Level(final MysteryMaze _game, int _playerLives, int _playerScore) {
        game = _game;
        timer = 60;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 530, 600);

        wallTexture = new Texture("V01_Tile1.png");
        pathTexture = new Texture("V01_Tile3.png");
        playerTexture = new Texture("V01_MainCharacter.png");
        spikeTexture = new Texture("V01_Obstacle.png");
        keyTexture = new Texture("V01_Key.png");
        enemyTexture = new Texture("V01_Enemy.png");
        coinTexture = new Texture("V01_Coin.png");
        doorTexture = new Texture("V01_Door.png");
        bombTexture = new Texture("V01_Bomb.png");
        bombFlashTexture = new Texture("V01_Bomb_Flash.png");
        treasureTexture = new Texture("V01_Chest.png");
        treasureOpenTexture = new Texture("V01_Chest_Open.png");

        treasureSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
        coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
        doorSound = Gdx.audio.newSound(Gdx.files.internal("door.wav"));

        enemies = new Array<>();
        Array<Vector2> enemySpawns = new Array<>();
        maze = new Maze(16, 16, 16, enemySpawns);
        player = new Player(16, _playerLives, _playerScore);
        for (Vector2 position : enemySpawns) {
            enemies.add(new Enemy(1, 31, 31, 1, 16, position));
        }
        enemy_refresh = 0;
    }

    @Override
    public void render(float delta) {
        timer -= delta;
        if (timer <= 0) {
            int score = player.score;
            game.setScreen(new GameOver(game, score));
            dispose();
        }
        enemy_refresh += delta;
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        maze.render(game.batch, wallTexture, pathTexture, spikeTexture, keyTexture, coinTexture, doorTexture,
                bombFlashTexture, treasureTexture, treasureOpenTexture);
        game.bigFont.draw(game.batch, "Mystery Maze", 20, 580);
        game.font.draw(game.batch, "Time: " + timer, 370, 590);
        game.font.draw(game.batch, "Score: " + player.getScore(), 370, 570);
        game.font.draw(game.batch, "Lives: " + player.lives, 370, 550);
        game.font.draw(game.batch, "Bombs: " + player.bombs, 430, 550);
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
                    game.setScreen(new GameOver(game, player.score));
                    dispose();
                }
                player.reset();
                break;
            case Maze.COIN:
                player.score += 10;
                maze.playerMaze[playerX][playerY] = 0;
                coinSound.play();
                break;
            case Maze.DOOR:
                if (maze.keyObtained) {
                    doorSound.play();
                    game.setScreen(new Level(game, player.lives, player.score + (int) timer * 100));
                    dispose();
                } else {
                    player.goBack();
                }
                break;
            case Maze.TREASURE:
                player.score += 100;
                maze.playerMaze[playerX][playerY] = Maze.TREASUREOPEN;
                treasureSound.play();
            default:
                break;
        }
        player.render(game.batch, playerTexture, bombTexture);
        if (player.bombPresent) {
            player.bombTimer -= delta;
            if (player.bombTimer <= 0) {
                player.blast(maze, enemies);
                player.removeBomb();
            }
        }
        if (player.flashPresent) {
            player.flashTimer -= delta;
            if (player.flashTimer <= 0) {
                player.removeFlash(maze);
            }
        }
        if (enemy_refresh > .4) {
            enemy_refresh = 0;
            for (Enemy enemy : enemies) {
                enemy.move(maze, player.getX(), player.getY());
            }
        }
        for (Enemy enemy : enemies) {
            if (enemy.getX() == player.getX() && enemy.getY() == player.getY()) {
                player.lives -= 1;
                if (player.lives <= 0) {
                    game.setScreen(new GameOver(game, player.score));
                    dispose();
                }
                player.reset();
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
        spikeTexture.dispose();
        keyTexture.dispose();
        enemyTexture.dispose();
        coinTexture.dispose();
        doorTexture.dispose();
        bombTexture.dispose();
        bombFlashTexture.dispose();
        treasureTexture.dispose();
        treasureOpenTexture.dispose();
        doorSound.dispose();
        treasureSound.dispose();
        coinSound.dispose();
    }
}
