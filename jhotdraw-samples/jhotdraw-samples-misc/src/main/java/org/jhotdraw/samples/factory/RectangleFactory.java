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
        switch (type) {
            case "SVG":
                return new SVGRectFigure();
            case "ODG":
                return new ODGRectFigure();
            default:
                throw new RuntimeException("No rectangle of type: " + type);
        }
    }
}
