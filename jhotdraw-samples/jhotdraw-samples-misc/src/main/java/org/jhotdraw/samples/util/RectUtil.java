package org.jhotdraw.samples.util;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.geom.Geom;
import org.jhotdraw.samples.adapter.RectangleAdapter;
import org.jhotdraw.samples.svg.Gradient;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import static org.jhotdraw.draw.AttributeKeys.*;
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

    public Rectangle2D.Double getDrawingArea(RectangleAdapter rectangleAdapter, Figure figure, double hitGrowth) {
        Rectangle2D rx = rectangleAdapter.getTransformedShape().getBounds2D();
        Rectangle2D.Double r = (rx instanceof Rectangle2D.Double) ? (Rectangle2D.Double) rx : new Rectangle2D.Double(rx.getX(), rx.getY(), rx.getWidth(), rx.getHeight());
        if (rectangleAdapter.get(TRANSFORM) == null) {
            double g = hitGrowth;
            Geom.grow(r, g, g);
        } else {
            double strokeTotalWidth = AttributeKeys.getStrokeTotalWidth(figure, 1.0);
            double width = strokeTotalWidth / 2d;
            if (rectangleAdapter.get(STROKE_JOIN) == BasicStroke.JOIN_MITER) {
                width *= rectangleAdapter.get(STROKE_MITER_LIMIT);
            }
            if (rectangleAdapter.get(STROKE_CAP) != BasicStroke.CAP_BUTT) {
                width += strokeTotalWidth * 2;
            }
            width++;
            Geom.grow(r, width, width);
        }
        return r;
    }

    public void transform(AffineTransform tx, RectangleAdapter rectangleAdapter, Figure figure) {
        rectangleAdapter.invalidateTransformedShape();
        if (rectangleAdapter.get(TRANSFORM) != null || (tx.getType() & (AffineTransform.TYPE_TRANSLATION)) != tx.getType()) {
            if (rectangleAdapter.get(TRANSFORM) == null) {
                rectangleAdapter.set(TRANSFORM, (AffineTransform) tx.clone());
            } else {
                AffineTransform t = TRANSFORM.getClone(figure);
                t.preConcatenate(tx);
                rectangleAdapter.set(TRANSFORM, t);
            }
        } else {
            Point2D.Double anchor = figure.getStartPoint();
            Point2D.Double lead = figure.getEndPoint();
            figure.setBounds(
                    (Point2D.Double) tx.transform(anchor, anchor),
                    (Point2D.Double) tx.transform(lead, lead));
            if (rectangleAdapter.get(FILL_GRADIENT) != null
                    && !rectangleAdapter.get(FILL_GRADIENT).isRelativeToFigureBounds()) {
                Gradient g = FILL_GRADIENT.getClone(figure);
                g.transform(tx);
                rectangleAdapter.set(FILL_GRADIENT, g);
            }
            if (rectangleAdapter.get(STROKE_GRADIENT) != null
                    && !rectangleAdapter.get(STROKE_GRADIENT).isRelativeToFigureBounds()) {
                Gradient g = STROKE_GRADIENT.getClone(figure);
                g.transform(tx);
                rectangleAdapter.set(STROKE_GRADIENT, g);
            }
        }
    }
}
