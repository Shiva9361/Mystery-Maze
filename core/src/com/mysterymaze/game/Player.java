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
    private int playerX;
    private int playerY;

    Player(int _leftBound, int _rightBound, int _upBound, int _downBound, int _cellSize) {
        playerX = 1;
        playerY = 1;
        leftBound = _leftBound;
        rightBound = _rightBound;
        upBound = _upBound;
        downBound = _downBound;
        cellSize = _cellSize;
    }

    public void move(Maze maze) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && (playerY < upBound)
                && (maze.playerMaze[playerX][playerY + 1] == 0 || maze.playerMaze[playerX][playerY + 1] == 7)) {
            playerY += 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D) && (playerX < rightBound)
                && (maze.playerMaze[playerX + 1][playerY] == 0 || maze.playerMaze[playerX + 1][playerY] == 7)) {
            playerX += 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.A) && (playerX > leftBound)
                && (maze.playerMaze[playerX - 1][playerY] == 0 || maze.playerMaze[playerX - 1][playerY] == 7)) {
            playerX -= 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S) && (playerY > downBound)
                && (maze.playerMaze[playerX][playerY - 1] == 0 || maze.playerMaze[playerX][playerY - 1] == 7)) {
            playerY -= 1;
        }
        if (maze.playerMaze[playerX][playerY] == 7) {
            maze.playerMaze[playerX][playerY] = 0;
            maze.keyObtained = true;
        }

    }

    public void render(SpriteBatch batch, Texture playerTexture) {
        batch.draw(playerTexture, playerX * cellSize, playerY * cellSize, cellSize, cellSize);
    }
}
