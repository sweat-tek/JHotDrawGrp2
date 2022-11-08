package org.jhotdraw.samples.adapter;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public interface RectImageAdapter {

    void setBounds(Point2D.Double anchor, Point2D.Double lead);

    void invalidateTransformedShape();

}
