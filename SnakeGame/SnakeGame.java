package SnakeGame;

import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
    JLabel textLabel;

    Timer actionTimer;
    static int iteration = 0;

    public SnakeGame() {
        super("SnakeGame");
        startScreen();
    }

    void startScreen() {
        AnimationSnake start = new AnimationSnake();
        start.setLayout(null);
        start.setBackground(Color.BLACK);

        String titleText = "!!! Snake Game !!!";
        textLabel = new JLabel(titleText, SwingConstants.CENTER);
        textLabel.setFont(new Font(textLabel.getFont().getFontName(), Font.PLAIN, 40));
        textLabel.setForeground(Color.WHITE);
        textLabel.setBounds(50, 150, 400, 40);
        start.add(textLabel);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                actionTimer.stop();
                remove(start);
                InitializeGame();
                GameStart();
                remove(start);
                revalidate();
                toFront();
                requestFocus();
            }

        });

        startButton.setBounds(173, 280, 170, 40);
        startButton.setBackground(Color.WHITE);
        startButton.setForeground(Color.BLACK);
        start.add(startButton);

        add(start);
        setSize(517, 540);
        setVisible(true);
    }

    class AnimationSnake extends JPanel {
        LinkedList<Point> squares = new LinkedList<>();
        int squareSize = 20;
        int xPoint = 20;
        int yPoint = 20;
        int i = 0;
        Color[] rainbowColors
                = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, new Color(148, 0, 211) , Color.PINK };

        public AnimationSnake() {
            actionTimer = new Timer(100, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (squares.size() < 19) {
                        squares.add(new Point(xPoint, yPoint));
                    } else {
                        squares.add(new Point(xPoint, yPoint));
                        squares.remove(0);
                    }

                    if (yPoint == 20 && xPoint < 460)
                        xPoint += squareSize;
                    else if (xPoint == 460 && yPoint < 460)
                        yPoint += squareSize;
                    else if (yPoint == 460 && xPoint > 20)
                        xPoint -= squareSize;
                    else if (xPoint == 20 && yPoint > 20)
                        yPoint -= squareSize;

                    repaint();

                }
            });
            actionTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Point square : squares) {
                g.setColor(rainbowColors[(i + iteration) % rainbowColors.length]);
                i++;
                g.drawRect(square.x, square.y, squareSize, squareSize);
                g.fillRect(square.x, square.y, squareSize, squareSize);
            }
        }
    }

    void InitializeGame() {
        boardSize = new Point(20, 20);
        frameRefresher = new Timer(90, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SnakeMovement();
                SnakeGameScreen.iteration++;
                screen.repaint();
            }
        });

        ih = new inputHandler();
        screen = new SnakeGameScreen(boardSize, 25);
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
        frameRefresher.setDelay(90);
        screen.gameOver = false;

        frameRefresher.start();
        foodSpawner.start();

        snake = new LinkedList<Point>();
        snake.add(new Point(10, 10));
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
        if (newHead.x < 0 || newHead.x >= boardSize.x || newHead.y < 0 || newHead.y >= boardSize.y)
            GameStop();
        for (Point p : snake) {
            if (p.x == newHead.x && p.y == newHead.y) {
                GameStop();
            }
        }
        snake.addFirst(newHead);
        if (newHead.x == food.x && newHead.y == food.y) {
            frameRefresher.setDelay(frameRefresher.getDelay() - 3);
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
                    if(snakeDirection != DOWN) snakeDirection = UP;
                    break;
                case 40:
                    if(snakeDirection != UP) snakeDirection = DOWN;
                    break;
                case 39:
                    if(snakeDirection != LEFT) snakeDirection = RIGHT;
                    break;
                case 37:
                    if(snakeDirection != RIGHT) snakeDirection = LEFT;
                    break;
                /*case KeyEvent.VK_SHIFT:
                    frameRefresher.setDelay(50);
                    break;
                default:
                    System.out.println(e.getKeyCode());*/
            }
        }
	//Sprinting
        /*@Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SHIFT)
                frameRefresher.setDelay(100);
        }*/
    }
}
