/*
 * @(#)SVGRect.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.svg.figures;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.handle.BoundsOutlineHandle;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.handle.ResizeHandleKit;
import org.jhotdraw.draw.handle.TransformHandleKit;
import org.jhotdraw.geom.Geom;
import org.jhotdraw.samples.adapter.RectangleAdapter;
import org.jhotdraw.samples.svg.Gradient;
import org.jhotdraw.samples.svg.SVGAttributeKeys;
import org.jhotdraw.samples.util.RectUtil;
import org.jhotdraw.samples.util.SharedUtil;

import java.awt.*;
import java.awt.geom.*;
import java.util.Collection;
import java.util.LinkedList;

import static org.jhotdraw.draw.AttributeKeys.*;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT;

/**
 * SVGRect.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class SVGRectFigure extends SVGAttributedFigure implements SVGFigure, RectangleAdapter {

    private static final long serialVersionUID = 1L;
    /**
     * Identifies the {@code arcWidth} JavaBeans property.
     */
    public static final String ARC_WIDTH_PROPERTY = "arcWidth";
    /**
     * Identifies the {@code arcHeight} JavaBeans property.
     */
    public static final String ARC_HEIGHT_PROPERTY = "arcHeight";
    /**
     * The variable acv is used for generating the locations of the control
     * points for the rounded rectangle using path.curveTo.
     */
    private static final double ACV;

    static {
        double angle = Math.PI / 4.0;
        double a = 1.0 - Math.cos(angle);
        double b = Math.tan(angle);
        double c = Math.sqrt(1.0 + b * b) - 1 + a;
        double cv = 4.0 / 3.0 * a * b / c;
        ACV = (1.0 - cv);
    }

    public void setRoundrect(RoundRectangle2D.Double roundrect) {
        this.roundrect = roundrect;
    }

    /**
     *
     */
    private RoundRectangle2D.Double roundrect;
    /**
     * This is used to perform faster drawing.
     */
    private transient Shape cachedTransformedShape;
    /**
     * This is used to perform faster hit testing.
     */
    private transient Shape cachedHitShape;

    private final RectUtil rectUtil;

    private final SharedUtil sharedUtil;

    /**
     * Creates a new instance.
     */
    public SVGRectFigure() {
        this(0, 0, 0, 0);
    }

    public SVGRectFigure(double x, double y, double width, double height) {
        this(x, y, width, height, 0, 0);
    }

    @FeatureEntryPoint(value = "RectangleConstructor")
    public SVGRectFigure(double x, double y, double width, double height, double rx, double ry) {
        roundrect = new RoundRectangle2D.Double(x, y, width, height, rx, ry);
        this.rectUtil = new RectUtil();
        this.sharedUtil = new SharedUtil();
        SVGAttributeKeys.setDefaults(this);
        setConnectable(false);
    }

    // DRAWING
    @Override
    protected void drawFill(Graphics2D g) {
        if (getArcHeight() == 0d && getArcWidth() == 0d) {
            g.fill(roundrect.getBounds2D());
        } else {
            g.fill(roundrect);
        }
    }

    @Override
    protected void drawStroke(Graphics2D g) {
        if (roundrect.archeight == 0 && roundrect.arcwidth == 0) {
            g.draw(roundrect.getBounds2D());
        } else {
            // We have to generate the path for the round rectangle manually,
            // because the path of a Java RoundRectangle is drawn counter clockwise
            // whereas an SVG rect needs to be drawn clockwise.
            Path2D.Double p = initDrawStroke();
            g.draw(p);
        }
    }

    private Path2D.Double initDrawStroke() {
        Path2D.Double p = new Path2D.Double();
        double aw = roundrect.arcwidth / 2d;
        double ah = roundrect.archeight / 2d;

        p.moveTo((roundrect.x + aw), (float) roundrect.y);
        this.generateFirstStrokeLineAndCurve(p, roundrect.x - roundrect.width, roundrect.y, aw, ah);
        this.generateSecondStrokeLineAndCurve(p, roundrect.x - roundrect.width, roundrect.y + roundrect.height, aw, ah);
        this.generateThirdStrokeLineAndCurve(p, roundrect.x, roundrect.y + roundrect.height, aw, ah);
        this.generateFinalStrokeLineAndCurve(p, roundrect.x, roundrect.y, aw, ah);

        p.closePath();
        return p;
    }


    private void generateFirstStrokeLineAndCurve(Path2D.Double p, double xAndWidth, double y, double aw, double ah) {
        p.lineTo(xAndWidth - aw, (float) y);
        p.curveTo((xAndWidth - aw * ACV), (float) y,
                xAndWidth, (float) (y + ah * ACV),
                xAndWidth, (y + ah));
    }

    private void generateSecondStrokeLineAndCurve(Path2D.Double p, double xAndWidth, double yAndHeight, double aw, double ah) {
        p.lineTo(xAndWidth, (yAndHeight - ah));
        p.curveTo((xAndWidth), (float) (yAndHeight - ah * ACV),
                (xAndWidth - aw * ACV), (float) (yAndHeight),
                (xAndWidth - aw), (yAndHeight));
    }

    private void generateThirdStrokeLineAndCurve(Path2D.Double p, double x, double yAndHeight, double aw, double ah) {
        p.lineTo((x + aw), (yAndHeight));
        p.curveTo((x + aw * ACV), (yAndHeight),
                (x), (float) (yAndHeight - ah * ACV),
                (float) (x), (yAndHeight - ah));
    }

    private void generateFinalStrokeLineAndCurve(Path2D.Double p, double x, double y, double aw, double ah) {
        p.lineTo((float) x, (y + ah));
        p.curveTo((x), (y + ah * ACV),
                (x + aw * ACV), (float) (y),
                (float) (x + aw), (float) (y));

        p.lineTo((float) roundrect.x, (roundrect.y + ah));
        p.curveTo((roundrect.x), (roundrect.y + ah * ACV),
                (roundrect.x + aw * ACV), (float) (roundrect.y),
                (float) (roundrect.x + aw), (float) (roundrect.y));
    }


    // SHAPE AND BOUNDS
    public double getX() {
        return roundrect.x;
    }

    public double getY() {
        return roundrect.y;
    }

    public double getWidth() {
        return roundrect.width;
    }

    public double getHeight() {
        return roundrect.height;
    }

    /**
     * Gets the arc width.
     */
    public double getArcWidth() {
        return roundrect.arcwidth;
    }

    /**
     * Gets the arc height.
     */
    public double getArcHeight() {
        return roundrect.archeight;
    }

    /**
     * Sets the arc width.
     */
    public void setArcWidth(double newValue) {
        double oldValue = roundrect.arcwidth;
        roundrect.arcwidth = newValue;
        firePropertyChange(ARC_WIDTH_PROPERTY, oldValue, newValue);
    }

    /**
     * Sets the arc height.
     */
    public void setArcHeight(double newValue) {
        double oldValue = roundrect.archeight;
        roundrect.archeight = newValue;
        firePropertyChange(ARC_HEIGHT_PROPERTY, oldValue, newValue);
    }

    /**
     * Convenience method for setting both the arc width and the arc height.
     */
    public void setArc(double width, double height) {
        setArcWidth(width);
        setArcHeight(height);
    }

    @Override
    public Rectangle2D.Double getBounds() {
        return (Rectangle2D.Double) roundrect.getBounds2D();
    }

    @Override
    public Rectangle2D.Double getDrawingArea() {
        Rectangle2D rx = getTransformedShape().getBounds2D();
        Rectangle2D.Double r = (rx instanceof Rectangle2D.Double) ? (Rectangle2D.Double) rx : new Rectangle2D.Double(rx.getX(), rx.getY(), rx.getWidth(), rx.getHeight());
        if (get(TRANSFORM) == null) {
            double g = SVGAttributeKeys.getPerpendicularHitGrowth(this, 1.0) * 2d + 1d;
            Geom.grow(r, g, g);
        } else {
            double strokeTotalWidth = AttributeKeys.getStrokeTotalWidth(this, 1.0);
            double width = strokeTotalWidth / 2d;
            if (get(STROKE_JOIN) == BasicStroke.JOIN_MITER) {
                width *= get(STROKE_MITER_LIMIT);
            }
            if (get(STROKE_CAP) != BasicStroke.CAP_BUTT) {
                width += strokeTotalWidth * 2;
            }
            width++;
            Geom.grow(r, width, width);
        }
        return r;
    }

    /**
     * Checks if a Point2D.Double is inside the figure.
     */
    @Override
    public boolean contains(Point2D.Double p) {
        return getHitShape().contains(p);
    }

    @Override
    public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
        invalidateTransformedShape();
        roundrect.x = Math.min(anchor.x, lead.x);
        roundrect.y = Math.min(anchor.y, lead.y);
        roundrect.width = Math.max(0.1, Math.abs(lead.x - anchor.x));
        roundrect.height = Math.max(0.1, Math.abs(lead.y - anchor.y));
        invalidate();
    }

    public void invalidateTransformedShape() {
        cachedTransformedShape = null;
        cachedHitShape = null;
    }

    public Shape getTransformedShape() {
        return rectUtil.getTransformedShape(cachedTransformedShape, roundrect, this);
    }

    private Shape getHitShape() {
        return sharedUtil.getHitShape(cachedHitShape, this, this);
    }

    /**
     * Transforms the figure.
     *
     * @param tx The transformation.
     */
    @FeatureEntryPoint(value = "RectangleMove")
    @Override
    public void transform(AffineTransform tx) {
        invalidateTransformedShape();
        if (get(TRANSFORM) != null || (tx.getType() & (AffineTransform.TYPE_TRANSLATION)) != tx.getType()) {
            if (get(TRANSFORM) == null) {
                set(TRANSFORM, (AffineTransform) tx.clone());
            } else {
                AffineTransform t = TRANSFORM.getClone(this);
                t.preConcatenate(tx);
                set(TRANSFORM, t);
            }
        } else {
            Point2D.Double anchor = getStartPoint();
            Point2D.Double lead = getEndPoint();
            setBounds(
                    (Point2D.Double) tx.transform(anchor, anchor),
                    (Point2D.Double) tx.transform(lead, lead));
            if (get(FILL_GRADIENT) != null
                    && !get(FILL_GRADIENT).isRelativeToFigureBounds()) {
                Gradient g = FILL_GRADIENT.getClone(this);
                g.transform(tx);
                set(FILL_GRADIENT, g);
            }
            if (get(STROKE_GRADIENT) != null
                    && !get(STROKE_GRADIENT).isRelativeToFigureBounds()) {
                Gradient g = STROKE_GRADIENT.getClone(this);
                g.transform(tx);
                set(STROKE_GRADIENT, g);
            }
        }
    }

    @Override
    public void restoreTransformTo(Object geometry) {
        invalidateTransformedShape();
        Object[] restoreData = (Object[]) geometry;
        roundrect = (RoundRectangle2D.Double) ((RoundRectangle2D.Double) restoreData[0]).clone();
        TRANSFORM.setClone(this, (AffineTransform) restoreData[1]);
        FILL_GRADIENT.setClone(this, (Gradient) restoreData[2]);
        STROKE_GRADIENT.setClone(this, (Gradient) restoreData[3]);
    }

    @Override
    public Object getTransformRestoreData() {
        return new Object[]{
                roundrect.clone(),
                TRANSFORM.getClone(this),
                FILL_GRADIENT.getClone(this),
                STROKE_GRADIENT.getClone(this)};
    }

    // EDITING
    @Override
    public Collection<Handle> createHandles(int detailLevel) {
        LinkedList<Handle> handles = new LinkedList<Handle>();
        switch (detailLevel % 2) {
            case -1: // Mouse hover handles
                handles.add(new BoundsOutlineHandle(this, false, true));
                break;
            case 0:
                ResizeHandleKit.addResizeHandles(this, handles);
                handles.add(new SVGRectRadiusHandle(this));
                handles.add(new LinkHandle(this));
                break;
            case 1:
                TransformHandleKit.addTransformHandles(this, handles);
                break;
            default:
                break;
        }
        return handles;
    }

    // CLONING
    @Override
    public SVGRectFigure clone() {
        SVGRectFigure that = (SVGRectFigure) super.clone();
        that.roundrect = (RoundRectangle2D.Double) this.roundrect.clone();
        that.cachedTransformedShape = null;
        that.cachedHitShape = null;
        return that;
    }

    @Override
    public boolean isEmpty() {
        Rectangle2D.Double b = getBounds();
        return b.width <= 0 || b.height <= 0;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateTransformedShape();
    }

    public static SVGRectFigure newDefaultRectangle() {
        return new SVGRectFigure();
    }
}
