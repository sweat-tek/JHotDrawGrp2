package org.jhotdraw.samples.bridge;

import org.jhotdraw.draw.figure.AbstractFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.SPI.Ellipse;
import org.jhotdraw.samples.svg.Gradient;

import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT;

public class EllipseBridge {

    public void transform(AffineTransform tx, Ellipse ellipseFigure, AbstractFigure abstractFigure) {
        if (shouldTransformEllipseFigure(ellipseFigure, tx)) {
            transformEllipseFigure(ellipseFigure, tx);
        } else {
            setEllipseFigureBounds(ellipseFigure, abstractFigure, tx);
            transformFillGradient(ellipseFigure, tx);
            transformStrokeGradient(ellipseFigure, tx);
        }
        ellipseFigure.invalidate();
    }

    private boolean shouldTransformEllipseFigure(Ellipse ellipseFigure, AffineTransform tx) {
        return ellipseFigure.get(TRANSFORM) != null
                || (tx.getType() & (AffineTransform.TYPE_TRANSLATION)) != tx.getType();
    }

    private void transformEllipseFigure(Ellipse ellipseFigure, AffineTransform tx) {
        if (ellipseFigure.get(TRANSFORM) == null) {
            TRANSFORM.setClone(ellipseFigure, tx);
        } else {
            AffineTransform t = TRANSFORM.getClone(ellipseFigure);
            t.preConcatenate(tx);
            ellipseFigure.set(TRANSFORM, t);
        }
    }

    private void setEllipseFigureBounds(Ellipse ellipseFigure, AbstractFigure abstractFigure, AffineTransform tx) {
        Point2D.Double anchor = abstractFigure.getStartPoint();
        Point2D.Double lead = abstractFigure.getEndPoint();
        ellipseFigure.setBounds(
                (Point2D.Double) tx.transform(anchor, anchor),
                (Point2D.Double) tx.transform(lead, lead));
    }

    private void transformFillGradient(Ellipse ellipseFigure, AffineTransform tx) {
        if (ellipseFigure.get(FILL_GRADIENT) != null
                && !ellipseFigure.get(FILL_GRADIENT).isRelativeToFigureBounds()) {
            Gradient g = FILL_GRADIENT.getClone(ellipseFigure);
            g.transform(tx);
            ellipseFigure.set(FILL_GRADIENT, g);
        }
    }

    private void transformStrokeGradient(Ellipse ellipseFigure, AffineTransform tx) {
        if (ellipseFigure.get(STROKE_GRADIENT) != null
                && !ellipseFigure.get(STROKE_GRADIENT).isRelativeToFigureBounds()) {
            Gradient g = STROKE_GRADIENT.getClone(ellipseFigure);
            g.transform(tx);
            ellipseFigure.set(STROKE_GRADIENT, g);
        }
    }


    public void restoreTransformTo(Object geometry, org.jhotdraw.samples.SPI.Ellipse ellipseFigureAdapter, Ellipse2D.Double ellipse) {
        Object[] restoreData = (Object[]) geometry;
        ellipse = (Ellipse2D.Double) ((Ellipse2D.Double) restoreData[0]).clone();
        TRANSFORM.setClone((Figure) ellipseFigureAdapter, (AffineTransform) restoreData[1]);
        FILL_GRADIENT.setClone((Figure) ellipseFigureAdapter, (Gradient) restoreData[2]);
        STROKE_GRADIENT.setClone((Figure) ellipseFigureAdapter, (Gradient) restoreData[3]);
        ellipseFigureAdapter.invalidate();
    }
}
