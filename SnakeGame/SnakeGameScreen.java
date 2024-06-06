package SnakeGame;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGameScreen extends JPanel {
    public LinkedList<Point> snake;
    public Point food;
    private Point boardSize;
    private int size;
    public boolean gameOver = false;

    public SnakeGameScreen(Point boardSize, int size) {
        this.boardSize = boardSize;
        this.size = size;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        if (gameOver) {
            RenderGameOverScreen(g);
            return;
        }

        RenderGameScreen(g);
    }

    private void RenderGameOverScreen(Graphics g)
    {
        g.setColor(Color.BLUE);
        g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 20));
        g.drawString("GameOver...Press Space bar to restart", 60, getHeight() / 2);
        g.drawString(String.format("Your score : %d", snake.size()), 60, getHeight() / 2 + 30);
    }

    private void RenderGameScreen(Graphics g)
    {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        /*g.setColor(Color.BLACK);
        for (int y = 0; y < boardSize.y; y++) {
            for (int x = 0; x < boardSize.x; x++) {
                g.drawRect(x * size, y * size, size, size);
            }
        }*/

        Random r = new Random();
        g.setColor(Color.RED);
        for (int i = 0; i < snake.size(); i++) {
            g.setColor(new Color(Math.clamp(255 - i * 25, 0, 255), Math.clamp(i * 25, 0, 255), Math.clamp(i * 10, 0, 255)));
            g.fillRect(snake.get(i).x * size, snake.get(i).y * size, size, size);
        }

        if (food.x != -1) {
            g.setColor(Color.YELLOW);
            g.fillRect(food.x * size, food.y * size, size, size);
        }
    }
}