package org.jhotdraw.samples.svg;


import org.jhotdraw.samples.svg.figures.SVGImageFigure;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class ImageToolTest {

    private SVGImageFigure imageFigure;
    private AffineTransform tx;

    @Before
    public void setUp() {
        imageFigure = new SVGImageFigure();
        tx = new AffineTransform();
        imageFigure.setBounds(new Point2D.Double(0,0), new Point2D.Double(20,20));
    }

    @Test
    public void moveImage(){
        moveImage(10, 10);
        Rectangle2D.Double bounds = imageFigure.getBounds();
        Assert.assertTrue(bounds.x == 10 && bounds.y == 10);
    }

    @Test
    public void moveImageByNegativeDirection(){
        moveImage(-10, -10);
        Rectangle2D.Double bounds = imageFigure.getBounds();
        Assert.assertTrue(bounds.x == -10 && bounds.y == -10);
    }

    private void moveImage(double x, double y) {
        tx.translate(x,y);
        imageFigure.transform(tx);
    }
}
