package org.jhotdraw.samples.svg;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.samples.SPI.Rectangle;
import org.jhotdraw.samples.factory.AbstractFigureFactory;
import org.jhotdraw.samples.factory.RectangleFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import static org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT;

public class RectangleUnitTest {

    private Rectangle rectangle;
    private AffineTransform tx;


    @Before
    public void setUp() {
        /**
         * Instantiate a 10x10 rectangle for testing.
         */
        rectangle = RectangleFactory.getInstance().create(AbstractFigureFactory.Type.SVG);
        tx = new AffineTransform();
        rectangle.setBounds(new Point2D.Double(10, 10), new Point2D.Double(10 + 10, 10 + 10));
    }

    @Test
    public void testMovingFigure() {
        moveRectangle(20, 20);
        Rectangle2D.Double bounds = rectangle.getBounds();
        Assert.assertTrue(bounds.x == 30 && bounds.y == 30);
    }

    @Test
    public void testMovingFigureInNegativeDirection() {
        moveRectangle(-20, -20);
        Rectangle2D.Double bounds = rectangle.getBounds();
        Assert.assertTrue(bounds.x == -10 && bounds.y == -10);
    }

    @Test
    public void testFillGradientTransform() {
        double[] stopOffsets = {8, 3, 1};
        Color[] stopColors = {Color.BLACK, Color.BLUE, Color.GREEN};
        double[] stopOpacities = {100.0, 80.0, 50.0};
        LinearGradient linearGradient = new LinearGradient(10, 10, 0, 0, stopOffsets, stopColors, stopOpacities, false, tx);

        Map<AttributeKey<?>, Object> fillGradient = Map.of(FILL_GRADIENT, linearGradient);
        rectangle.setAttributes(fillGradient);
        rectangle.transform(tx);

        Assert.assertTrue(rectangle.get(FILL_GRADIENT) != null);

    }

    private Rectangle moveRectangle(double x, double y) {
        tx.translate(x, y);
        rectangle.transform(tx);
        return rectangle;
    }

}
