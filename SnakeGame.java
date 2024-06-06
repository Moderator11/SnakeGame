package SnakeGame;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame {
    private final static Point UP = new Point(0, -1);
    private final static Point RIGHT = new Point(1, 0);
    private final static Point DOWN = new Point(0, 1);
    private final static Point LEFT = new Point(-1, 0);

    Point boardSize;
    Point snakeDirection;
    LinkedList<Point> snake;
    Timer frameRefresher;
    Timer foodSpawner;
    Point food;
    inputHandler ih;
    SnakeGameScreen screen;

    public SnakeGame() {
        super("SnakeGame");
        InitializeGame();
        GameStart();
    }

    void InitializeGame() {
        boardSize = new Point(25, 25);
        frameRefresher = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SnakeMovement();
                screen.repaint();
            }
        });

        ih = new inputHandler();
        screen = new SnakeGameScreen(boardSize, 20);
        add(screen);
        addKeyListener(ih);
        setSize(517, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        food = new Point(-1, -1);
        screen.food = food;
        Random r = new Random();
        foodSpawner = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (food.x == -1) {
                    food.x = r.nextInt(0, boardSize.x);
                    food.y = r.nextInt(0, boardSize.y);
                }
            }
        });
    }

    void GameStart() {
        frameRefresher.setDelay(100);
        screen.gameOver = false;

        frameRefresher.start();
        foodSpawner.start();

        snake = new LinkedList<Point>();
        snake.add(new Point(5, 5));
        screen.snake = snake;
        snakeDirection = UP;
    }

    void GameStop() {
        frameRefresher.stop();
        foodSpawner.stop();
        screen.gameOver = true;
        screen.repaint();
    }

    void SnakeMovement() {
        Point newHead = Point.addClamp(snake.getFirst(), snakeDirection, boardSize.x, boardSize.y);
      //  if (newHead.x == boardSize.x && newHead.y == boardSize.y)
      //  	GameStop();
        for (Point p : snake) {
            if (p.x == newHead.x && p.y == newHead.y) {
                GameStop();
            }
        }
        snake.addFirst(newHead);
        if (newHead.x == food.x && newHead.y == food.y) {
            frameRefresher.setDelay(frameRefresher.getDelay() - 5);
            food.x = -1;
            food.y = -1;
        } else {
            snake.removeLast();
        }
    }

    private class inputHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case 32:
                    if (screen.gameOver == true) GameStart();
                case 38:
                    snakeDirection = UP;
                    break;
                case 40:
                    snakeDirection = DOWN;
                    break;
                case 39:
                    snakeDirection = RIGHT;
                    break;
                case 37:
                    snakeDirection = LEFT;
                    break;
                /*case KeyEvent.VK_SHIFT:
                    frameRefresher.setDelay(50);
                    break;
                default:
                    System.out.println(e.getKeyCode());*/
            }
        }

        /*@Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SHIFT)
                frameRefresher.setDelay(100);
        }*/
    }
}