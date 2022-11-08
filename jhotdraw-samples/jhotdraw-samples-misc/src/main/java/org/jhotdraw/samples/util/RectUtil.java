package org.jhotdraw.samples.util;

import org.jhotdraw.samples.adapter.RectangleAdapter;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;

public class RectUtil {

    public Shape getTransformedShape(Shape cachedTransformedShape, RoundRectangle2D.Double roundrect, RectangleAdapter rectangleAdapter) {
        if (cachedTransformedShape == null) {
            if (rectangleAdapter.getArcHeight() == 0 || rectangleAdapter.getArcWidth() == 0) {
                cachedTransformedShape = roundrect.getBounds2D();
            } else {
                cachedTransformedShape = (Shape) roundrect.clone();
            }
            if (rectangleAdapter.get(TRANSFORM) != null) {
                cachedTransformedShape = rectangleAdapter.get(TRANSFORM).createTransformedShape(cachedTransformedShape);
            }
        }
        return cachedTransformedShape;
    }

}
