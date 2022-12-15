package org.jhotdraw.samples.factory;

import org.jhotdraw.samples.SPI.Rectangle;
import org.jhotdraw.samples.odg.figures.ODGRectFigure;
import org.jhotdraw.samples.svg.figures.SVGRectFigure;

public class RectangleFactory implements AbstractFactory<Rectangle> {

    @Override
    public Rectangle create(String type) {
        if (type.equalsIgnoreCase("SVG")) return new SVGRectFigure();
        else if (type.equalsIgnoreCase("ODF")) return new ODGRectFigure();
        else return null;
    }
}
