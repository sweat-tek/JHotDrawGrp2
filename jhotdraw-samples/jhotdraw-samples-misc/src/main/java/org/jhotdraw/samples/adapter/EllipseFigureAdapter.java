package org.jhotdraw.samples.adapter;

import org.jhotdraw.draw.AttributeKey;

import java.awt.geom.Point2D;

public interface EllipseFigureAdapter {

    public <T> T get(AttributeKey<T> key);

    public <T> void set(AttributeKey<T> key, T newValue);

    public void setBounds(Point2D.Double anchor, Point2D.Double lead);

    public void invalidate();
}
