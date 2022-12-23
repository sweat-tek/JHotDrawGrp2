package org.jhotdraw.samples.SPI;

import java.awt.geom.Point2D;

public interface RectImage {

    void setBounds(Point2D.Double anchor, Point2D.Double lead);

    void invalidateTransformedShape();

}
