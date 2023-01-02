package org.jhotdraw.samples.svg.figures.drawing;

import java.awt.*;

public class EllipseShapeRenderer {

    public void drawFill(Graphics2D g, Shape shape) {
        if (shape.getBounds().getWidth() > 0 && shape.getBounds().getHeight() > 0) {
            g.fill(shape);
        }
    }

    public void drawStroke(Graphics2D g, Shape shape) {
        if (shape.getBounds().getWidth() > 0 && shape.getBounds().getHeight() > 0) {
            g.draw(shape);
        }
    }
}
