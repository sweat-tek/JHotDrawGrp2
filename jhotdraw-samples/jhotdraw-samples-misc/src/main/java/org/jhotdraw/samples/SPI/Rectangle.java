package org.jhotdraw.samples.SPI;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.geom.Geom;
import org.jhotdraw.samples.svg.Gradient;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import static org.jhotdraw.draw.AttributeKeys.*;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT;

public interface Rectangle extends EllipseRectangle {

    double getArcWidth();

    double getArcHeight();

    void invalidateTransformedShape();

    void setRoundrect(RoundRectangle2D.Double roundrect);

    void setArc(double width, double height);

    default Shape getTransformedShape(Shape cachedTransformedShape, RoundRectangle2D.Double roundrect) {
        if (cachedTransformedShape == null) {
            if (this.getArcHeight() != 0 || this.getArcWidth() != 0) {
                return (Shape) roundrect.clone();
            }
            cachedTransformedShape = roundrect.getBounds2D();

            if (this.get(TRANSFORM) != null) {
                cachedTransformedShape = this.get(TRANSFORM).createTransformedShape(cachedTransformedShape);
            }
        }
        return cachedTransformedShape;
    }

    default void restoreTransformTo(Object geometry) {
        this.invalidateTransformedShape();
        Object[] restoreData = (Object[]) geometry;
        this.setRoundrect((RoundRectangle2D.Double) ((RoundRectangle2D.Double) restoreData[0]).clone());
        TRANSFORM.setClone(this, (AffineTransform) restoreData[1]);
        FILL_GRADIENT.setClone(this, (Gradient) restoreData[2]);
        STROKE_GRADIENT.setClone(this, (Gradient) restoreData[3]);
    }

    default Rectangle2D.Double getDrawingArea(double hitGrowth) {
        Rectangle2D rx = this.getTransformedShape().getBounds2D();
        Rectangle2D.Double r = (rx instanceof Rectangle2D.Double) ? (Rectangle2D.Double) rx : new Rectangle2D.Double(rx.getX(), rx.getY(), rx.getWidth(), rx.getHeight());

        if (this.get(TRANSFORM) != null) {
            double strokeTotalWidth = AttributeKeys.getStrokeTotalWidth(this, 1.0);
            double width = strokeTotalWidth / 2d;
            if (this.get(STROKE_JOIN) == BasicStroke.JOIN_MITER) {
                width *= this.get(STROKE_MITER_LIMIT);
            }
            if (this.get(STROKE_CAP) != BasicStroke.CAP_BUTT) {
                width += strokeTotalWidth * 2;
            }
            width++;
            Geom.grow(r, width, width);

            return r;
        }

        double g = hitGrowth;
        Geom.grow(r, g, g);

        return r;
    }

    /**
     * Transforms the figure.
     *
     * @param tx The transformation.
     */
    default void transform(AffineTransform tx) {
        this.invalidateTransformedShape();

        if (this.get(TRANSFORM) == null || (tx.getType() & (AffineTransform.TYPE_TRANSLATION)) == tx.getType()) {
            Point2D.Double anchor = this.getStartPoint();
            Point2D.Double lead = this.getEndPoint();

            this.setBounds(
                    (Point2D.Double) tx.transform(anchor, anchor),
                    (Point2D.Double) tx.transform(lead, lead));

            this.gradientTransform(FILL_GRADIENT, tx);

            this.gradientTransform(STROKE_GRADIENT, tx);

            if (this.get(TRANSFORM) != null) {
                AffineTransform t = TRANSFORM.getClone(this);
                t.preConcatenate(tx);
                this.set(TRANSFORM, t);
            }

            this.set(TRANSFORM, (AffineTransform) tx.clone());
        }
    }

    private void gradientTransform(AttributeKey<Gradient> gradient, AffineTransform tx) {
        if (this.get(gradient) != null && !this.get(gradient).isRelativeToFigureBounds()) {
            Gradient g = gradient.getClone(this);
            g.transform(tx);
            this.set(gradient, g);
        }
    }
}
