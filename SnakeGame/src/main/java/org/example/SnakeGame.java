package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextLayout;

public class SnakeGame extends JPanel {
    private final int width;
    private final int height;

    public SnakeGame(final int width, final int height) {
        super();
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
    }

    public void startGame() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.setColor(Color.green);
        graphics.setFont(graphics.getFont().deriveFont(30F));
        int currentHeight = height / 4;
        final var graphics2D = (Graphics2D) graphics;
        final var frc = graphics2D.getFontRenderContext();
        final String message = "This is a\nmulti-line message";
        for(final var line : message.split("\n")) {
            final var layout = new TextLayout(line, graphics.getFont(), frc);
            final var bounds = layout.getBounds();
            final var targetWidth = (float) (width - bounds.getWidth()) / 2;
            layout.draw(graphics2D, targetWidth, currentHeight);
            currentHeight += graphics.getFontMetrics().getHeight();
        }

    }
}
