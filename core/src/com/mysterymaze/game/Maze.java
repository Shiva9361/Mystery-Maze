package com.mysterymaze.game;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Maze {
    private static final int S = 1;
    private static final int E = 2;
    private static final int MIN_ENEMY_DISTANCE = 10;
    public static final int WALL = 1;
    public static final int SPIKE = 4;
    public static final int KEY = 7;
    public static final int PATH = 0;
    public static final int COIN = 3;
    public static final int DOOR = 9;

    protected int[][] maze;
    protected int[][] playerMaze;
    private int cellSize;
    private int height;
    private int width;
    private Random rand;
    private int key;
    public boolean keyObtained;

    public Maze(int _cellSize, int _height, int _width, Array<Vector2> enemySpawns) {
        cellSize = _cellSize;
        height = _height;
        width = _width;
        maze = new int[height][width];
        playerMaze = new int[2 * height + 1][2 * width + 1];
        rand = new Random();
        key = 1;
        generateMaze(1, 0, height, width);
        initializePlayerMaze(enemySpawns);
        System.out.println(key);
        keyObtained = key == 0 ? false : true;
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
                if (maze[wallX][wallY] != 0) {
                    continue;
                }
                maze[wallX][wallY] = dir;
                if (rand.nextDouble() < .025) {
                    maze[wallX][wallY] = SPIKE;
                } else if (rand.nextDouble() < .1 && key > 0
                        && (wallX != 0 && wallY != 0 && wallX != height - 1 && wallY != width - 1)) {
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

    private void initializePlayerMaze(Array<Vector2> enemySpawns) {
        int enemies = rand.nextInt(10);
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                if (maze[x][y] == S) {
                    playerMaze[2 * x][2 * y] = playerMaze[2 * x][2 * y + 1] = 1;
                } else if (maze[x][y] == E) {
                    playerMaze[2 * x][2 * y] = playerMaze[2 * x + 1][2 * y] = 1;
                } else if (maze[x][y] == SPIKE || maze[x][y] == KEY) {
                    playerMaze[2 * x][2 * y] = maze[x][y];
                } else if (maze[x][y] == Maze.PATH) {
                    if (x > 5 && y > 5) {
                        if (enemies > 0) {
                            Vector2 position = new Vector2(x, y);
                            if (isFarFromOtherSpawns(position, enemySpawns)) {
                                enemySpawns.add(position);
                                enemies--;
                            } else {
                                if (rand.nextDouble() < .3) {
                                    playerMaze[2 * x][2 * y] = COIN;
                                }
                            }
                        } else {
                            if (rand.nextDouble() < .3) {
                                playerMaze[2 * x][2 * y] = COIN;
                            }
                        }
                    } else {
                        if (rand.nextDouble() < .5) {
                            playerMaze[2 * x][2 * y] = COIN;
                        }
                    }
                }
            }
        }
        for (int x = 0; x < 2 * height + 1; x++) {
            for (int y = 0; y < 2 * width + 1; y++) {
                if (x == 0 || y == 0 || x == 2 * height || y == 2 * width) {
                    playerMaze[x][y] = WALL;
                }
            }
        }

        int doorX = rand.nextInt(2 * width);
        int doorY = 2 * width;
        playerMaze[doorX][doorY] = DOOR;
    }

    private boolean isFarFromOtherSpawns(Vector2 newSpawn, Array<Vector2> enemySpawns) {
        for (Vector2 spawn : enemySpawns) {
            if (manhattanDistance(spawn, newSpawn) < MIN_ENEMY_DISTANCE) {
                return false;
            }
        }
        return true;
    }

    private int manhattanDistance(Vector2 a, Vector2 b) {
        return (int) (Math.abs(a.x - b.x) + Math.abs(a.y - b.y));
    }

    public void render(SpriteBatch batch, Texture wallTexture, Texture pathTexture, Texture spikeTexture,
            Texture keyTexture, Texture coinTexture, Texture doorTexture) {
        for (int x = 0; x < 2 * height + 1; x++) {
            for (int y = 0; y < 2 * width + 1; y++) {
                batch.draw(pathTexture, x * cellSize, y * cellSize, cellSize, cellSize);

            }
        }
        for (int x = 0; x < 2 * height + 1; x++) {
            for (int y = 0; y < 2 * width + 1; y++) {
                if (x == 0 || y == 0 || x == 2 * height || y == 2 * width) {
                    if (playerMaze[x][y] == DOOR) {
                        batch.draw(doorTexture, x * cellSize, y * cellSize, cellSize, cellSize);
                    } else {
                        batch.draw(wallTexture, x * cellSize, y * cellSize, cellSize, cellSize);
                    }
                } else if (playerMaze[x][y] == WALL) {
                    batch.draw(wallTexture, x * cellSize, y * cellSize, cellSize, cellSize);
                } else if (playerMaze[x][y] == SPIKE) {
                    batch.draw(spikeTexture, x * cellSize, y * cellSize, cellSize, cellSize);
                } else if (playerMaze[x][y] == KEY) {
                    batch.draw(keyTexture, x * cellSize, y * cellSize, cellSize, cellSize);
                } else if (playerMaze[x][y] == COIN) {
                    batch.draw(coinTexture, x * cellSize, y * cellSize, cellSize, cellSize);
                }

            }
        }

    }
}
