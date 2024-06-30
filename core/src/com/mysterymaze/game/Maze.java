package com.mysterymaze.game;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Maze {
    private static final int S = 1;
    private static final int E = 2;
    public static final int WALL = 1;
    public static final int SPIKE = 4;
    public static final int KEY = 7;

    protected int[][] maze;
    protected int[][] playerMaze;
    private int cellSize;
    private int height;
    private int width;
    private Random rand;
    private int key;
    public boolean keyObtained;

    public Maze(int _cellSize, int _height, int _width) {
        cellSize = _cellSize;
        height = _height;
        width = _width;
        maze = new int[height][width];
        playerMaze = new int[2 * height + 1][2 * width + 1];
        rand = new Random();
        key = 1;
        generateMaze(1, 1, height - 1, width - 1);
        initializePlayerMaze();
        keyObtained = false;
    }

    private void generateMaze(int x, int y, int width, int height) {
        if (width < 2 || height < 2) {
            return;
        }

        boolean horizontalDivide = rand.nextBoolean();

        if (width > height) {
            horizontalDivide = false;
        } else if (height > width) {
            horizontalDivide = true;
        }
        int wallX = x + (horizontalDivide ? 0 : rand.nextInt(width - 1));
        int wallY = y + (horizontalDivide ? rand.nextInt(height - 1) : 0);

        int gapX = wallX + (horizontalDivide ? rand.nextInt(width + 1) : 0);
        int gapY = wallY + (horizontalDivide ? 0 : rand.nextInt(height + 1));

        int deltaX = horizontalDivide ? 1 : 0;
        int deltaY = horizontalDivide ? 0 : 1;

        int wallLength = horizontalDivide ? width : height;
        int dir = horizontalDivide ? S : E;
        for (int i = 0; i < wallLength; i++) {
            if ((wallX != gapX || wallY != gapY) && wallX < this.width && wallY < this.height) {
                maze[wallX][wallY] |= dir;
                if (rand.nextDouble() < .025) {
                    maze[wallX][wallY] = SPIKE;
                } else if (rand.nextDouble() < .1 && key > 0) {
                    key = 0;
                    maze[wallX][wallY] = KEY;
                }
            }
            wallX += deltaX;
            wallY += deltaY;
        }

        int nextX, nextY, nextWidth, nextHeight;

        // First quadrant
        nextX = x;
        nextY = y;
        nextWidth = horizontalDivide ? width : wallX - x + 1;
        nextHeight = horizontalDivide ? wallY - y + 1 : height;
        generateMaze(nextX, nextY, nextWidth, nextHeight);

        // Second quadrant
        nextX = horizontalDivide ? x : wallX + 1;
        nextY = horizontalDivide ? wallY + 1 : y;
        nextWidth = horizontalDivide ? width : x + width - wallX - 1;
        nextHeight = horizontalDivide ? y + height - wallY - 1 : height;
        generateMaze(nextX, nextY, nextWidth, nextHeight);
    }

    private void initializePlayerMaze() {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                if (maze[x][y] == S) {
                    playerMaze[2 * x][2 * y] = playerMaze[2 * x][2 * y + 1] = 1;
                } else if (maze[x][y] == E) {
                    playerMaze[2 * x][2 * y] = playerMaze[2 * x + 1][2 * y] = 1;
                } else if (maze[x][y] == SPIKE || maze[x][y] == KEY) {
                    playerMaze[2 * x][2 * y] = maze[x][y];
                }
            }
        }
    }

    public void render(SpriteBatch batch, Texture wallTexture, Texture pathTexture, Texture spikeTexture,
            Texture keyTexture) {
        for (int x = 0; x < 2 * height + 1; x++) {
            for (int y = 0; y < 2 * width + 1; y++) {
                batch.draw(pathTexture, x * cellSize, y * cellSize, cellSize, cellSize);
                if (x == 0 || y == 0 || x == 2 * height || y == 2 * width) {
                    batch.draw(wallTexture, x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
        }
        for (int x = 0; x < 2 * height + 1; x++) {
            for (int y = 0; y < 2 * width + 1; y++) {
                if (playerMaze[x][y] == WALL) {
                    batch.draw(wallTexture, x * cellSize, y * cellSize, cellSize, cellSize);
                } else if (playerMaze[x][y] == SPIKE) {
                    batch.draw(spikeTexture, x * cellSize, y * cellSize, cellSize, cellSize);
                } else if (playerMaze[x][y] == KEY) {
                    batch.draw(keyTexture, x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
        }

    }
}
