package org.jhotdraw.samples.SPI;

import java.awt.geom.RoundRectangle2D;

public interface Rectangle extends EllipseRectangle {

    double getX();

    double getY();

    double getWidth();

    double getHeight();

    double getArcWidth();

    double getArcHeight();

    void invalidateTransformedShape();

    void setRoundrect(RoundRectangle2D.Double roundrect);

}
