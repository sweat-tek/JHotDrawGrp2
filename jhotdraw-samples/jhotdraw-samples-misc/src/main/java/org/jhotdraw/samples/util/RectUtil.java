package org.jhotdraw.samples.util;

import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.adapter.RectangleAdapter;
import org.jhotdraw.samples.svg.Gradient;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;

import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT;

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

    public void restoreTransformTo(Object geometry, RectangleAdapter rectangleAdapter, Figure figure) {
        rectangleAdapter.invalidateTransformedShape();
        Object[] restoreData = (Object[]) geometry;
        rectangleAdapter.setRoundrect((RoundRectangle2D.Double) ((RoundRectangle2D.Double) restoreData[0]).clone());
        TRANSFORM.setClone(figure, (AffineTransform) restoreData[1]);
        FILL_GRADIENT.setClone(figure, (Gradient) restoreData[2]);
        STROKE_GRADIENT.setClone(figure, (Gradient) restoreData[3]);
    }

}
