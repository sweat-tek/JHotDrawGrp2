/*
 * @(#)SVGEllipse.java
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
import org.jhotdraw.samples.SPI.Ellipse;
import org.jhotdraw.samples.bridge.EllipseBridge;
import org.jhotdraw.samples.svg.bridge.EllipseRectangleBridge;
import org.jhotdraw.samples.svg.SVGAttributeKeys;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.LinkedList;

import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT;

/**
 * SVGEllipse represents a SVG ellipse and a SVG circle element.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */

public class SVGEllipseFigure extends SVGAttributedFigure implements SVGFigure, Ellipse {

    private static final long serialVersionUID = 1L;
    private Ellipse2D.Double ellipse;
    /**
     * This is used to perform faster drawing and hit testing.
     */
    private transient java.awt.Shape cachedTransformedShape;
    /**
     * This is used to perform faster hit testing.
     */
    private transient java.awt.Shape cachedHitShape;
    private final EllipseRectangleBridge ellipseRectangleBridge;

    private final EllipseBridge ellipseBridge;

    /**
     * Creates a new instance.
     */
    public SVGEllipseFigure() {
        this(0, 0, 0, 0);
    }

    @FeatureEntryPoint("EllipseConstructor")
    public SVGEllipseFigure(double x, double y, double width, double height) {
        ellipse = new Ellipse2D.Double(x, y, width, height);
        this.ellipseRectangleBridge = new EllipseRectangleBridge();
        this.ellipseBridge = new EllipseBridge();
        SVGAttributeKeys.setDefaults(this);
        setConnectable(false);
    }

    // DRAWING
    @Override
    protected void drawFill(Graphics2D g) {
        if (ellipse.width > 0 && ellipse.height > 0) {
            g.fill(ellipse);
        }
    }

    @Override
    protected void drawStroke(Graphics2D g) {
        if (ellipse.width > 0 && ellipse.height > 0) {
            g.draw(ellipse);
        }
    }

    // SHAPE AND BOUNDS
    public double getX() {
        return ellipse.x;
    }

    public double getY() {
        return ellipse.y;
    }

    public double getWidth() {
        return ellipse.getWidth();
    }

    public double getHeight() {
        return ellipse.getHeight();
    }

    @Override
    public Rectangle2D.Double getBounds() {
        return (Rectangle2D.Double) ellipse.getBounds2D();
    }

    @Override
    public Rectangle2D.Double getDrawingArea() {
        Rectangle2D rx = getTransformedShape().getBounds2D();
        Rectangle2D.Double r = (rx instanceof Rectangle2D.Double) ? (Rectangle2D.Double) rx : new Rectangle2D.Double(rx.getX(), rx.getY(), rx.getWidth(), rx.getHeight());
        if (get(TRANSFORM) == null) {
            double g = SVGAttributeKeys.getPerpendicularHitGrowth(this, 1.0) * 2d + 1;
            Geom.grow(r, g, g);
        } else {
            double strokeTotalWidth = AttributeKeys.getStrokeTotalWidth(this, 1.0);
            double width = strokeTotalWidth / 2d;
            width *= Math.max(get(TRANSFORM).getScaleX(), get(TRANSFORM).getScaleY()) + 1;
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

    public Shape getTransformedShape() {
        if (cachedTransformedShape == null) {
            if (get(TRANSFORM) == null) {
                cachedTransformedShape = ellipse;
            } else {
                cachedTransformedShape = get(TRANSFORM).createTransformedShape(ellipse);
            }
        }
        return cachedTransformedShape;
    }

    private Shape getHitShape() {
        return ellipseRectangleBridge.getHitShape(cachedHitShape, this, this);
    }

    @Override
    public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
        ellipse.x = Math.min(anchor.x, lead.x);
        ellipse.y = Math.min(anchor.y, lead.y);
        ellipse.width = Math.max(0.1, Math.abs(lead.x - anchor.x));
        ellipse.height = Math.max(0.1, Math.abs(lead.y - anchor.y));
        invalidate();
    }

    /**
     * Transforms the figure.
     *
     * @param tx the transformation.
     */
    @Override
    @FeatureEntryPoint("EllipseTransform")
    public void transform(AffineTransform tx) {
        ellipseBridge.transform(tx, this, this);
    }

    @Override
    public void restoreTransformTo(Object geometry) {
        ellipseBridge.restoreTransformTo(geometry, this, ellipse);
    }

    @Override
    public Object getTransformRestoreData() {
        return new Object[]{
                ellipse.clone(),
                TRANSFORM.getClone(this),
                FILL_GRADIENT.getClone(this),
                STROKE_GRADIENT.getClone(this)};
    }

    // ATTRIBUTES
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

    // CONNECTING
    // COMPOSITE FIGURES
    // CLONING
    @Override
    public SVGEllipseFigure clone() {
        SVGEllipseFigure that = (SVGEllipseFigure) super.clone();
        that.ellipse = (Ellipse2D.Double) this.ellipse.clone();
        that.cachedTransformedShape = null;
        return that;
    }

    // EVENT HANDLING
    @Override
    public boolean isEmpty() {
        Rectangle2D.Double b = getBounds();
        return b.width <= 0 || b.height <= 0;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        cachedTransformedShape = null;
        cachedHitShape = null;
    }
}
