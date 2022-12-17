package org.jhotdraw.samples.factory;

import org.jhotdraw.samples.SPI.Rectangle;
import org.jhotdraw.samples.odg.figures.ODGRectFigure;
import org.jhotdraw.samples.svg.figures.SVGRectFigure;

public class RectangleFactory implements AbstractFactory<Rectangle> {

    private static RectangleFactory rectangleFactory;

    private RectangleFactory() {
    }

    public static RectangleFactory getInstance() {
        if (rectangleFactory == null) {
            rectangleFactory = new RectangleFactory();
        }
        return rectangleFactory;
    }

    @Override
    public Rectangle create(String type) {
        if (type.equalsIgnoreCase("SVG")) {
            return new SVGRectFigure();
        }
        else if (type.equalsIgnoreCase("ODG")) {
            return new ODGRectFigure();
        }
        else {
            return null;
        }
    }
}
