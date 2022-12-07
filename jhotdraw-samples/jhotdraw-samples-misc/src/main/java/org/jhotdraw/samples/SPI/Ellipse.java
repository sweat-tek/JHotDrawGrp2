package org.jhotdraw.samples.SPI;

import org.jhotdraw.draw.AttributeKey;

import java.awt.geom.Point2D;

public interface Ellipse extends EllipseRectangle {

    public <T> T get(AttributeKey<T> key);

    public <T> void set(AttributeKey<T> key, T newValue);

    public void setBounds(Point2D.Double anchor, Point2D.Double lead);

    public void invalidate();
}
