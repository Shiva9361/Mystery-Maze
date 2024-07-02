package com.mysterymaze.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    private int leftBound;
    private int rightBound;
    private int upBound;
    private int downBound;
    private int cellSize;
    private int X;
    private int Y;
    public int lives;
    public int score;

    Player(int _leftBound, int _rightBound, int _upBound, int _downBound, int _cellSize) {
        X = 1;
        Y = 1;
        leftBound = _leftBound;
        rightBound = _rightBound;
        upBound = _upBound;
        downBound = _downBound;
        cellSize = _cellSize;
        lives = 5;
    }

    public void move(Maze maze) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && (Y < upBound)
                && (maze.playerMaze[X][Y + 1] != Maze.WALL)) {
            Y += 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D) && (X < rightBound)
                && (maze.playerMaze[X + 1][Y] != Maze.WALL)) {
            X += 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.A) && (X > leftBound)
                && (maze.playerMaze[X - 1][Y] != Maze.WALL)) {
            X -= 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S) && (Y > downBound)
                && (maze.playerMaze[X][Y - 1] != Maze.WALL)) {
            Y -= 1;
        }

    }

    public void render(SpriteBatch batch, Texture playerTexture) {
        batch.draw(playerTexture, X * cellSize, Y * cellSize, cellSize, cellSize);
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
}
