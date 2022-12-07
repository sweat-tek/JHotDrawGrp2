package org.jhotdraw.samples.bridge;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.geom.Geom;
import org.jhotdraw.samples.SPI.Rectangle;
import org.jhotdraw.samples.svg.Gradient;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import static org.jhotdraw.draw.AttributeKeys.*;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT;

public class RectBridge {

    public Shape getTransformedShape(Shape cachedTransformedShape, RoundRectangle2D.Double roundrect, Rectangle rectangle, Figure figure) {
        if (cachedTransformedShape == null) {
            if (rectangle.getArcHeight() != 0 || rectangle.getArcWidth() != 0) {
                return (Shape) roundrect.clone();
            }
            cachedTransformedShape = roundrect.getBounds2D();

            if (figure.get(TRANSFORM) != null) {
                cachedTransformedShape = figure.get(TRANSFORM).createTransformedShape(cachedTransformedShape);
            }
        }
        return cachedTransformedShape;
    }

    public void restoreTransformTo(Object geometry, Rectangle rectangle, Figure figure) {
        rectangle.invalidateTransformedShape();
        Object[] restoreData = (Object[]) geometry;
        rectangle.setRoundrect((RoundRectangle2D.Double) ((RoundRectangle2D.Double) restoreData[0]).clone());
        TRANSFORM.setClone(figure, (AffineTransform) restoreData[1]);
        FILL_GRADIENT.setClone(figure, (Gradient) restoreData[2]);
        STROKE_GRADIENT.setClone(figure, (Gradient) restoreData[3]);
    }

    public Rectangle2D.Double getDrawingArea(Rectangle rectangle, Figure figure, double hitGrowth) {
        Rectangle2D rx = rectangle.getTransformedShape().getBounds2D();
        Rectangle2D.Double r = (rx instanceof Rectangle2D.Double) ? (Rectangle2D.Double) rx : new Rectangle2D.Double(rx.getX(), rx.getY(), rx.getWidth(), rx.getHeight());

        if (figure.get(TRANSFORM) != null) {
            double strokeTotalWidth = AttributeKeys.getStrokeTotalWidth(figure, 1.0);
            double width = strokeTotalWidth / 2d;
            if (figure.get(STROKE_JOIN) == BasicStroke.JOIN_MITER) {
                width *= figure.get(STROKE_MITER_LIMIT);
            }
            if (figure.get(STROKE_CAP) != BasicStroke.CAP_BUTT) {
                width += strokeTotalWidth * 2;
            }
            width++;
            Geom.grow(r, width, width);
        }
        double g = hitGrowth;
        Geom.grow(r, g, g);

        return r;
    }

    public void transform(AffineTransform tx, Rectangle rectangle, Figure figure) {
        rectangle.invalidateTransformedShape();

        if (figure.get(TRANSFORM) == null || (tx.getType() & (AffineTransform.TYPE_TRANSLATION)) == tx.getType()) {
            Point2D.Double anchor = figure.getStartPoint();
            Point2D.Double lead = figure.getEndPoint();

            figure.setBounds(
                    (Point2D.Double) tx.transform(anchor, anchor),
                    (Point2D.Double) tx.transform(lead, lead));

            if (figure.get(FILL_GRADIENT) != null
                    && !figure.get(FILL_GRADIENT).isRelativeToFigureBounds()) {
                this.gradientTransform(FILL_GRADIENT, tx, rectangle, figure);
            }

            if (figure.get(STROKE_GRADIENT) != null
                    && !figure.get(STROKE_GRADIENT).isRelativeToFigureBounds()) {
                this.gradientTransform(STROKE_GRADIENT, tx, rectangle, figure);
            }

            if (figure.get(TRANSFORM) != null) {
                AffineTransform t = TRANSFORM.getClone(figure);
                t.preConcatenate(tx);
                figure.set(TRANSFORM, t);
            }

            figure.set(TRANSFORM, (AffineTransform) tx.clone());
        }
    }

    private void gradientTransform(AttributeKey<Gradient> gradient, AffineTransform tx, Rectangle rectangle, Figure figure){
        Gradient g = gradient.getClone(figure);
        g.transform(tx);
        figure.set(gradient, g);
    }

}
