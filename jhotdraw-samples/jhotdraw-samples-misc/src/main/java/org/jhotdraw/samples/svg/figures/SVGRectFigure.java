/*
 * @(#)SVGRect.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.svg.figures;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.draw.handle.BoundsOutlineHandle;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.handle.ResizeHandleKit;
import org.jhotdraw.draw.handle.TransformHandleKit;
import org.jhotdraw.samples.SPI.RectImage;
import org.jhotdraw.samples.SPI.Rectangle;
import org.jhotdraw.samples.svg.SVGAttributeKeys;
import org.jhotdraw.samples.svg.bridge.EllipseRectangleBridge;
import org.jhotdraw.samples.svg.bridge.RectImageBridge;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Collection;
import java.util.LinkedList;

import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_GRADIENT;

/**
 * SVGRect.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class SVGRectFigure extends SVGAttributedFigure implements SVGFigure, Rectangle, RectImage {

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

    private final EllipseRectangleBridge ellipseRectangleBridge;

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
        this.ellipseRectangleBridge = new EllipseRectangleBridge();
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
        if (roundrect.archeight != 0 && roundrect.arcwidth != 0) {
            // We have to generate the path for the round rectangle manually,
            // because the path of a Java RoundRectangle is drawn counter clockwise
            // whereas an SVG rect needs to be drawn clockwise.
            Path2D.Double p = initDrawStroke();
            g.draw(p);
            return;
        }
        g.draw(roundrect.getBounds2D());
    }

    private Path2D.Double initDrawStroke() {
        Path2D.Double p = new Path2D.Double();
        double aw = roundrect.arcwidth / 2d;
        double ah = roundrect.archeight / 2d;

        return generateStrokeLineAndCurve(p, aw, ah);
    }

    private Path2D.Double generateStrokeLineAndCurve(Path2D.Double p, double aw, double ah) {
        p.moveTo((roundrect.x + aw), (float) roundrect.y);

        p.lineTo((roundrect.x + roundrect.width - aw), (float) roundrect.y);
        p.curveTo((roundrect.x + roundrect.width - aw * ACV), (float) roundrect.y,
                (roundrect.x + roundrect.width), (float) (roundrect.y + ah * ACV),
                (roundrect.x + roundrect.width), (roundrect.y + ah));

        p.lineTo((roundrect.x + roundrect.width), (roundrect.y + roundrect.height - ah));
        p.curveTo(
                (roundrect.x + roundrect.width), (roundrect.y + roundrect.height - ah * ACV),
                (roundrect.x + roundrect.width - aw * ACV), (roundrect.y + roundrect.height),
                (roundrect.x + roundrect.width - aw), (roundrect.y + roundrect.height));

        p.lineTo((roundrect.x + aw), (roundrect.y + roundrect.height));
        p.curveTo((roundrect.x + aw * ACV), (roundrect.y + roundrect.height),
                (roundrect.x), (roundrect.y + roundrect.height - ah * ACV),
                (float) roundrect.x, (roundrect.y + roundrect.height - ah));

        p.lineTo((float) roundrect.x, (roundrect.y + ah));
        p.curveTo((roundrect.x), (roundrect.y + ah * ACV),
                (roundrect.x + aw * ACV), (float) (roundrect.y),
                (float) (roundrect.x + aw), (float) (roundrect.y));

        p.closePath();
        return p;
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
        double hitGrowth = SVGAttributeKeys.getPerpendicularHitGrowth(this, 1.0) * 2d + 1d;
        return getDrawingArea(hitGrowth);
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

    private Shape getHitShape() {
        return ellipseRectangleBridge.getHitShape(cachedHitShape, this, this);
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
        LinkedList<Handle> handles = new LinkedList<>();
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

    @Override
    public Shape getTransformedShape() {
        return this.getTransformedShape(cachedTransformedShape, roundrect);
    }

}
