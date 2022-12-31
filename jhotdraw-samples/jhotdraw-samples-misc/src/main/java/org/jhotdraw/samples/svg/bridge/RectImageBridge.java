package org.jhotdraw.samples.svg.bridge;

import org.jhotdraw.samples.SPI.RectImage;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class RectImageBridge {

    public void setBounds(Point2D.Double anchor, Point2D.Double lead, RectImage rectImage, Rectangle2D.Double rectangle) {
        rectImage.invalidateTransformedShape();
        rectangle.x = Math.min(anchor.x, lead.x);
        rectangle.y = Math.min(anchor.y, lead.y);
        rectangle.width = Math.max(0.1, Math.abs(lead.x - anchor.x));
        rectangle.height = Math.max(0.1, Math.abs(lead.y - anchor.y));
    }
    public void setBounds(Point2D.Double anchor, Point2D.Double lead, RectImage rectImage, RoundRectangle2D.Double rectangle) {
        rectImage.invalidateTransformedShape();
        rectangle.x = Math.min(anchor.x, lead.x);
        rectangle.y = Math.min(anchor.y, lead.y);
        rectangle.width = Math.max(0.1, Math.abs(lead.x - anchor.x));
        rectangle.height = Math.max(0.1, Math.abs(lead.y - anchor.y));
    }

}
