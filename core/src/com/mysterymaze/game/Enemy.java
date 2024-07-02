package com.mysterymaze.game;

import java.util.Random;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Enemy {
    private int leftBound;
    private int rightBound;
    private int upBound;
    private int downBound;
    private int X;
    private int Y;
    private int cellSize;
    private Random rand;
    private static int CHASE_DISTANCE = 10;

    Enemy(int _leftBound, int _rightBound, int _upBound, int _downBound, int _cellSize, Vector2 position) {
        X = (int) position.x;
        Y = (int) position.y;
        leftBound = _leftBound;
        rightBound = _rightBound;
        upBound = _upBound;
        downBound = _downBound;
        cellSize = _cellSize;
        rand = new Random();
    }

    private boolean isPlayerClose(int playerX, int playerY) {
        return Math.abs(X - playerX) <= CHASE_DISTANCE && Math.abs(Y - playerY) <= CHASE_DISTANCE;
    }

    public void move(Maze maze, int playerX, int playerY) {
        if (isPlayerClose(playerX, playerY)) {
            chaseMove(maze, playerX, playerY);
        } else {
            randomMove(maze);
        }
    }

    private int manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private void chaseMove(Maze maze, int playerX, int playerY) {
        int bestChoice = -1;
        int bestDistance = Integer.MAX_VALUE;

        // Check each possible move
        if (Y < upBound && maze.playerMaze[X][Y + 1] != Maze.WALL) {
            int distance = manhattanDistance(X, Y + 1, playerX, playerY);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestChoice = 1; // move up
            }
        }
        if (X < rightBound && maze.playerMaze[X + 1][Y] != Maze.WALL) {
            int distance = manhattanDistance(X + 1, Y, playerX, playerY);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestChoice = 2; // move right
            }
        }
        if (X > leftBound && maze.playerMaze[X - 1][Y] != Maze.WALL) {
            int distance = manhattanDistance(X - 1, Y, playerX, playerY);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestChoice = 3; // move left
            }
        }
        if (Y > downBound && maze.playerMaze[X][Y - 1] != Maze.WALL) {
            int distance = manhattanDistance(X, Y - 1, playerX, playerY);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestChoice = 4; // move down
            }
        }

        if (bestChoice != -1) {
            moveInDirection(bestChoice);
        }
    }

    private void randomMove(Maze maze) {
        Array<Integer> possible = new Array<Integer>();
        if ((Y < upBound) && (maze.playerMaze[X][Y + 1] != Maze.WALL)) {
            possible.add(1);
        }
        if ((X < rightBound) && (maze.playerMaze[X + 1][Y] != Maze.WALL)) {
            possible.add(2);
        }
        if ((X > leftBound) && (maze.playerMaze[X - 1][Y] != Maze.WALL)) {
            possible.add(3);
        }
        if ((Y > downBound) && (maze.playerMaze[X][Y - 1] != Maze.WALL)) {
            possible.add(4);
        }

        int choice = rand.nextInt(possible.size);
        choice = possible.get(choice);
        moveInDirection(choice);
    }

    private void moveInDirection(int choice) {
        switch (choice) {
            case 1:
                Y += 1;
                break;
            case 2:
                X += 1;
                break;
            case 3:
                X -= 1;
                break;
            case 4:
                Y -= 1;
                break;
            default:
                break;
        }
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public void render(SpriteBatch batch, Texture enemyTexture) {
        batch.draw(enemyTexture, X * cellSize, Y * cellSize, cellSize, cellSize);
    }
}
