package org.jhotdraw.samples.svg.bridge;

import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.geom.GrowStroke;
import org.jhotdraw.samples.SPI.EllipseRectangle;
import org.jhotdraw.samples.svg.SVGAttributeKeys;

import static org.jhotdraw.draw.AttributeKeys.FILL_COLOR;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT;

public class EllipseRectangleBridge {

    public java.awt.Shape getHitShape(java.awt.Shape cachedHitShape, EllipseRectangle ellipseRectangle, Figure figure) {
        if (cachedHitShape == null) {
            if (figure.get(FILL_COLOR) != null || figure.get(FILL_GRADIENT) != null) {
                cachedHitShape = new GrowStroke(
                        (float) SVGAttributeKeys.getStrokeTotalWidth(figure, 1.0) / 2f,
                        (float) SVGAttributeKeys.getStrokeTotalMiterLimit(figure, 1.0)).createStrokedShape(ellipseRectangle.getTransformedShape());
            } else {
                cachedHitShape = SVGAttributeKeys.getHitStroke(figure, 1.0).createStrokedShape(ellipseRectangle.getTransformedShape());
            }
        }

        return cachedHitShape;
    }
}
