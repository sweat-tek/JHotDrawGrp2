package org.jhotdraw.samples.svg;


import org.jhotdraw.samples.SPI.Ellipse;
import org.jhotdraw.samples.svg.figures.SVGEllipseFigure;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class EllipseUnitTest {

    private Ellipse ellipse;
    private AffineTransform tx;


    //Creating an ellipse at (0,0) with width 20 and height 20
    @Before
    public void setUp() {
        /**
         * Instantiate a 10x10 rectangle for testing.
         */
        ellipse = new SVGEllipseFigure();
        tx = new AffineTransform();
        ellipse.setBounds(new Point2D.Double(0,0),new Point2D.Double(20,20));
    }

    @Test
    public void moveEllipse(){
        moveEllipse(10,10);
        Rectangle2D.Double bounds = ellipse.getBounds();
        Assert.assertTrue(bounds.x == 10 && bounds.y == 10);
    }

    @Test
    public void moveEllipseByNegativeValues(){
        moveEllipse(-10,-10);
        Rectangle2D.Double bounds = ellipse.getBounds();
        Assert.assertTrue(bounds.x == -10 && bounds.y == -10);
    }

    private void moveEllipse(double x,double y) {
        tx.translate(x,y);
        ellipse.transform(tx);
    }


}
