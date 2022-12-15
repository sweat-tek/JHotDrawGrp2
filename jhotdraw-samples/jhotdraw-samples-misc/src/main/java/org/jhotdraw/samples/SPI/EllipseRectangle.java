package org.jhotdraw.samples.SPI;

import org.jhotdraw.draw.figure.Figure;

import java.awt.*;

public interface EllipseRectangle extends AttributedFigure {
    Shape getTransformedShape();

}
