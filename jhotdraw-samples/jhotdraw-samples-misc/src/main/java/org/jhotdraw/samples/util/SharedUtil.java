package org.jhotdraw.samples.util;

import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.geom.GrowStroke;
import org.jhotdraw.samples.adapter.SharedAdapter;
import org.jhotdraw.samples.svg.SVGAttributeKeys;

import java.awt.*;

import static org.jhotdraw.draw.AttributeKeys.FILL_COLOR;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_GRADIENT;

public class SharedUtil {

    public Shape getHitShape(Shape cachedHitShape, SharedAdapter sharedAdapter, Figure figure) {
        if (cachedHitShape == null) {
            if (sharedAdapter.get(FILL_COLOR) != null || sharedAdapter.get(FILL_GRADIENT) != null) {
                cachedHitShape = new GrowStroke(
                        (float) SVGAttributeKeys.getStrokeTotalWidth(figure, 1.0) / 2f,
                        (float) SVGAttributeKeys.getStrokeTotalMiterLimit(figure, 1.0)).createStrokedShape(sharedAdapter.getTransformedShape());
            } else {
                cachedHitShape = SVGAttributeKeys.getHitStroke(figure, 1.0).createStrokedShape(sharedAdapter.getTransformedShape());
            }
        }
        return cachedHitShape;
    }
}
