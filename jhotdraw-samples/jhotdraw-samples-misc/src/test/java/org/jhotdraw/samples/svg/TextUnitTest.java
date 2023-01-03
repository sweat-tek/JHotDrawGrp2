package org.jhotdraw.samples.svg;


import org.jhotdraw.samples.svg.figures.SVGTextFigure;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class TextUnitTest {

    private SVGTextFigure textFigure;
    private AffineTransform tx;

    @Before
    public void setUp() {
        textFigure = new SVGTextFigure();
        tx = new AffineTransform();
        textFigure.setBounds(new Point2D.Double(0, 0), new Point2D.Double(20, 20));
    }

    @Test
    public void moveTextFigure(){
        Rectangle2D.Double oldBounds = (Rectangle2D.Double) textFigure.getBounds().clone();
        moveTextFigure(10, 10);
        Rectangle2D.Double newBounds = textFigure.getBounds();
        Assert.assertTrue((newBounds.x - oldBounds.x) == 10 && (newBounds.y - oldBounds.y) == 10);
    }

    @Test
    public void moveTextFigureByNegativeValues(){
        Rectangle2D.Double oldBounds = (Rectangle2D.Double) textFigure.getBounds().clone();
        moveTextFigure(-10, -10);
        Rectangle2D.Double newBounds = textFigure.getBounds();
        Assert.assertTrue((newBounds.x - oldBounds.x) == -10 && (newBounds.y - oldBounds.y) == -10);
    }

    @Test
    public void initializeWithDefaultText(){
        Assert.assertEquals("Text", textFigure.getText());
    }
    @Test
    public void initializeWithGivenText(){
        String sampleText = "Sample given text";
        Assert.assertEquals(sampleText, new SVGTextFigure(sampleText).getText());
    }

    private void moveTextFigure(double x, double y) {
        tx.translate(x, y);
        textFigure.transform(tx);
    }
}
