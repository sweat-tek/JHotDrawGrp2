package org.jhotdraw.samples.util;

import org.jhotdraw.draw.figure.AbstractFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.adapter.EllipseFigureAdapter;
import org.jhotdraw.samples.svg.Gradient;

import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT;

public class EllipseUtil {

    public void transform(AffineTransform tx, EllipseFigureAdapter ellipseFigure, AbstractFigure abstractFigure){
        if (ellipseFigure.get(TRANSFORM) != null
                || (tx.getType() & (AffineTransform.TYPE_TRANSLATION)) != tx.getType()) {
            if (ellipseFigure.get(TRANSFORM) == null) {
                TRANSFORM.setClone((Figure) ellipseFigure, tx);
            } else {
                AffineTransform t = TRANSFORM.getClone((Figure) ellipseFigure);
                t.preConcatenate(tx);
                ellipseFigure.set(TRANSFORM, t);
            }
        } else {
            Point2D.Double anchor = abstractFigure.getStartPoint();
            Point2D.Double lead = abstractFigure.getEndPoint();
            ellipseFigure.setBounds(
                    (Point2D.Double) tx.transform(anchor, anchor),
                    (Point2D.Double) tx.transform(lead, lead));
            if (ellipseFigure.get(FILL_GRADIENT) != null
                    && !ellipseFigure.get(FILL_GRADIENT).isRelativeToFigureBounds()) {
                Gradient g = FILL_GRADIENT.getClone((Figure) ellipseFigure);
                g.transform(tx);
                ellipseFigure.set(FILL_GRADIENT, g);
            }
            if (ellipseFigure.get(STROKE_GRADIENT) != null
                    && !ellipseFigure.get(STROKE_GRADIENT).isRelativeToFigureBounds()) {
                Gradient g = STROKE_GRADIENT.getClone((Figure) ellipseFigure);
                g.transform(tx);
                ellipseFigure.set(STROKE_GRADIENT, g);
            }
        }
        ellipseFigure.invalidate();
    }

    public void restoreTransformTo(Object geometry, EllipseFigureAdapter ellipseFigureAdapter, Ellipse2D.Double ellipse) {
        Object[] restoreData = (Object[]) geometry;
        ellipse = (Ellipse2D.Double) ((Ellipse2D.Double) restoreData[0]).clone();
        TRANSFORM.setClone((Figure) ellipseFigureAdapter, (AffineTransform) restoreData[1]);
        FILL_GRADIENT.setClone((Figure) ellipseFigureAdapter, (Gradient) restoreData[2]);
        STROKE_GRADIENT.setClone((Figure) ellipseFigureAdapter, (Gradient) restoreData[3]);
        ellipseFigureAdapter.invalidate();
    }
}
