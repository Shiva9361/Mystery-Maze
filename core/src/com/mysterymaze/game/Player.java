package com.mysterymaze.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    private int cellSize;
    private int X;
    private int Y;
    private int bombX;
    private int bombY;
    private Sound moveSound;

    public float bombTimer;
    public float flashTimer;
    public boolean bombPresent;
    public boolean flashPresent;
    public int lives;
    public int score;
    public int bombs;

    Player(int _cellSize, int _lives, int _score) {
        X = 1;
        Y = 1;
        bombX = 0;
        bombY = 0;
        cellSize = _cellSize;
        lives = _lives;
        score = _score;
        bombs = 5;
        moveSound = Gdx.audio.newSound(Gdx.files.internal("move.wav"));
    }

    public void move(Maze maze) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)
                && (maze.playerMaze[X][Y + 1] != Maze.WALL)) {
            Y += 1;
            moveSound.play();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)
                && (maze.playerMaze[X + 1][Y] != Maze.WALL)) {
            X += 1;
            moveSound.play();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.A)
                && (maze.playerMaze[X - 1][Y] != Maze.WALL)) {
            X -= 1;
            moveSound.play();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)
                && (maze.playerMaze[X][Y - 1] != Maze.WALL)) {
            Y -= 1;
            moveSound.play();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !bombPresent && !flashPresent && bombs > 0) {
            bombX = X;
            bombY = Y;
            bombTimer = 1;
            bombs -= 1;
            bombPresent = true;
        }

    }

    public void render(SpriteBatch batch, Texture playerTexture, Texture bombTexture) {
        batch.draw(playerTexture, X * cellSize, Y * cellSize, cellSize, cellSize);
        if (bombPresent) {
            batch.draw(bombTexture, bombX * cellSize, bombY * cellSize, cellSize, cellSize);
        }
    }

    public void blast(Maze maze, Array<Enemy> enemies) {
        flashTimer = 1;
        flashPresent = true;
        maze.updatePlayerMaze(bombX, bombY, Maze.BOMBFLASH);
        maze.updatePlayerMaze(bombX - 1, bombY, Maze.BOMBFLASH);
        maze.updatePlayerMaze(bombX, bombY - 1, Maze.BOMBFLASH);
        maze.updatePlayerMaze(bombX + 1, bombY, Maze.BOMBFLASH);
        maze.updatePlayerMaze(bombX, bombY + 1, Maze.BOMBFLASH);
        maze.updatePlayerMaze(bombX - 2, bombY, Maze.BOMBFLASH);
        maze.updatePlayerMaze(bombX, bombY - 2, Maze.BOMBFLASH);
        maze.updatePlayerMaze(bombX + 2, bombY, Maze.BOMBFLASH);
        maze.updatePlayerMaze(bombX, bombY + 2, Maze.BOMBFLASH);
        maze.updatePlayerMaze(bombX - 1, bombY - 1, Maze.BOMBFLASH);
        maze.updatePlayerMaze(bombX + 1, bombY + 1, Maze.BOMBFLASH);

        for (int i = 0; i < enemies.size; i++) {
            if (manhattanDistanceFromBomb(enemies.get(i)) <= 2) {
                enemies.removeIndex(i);
            }
        }
        if (manhattanDistanceFromBomb() <= 2) {
            lives--;
            reset();
        }
    }

    public void goBack() {
        Y -= 1;
    }

    private int manhattanDistanceFromBomb(Enemy enemy) {
        int x = enemy.getX();
        int y = enemy.getY();
        return (int) (Math.abs(x - bombX) + Math.abs(y - bombY));
    }

    private int manhattanDistanceFromBomb() {
        return (int) (Math.abs(X - bombX) + Math.abs(Y - bombY));
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public void reset() {
        X = 1;
        Y = 1;
    }

    public int getScore() {
        return score;
    }

    public void removeBomb() {
        bombTimer = 0;
        bombPresent = false;
    }

    public void removeFlash(Maze maze) {
        flashPresent = false;
        maze.updatePlayerMaze(bombX, bombY, Maze.PATH);
        maze.updatePlayerMaze(bombX - 1, bombY, Maze.PATH);
        maze.updatePlayerMaze(bombX, bombY - 1, Maze.PATH);
        maze.updatePlayerMaze(bombX + 1, bombY, Maze.PATH);
        maze.updatePlayerMaze(bombX, bombY + 1, Maze.PATH);
        maze.updatePlayerMaze(bombX - 2, bombY, Maze.PATH);
        maze.updatePlayerMaze(bombX, bombY - 2, Maze.PATH);
        maze.updatePlayerMaze(bombX + 2, bombY, Maze.PATH);
        maze.updatePlayerMaze(bombX, bombY + 2, Maze.PATH);
        maze.updatePlayerMaze(bombX - 1, bombY - 1, Maze.PATH);
        maze.updatePlayerMaze(bombX + 1, bombY + 1, Maze.PATH);
    }
}
