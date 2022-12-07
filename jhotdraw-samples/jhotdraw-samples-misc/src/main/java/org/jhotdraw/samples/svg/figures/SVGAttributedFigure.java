/*
 * @(#)SVGAttributedFigure.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.svg.figures;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.figure.AbstractAttributedFigure;
import org.jhotdraw.samples.svg.SVGAttributeKeys;
import org.jhotdraw.util.ResourceBundleUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;

import static org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH;
import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.OPACITY;

/**
 * SVGAttributedFigure.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public abstract class SVGAttributedFigure extends AbstractAttributedFigure {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance.
     */
    public SVGAttributedFigure() {
    }

    public void draw(Graphics2D g) {
        double opacity = get(OPACITY);
        opacity = Math.min(Math.max(0d, opacity), 1d);
        if (opacity != 0d) {
            if (opacity == 1d) {
                drawFigure(g);
            }

            Rectangle2D.Double drawingArea = getDrawingArea();
            Rectangle2D clipBounds = g.getClipBounds();

            this.intersect(clipBounds, drawingArea);
            this.drawFigureWithComposite(drawingArea, g, opacity);
        }
    }

    private void intersect(Rectangle2D clipBounds, Rectangle2D.Double drawingArea) {
        if (clipBounds != null) {
            Rectangle2D.intersect(drawingArea, clipBounds, drawingArea);
        }
    }

    private void drawFigureWithComposite(Rectangle2D.Double drawingArea, Graphics2D g, double opacity) {
        if (!drawingArea.isEmpty()) {
            BufferedImage buf = new BufferedImage(
                    Math.max(1, (int) ((2 + drawingArea.width) * g.getTransform().getScaleX())),
                    Math.max(1, (int) ((2 + drawingArea.height) * g.getTransform().getScaleY())),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D gr = buf.createGraphics();
            gr.scale(g.getTransform().getScaleX(), g.getTransform().getScaleY());
            gr.translate((int) -drawingArea.x, (int) -drawingArea.y);
            gr.setRenderingHints(g.getRenderingHints());
            drawFigure(gr);
            gr.dispose();
            Composite savedComposite = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity));
            g.drawImage(buf, (int) drawingArea.x, (int) drawingArea.y,
                    2 + (int) drawingArea.width, 2 + (int) drawingArea.height, null);
            g.setComposite(savedComposite);
        }
    }

    /**
     * This method is invoked before the rendered image of the figure is
     * composited.
     */
    public void drawFigure(Graphics2D g) {
        AffineTransform savedTransform = null;
        if (get(TRANSFORM) != null) {
            savedTransform = g.getTransform();
            g.transform(get(TRANSFORM));
        }
        Paint paint = SVGAttributeKeys.getFillPaint(this);
        if (paint != null) {
            g.setPaint(paint);
            drawFill(g);
        }
        paint = SVGAttributeKeys.getStrokePaint(this);
        if (paint != null && get(STROKE_WIDTH) > 0) {
            g.setPaint(paint);
            g.setStroke(SVGAttributeKeys.getStroke(this, 1.0));
            drawStroke(g);
        }
        if (get(TRANSFORM) != null) {
            g.setTransform(savedTransform);
        }
    }

    @Override
    public <T> void set(AttributeKey<T> key, T newValue) {
        if (key == TRANSFORM) {
            invalidate();
        }
        super.set(key, newValue);
    }

    @Override
    public Collection<Action> getActions(Point2D.Double p) {
        LinkedList<Action> actions = new LinkedList<Action>();
        if (get(TRANSFORM) != null) {
            ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
            actions.add(new AbstractAction(labels.getString("edit.removeTransform.text")) {
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent evt) {
                    willChange();
                    fireUndoableEditHappened(
                            TRANSFORM.setUndoable(SVGAttributedFigure.this, null)
                    );
                    changed();
                }
            });
        }
        return actions;
    }
}
