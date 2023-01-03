package org.jhotdraw.samples.SPI;

import org.jhotdraw.draw.figure.Figure;

import java.awt.geom.Point2D;

public interface RectImage extends Figure {

    void setBounds(Point2D.Double anchor, Point2D.Double lead);

    void invalidateTransformedShape();

}
