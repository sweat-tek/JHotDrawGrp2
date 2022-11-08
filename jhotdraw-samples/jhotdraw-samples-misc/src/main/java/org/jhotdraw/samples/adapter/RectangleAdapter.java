package org.jhotdraw.samples.adapter;

import java.awt.geom.RoundRectangle2D;

public interface RectangleAdapter extends SharedAdapter {

    double getX();

    double getY();

    double getWidth();

    double getHeight();

    double getArcWidth();

    double getArcHeight();

    void invalidateTransformedShape();

    void setRoundrect(RoundRectangle2D.Double roundrect);

}
