package org.jhotdraw.samples.bridge;

import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.geom.GrowStroke;
import org.jhotdraw.samples.SPI.Shape;
import org.jhotdraw.samples.svg.SVGAttributeKeys;

import static org.jhotdraw.draw.AttributeKeys.FILL_COLOR;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT;

public class SharedBridge {

    public java.awt.Shape getHitShape(java.awt.Shape cachedHitShape, Shape shape, Figure figure) {
        if (cachedHitShape == null) {
            if (shape.get(FILL_COLOR) != null || shape.get(FILL_GRADIENT) != null) {
                cachedHitShape = new GrowStroke(
                        (float) SVGAttributeKeys.getStrokeTotalWidth(figure, 1.0) / 2f,
                        (float) SVGAttributeKeys.getStrokeTotalMiterLimit(figure, 1.0)).createStrokedShape(shape.getTransformedShape());
            } else {
                cachedHitShape = SVGAttributeKeys.getHitStroke(figure, 1.0).createStrokedShape(shape.getTransformedShape());
            }
        }
        return cachedHitShape;
    }
}
