package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextLayout;
import java.util.ArrayList;

public class SnakeGame extends JPanel implements ActionListener {
    private final int width;
    private final int height;
    private final int cellSize;
    private static final int FRAME_RATE = 20;
    private boolean gameStarted = false;
    private boolean gameOver = false;
    private Direction direction = Direction.LEFT;
    private Direction newDirection = Direction.RIGHT;

    private final ArrayList<GamePoint> snake = new ArrayList<>();

    public SnakeGame(final int width, final int height) {
        super();
        this.width = width;
        this.height = height;
        this.cellSize = width / (FRAME_RATE * 2);
        setPreferredSize(new Dimension(width, height));
        // RGB values for #00643a
        int red = 0;
        int green = 100;
        int blue = 58;

        // Convert RGB to HSB
        float[] hsbValues = Color.RGBtoHSB(red, green, blue, null);

        // Create Color object using HSB values
        Color backgroundColor = Color.getHSBColor(hsbValues[0], hsbValues[1], hsbValues[2]);

        setBackground(backgroundColor);
    }

    public void startGame() {
        resetGame();
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                handleKeyEvent(e.getKeyCode());
            }
        });

        new Timer(1000 / FRAME_RATE, this).start();
    }

    private void handleKeyEvent(final int keyCode) {
        if(!gameStarted) {
            if(keyCode == KeyEvent.VK_SPACE){
                gameStarted = true;
            }
        } else if(!gameOver) {
            switch (keyCode) {
                case KeyEvent.VK_UP:
                    newDirection = Direction.UP;
                    break;
                case KeyEvent.VK_DOWN:
                    newDirection = Direction.DOWN;
                    break;
                case KeyEvent.VK_RIGHT:
                    newDirection = Direction.RIGHT;
                    break;
                case KeyEvent.VK_LEFT:
                    newDirection = Direction.LEFT;
                    break;
            }
        }

    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if(!gameStarted) {
            graphics.setColor(Color.ORANGE);
            graphics.setFont(graphics.getFont().deriveFont(30F));
            int currentHeight = height / 4;
            final var graphics2D = (Graphics2D) graphics;
            final var frc = graphics2D.getFontRenderContext();
            final String message = "Press Space To\nStart The Game";
            for (final var line : message.split("\n")) {
                final var layout = new TextLayout(line, graphics.getFont(), frc);
                final var bounds = layout.getBounds();
                final var targetWidth = (float) (width - bounds.getWidth()) / 2;
                layout.draw(graphics2D, targetWidth, currentHeight);
                currentHeight += graphics.getFontMetrics().getHeight();
            }
        } else {
            graphics.setColor(Color.MAGENTA);
            for (final var point : snake){
                graphics.fillRect(point.x, point.y, cellSize, cellSize);
            }
        }
    }

    private void resetGame() {
        snake.clear();
        snake.add(new GamePoint(width / 2, height / 2));
    }

    private void move() {
        final GamePoint currentHead = snake.getFirst();
        final GamePoint newHead = new GamePoint(currentHead.x+cellSize, currentHead.y);
        snake.addFirst(newHead);

        if(checkCollision()){
            gameOver = true;
            snake.removeFirst();
        } else {
            snake.removeLast();
        }
    }

    private boolean checkCollision() {
        final GamePoint head = snake.getFirst();
        final var invalidWidth = (head.x < 0) || (head.x >= width);
        final var invalidHeight = (head.y < 0) || (head.y >= height);
        return invalidWidth || invalidHeight;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if(gameStarted && !gameOver) {
            move();
        }
        repaint();
    }

    private record GamePoint(int x, int y){}

    private enum Direction {
        UP, DOWN, RIGHT, LEFT
    }
}
