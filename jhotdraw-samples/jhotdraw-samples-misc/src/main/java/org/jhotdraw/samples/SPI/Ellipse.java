package org.jhotdraw.samples.SPI;

import org.jhotdraw.draw.AttributeKey;

import java.awt.geom.Point2D;

public interface Ellipse extends EllipseRectangle {

    <T> T get(AttributeKey<T> key);

    <T> void set(AttributeKey<T> key, T newValue);

    void setBounds(Point2D.Double anchor, Point2D.Double lead);

    void invalidate();
}
