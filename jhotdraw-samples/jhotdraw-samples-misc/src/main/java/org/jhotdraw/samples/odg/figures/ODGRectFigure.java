/*
 * @(#)ODGRect.java
 *
 * Copyright (c) 2007 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.odg.figures;

import org.jhotdraw.draw.connector.Connector;
import org.jhotdraw.draw.figure.ConnectionFigure;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.handle.ResizeHandleKit;
import org.jhotdraw.draw.handle.TransformHandleKit;
import org.jhotdraw.geom.Dimension2DDouble;
import org.jhotdraw.geom.GrowStroke;
import org.jhotdraw.samples.SPI.RectImage;
import org.jhotdraw.samples.SPI.Rectangle;
import org.jhotdraw.samples.odg.ODGAttributeKeys;
import org.jhotdraw.samples.bridge.RectImageBridge;
import org.jhotdraw.samples.bridge.RectBridge;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Collection;
import java.util.LinkedList;

import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;
import static org.jhotdraw.samples.odg.ODGAttributeKeys.FILL_GRADIENT;
import static org.jhotdraw.samples.odg.ODGAttributeKeys.STROKE_GRADIENT;

/**
 * ODGRect.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class ODGRectFigure extends ODGAttributedFigure implements ODGFigure, Rectangle, RectImage {

    private static final long serialVersionUID = 1L;
    private RoundRectangle2D.Double roundrect;

    @Override
    public void setRoundrect(RoundRectangle2D.Double roundrect) {
        this.roundrect = roundrect;
    }

    /**
     * This is used to perform faster drawing.
     */
    private transient Shape cachedTransformedShape;
    /**
     * This is used to perform faster hit testing.
     */
    private transient Shape cachedHitShape;

    private final RectBridge rectBridge;

    /**
     * Creates a new instance.
     */
    public ODGRectFigure() {
        this(0, 0, 0, 0);
    }

    public ODGRectFigure(double x, double y, double width, double height) {
        this(x, y, width, height, 0, 0);
    }

    public ODGRectFigure(double x, double y, double width, double height, double rx, double ry) {
        roundrect = new RoundRectangle2D.Double(x, y, width, height, rx, ry);
        this.rectBridge = new RectBridge();
        ODGAttributeKeys.setDefaults(this);
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
        if (getArcHeight() == 0d && getArcWidth() == 0d) {
            g.draw(roundrect.getBounds2D());
        } else {
            g.draw(roundrect);
        }
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

    public double getArcWidth() {
        return roundrect.arcwidth / 2d;
    }

    public double getArcHeight() {
        return roundrect.archeight / 2d;
    }

    @Override
    public Rectangle2D.Double getBounds() {
        return (Rectangle2D.Double) roundrect.getBounds2D();
    }

    @Override
    public Rectangle2D.Double getDrawingArea() {
        double hitGrowth = ODGAttributeKeys.getPerpendicularHitGrowth(this, 1.0) * 2;
        return rectBridge.getDrawingArea(this, this, hitGrowth);
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
        RectImageBridge imageUtil = new RectImageBridge();
        imageUtil.setBounds(anchor, lead, this, this.roundrect);
    }

    public void invalidateTransformedShape() {
        cachedTransformedShape = null;
        cachedHitShape = null;
    }

    public Shape getTransformedShape() {
        return rectBridge.getTransformedShape(cachedTransformedShape, roundrect, this);
    }

    private Shape getHitShape() {
        if (cachedHitShape == null) {
            cachedHitShape = new GrowStroke(
                    (float) ODGAttributeKeys.getStrokeTotalWidth(this, 1.0) / 2f,
                    (float) ODGAttributeKeys.getStrokeTotalMiterLimit(this, 1.0)).createStrokedShape(getTransformedShape());
        }
        return cachedHitShape;
    }

    /**
     * Transforms the figure.
     *
     * @param tx The transformation.
     */
    @Override
    public void transform(AffineTransform tx) {
        rectBridge.transform(tx, this, this);
    }

    // ATTRIBUTES
    public void setArc(double w, double h) {
        roundrect.arcwidth = Math.max(0d, Math.min(roundrect.width, w * 2d));
        roundrect.archeight = Math.max(0d, Math.min(roundrect.height, h * 2d));
    }

    public void setArc(Dimension2DDouble arc) {
        roundrect.arcwidth = Math.max(0d, Math.min(roundrect.width, arc.width * 2d));
        roundrect.archeight = Math.max(0d, Math.min(roundrect.height, arc.height * 2d));
    }

    public Dimension2DDouble getArc() {
        return new Dimension2DDouble(
                roundrect.arcwidth / 2d,
                roundrect.archeight / 2d);
    }

    @Override
    public void restoreTransformTo(Object geometry) {
        rectBridge.restoreTransformTo(geometry, this, this);
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
            case 0:
                ResizeHandleKit.addResizeHandles(this, handles);
                handles.add(new ODGRectRadiusHandle(this));
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
    @Override
    public Connector findConnector(Point2D.Double p, ConnectionFigure prototype) {
        return null; // ODG does not support connectors
    }

    @Override
    public Connector findCompatibleConnector(Connector c, boolean isStartConnector) {
        return null; // ODG does not support connectors
    }

    // COMPOSITE FIGURES
    // CLONING
    @Override
    public ODGRectFigure clone() {
        ODGRectFigure that = (ODGRectFigure) super.clone();
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
}
